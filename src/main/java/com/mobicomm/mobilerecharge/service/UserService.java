package com.mobicomm.mobilerecharge.service;

import com.mobicomm.mobilerecharge.dto.PlanResponse;
import com.mobicomm.mobilerecharge.dto.RechargeRequest;
import com.mobicomm.mobilerecharge.dto.PaymentRequest;
import com.mobicomm.mobilerecharge.dto.RegisterRequest;
import com.mobicomm.mobilerecharge.dto.ValidateRequest;
import com.mobicomm.mobilerecharge.exception.ResourceNotFoundException;
import com.mobicomm.mobilerecharge.model.Plan;
import com.mobicomm.mobilerecharge.model.Recharge;
import com.mobicomm.mobilerecharge.model.User;
import com.mobicomm.mobilerecharge.repository.PlanRepository;
import com.mobicomm.mobilerecharge.repository.RechargeRepository;
import com.mobicomm.mobilerecharge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if (userRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            throw new RuntimeException("Mobile number already registered");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setMobileNumber(request.getMobileNumber());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        userRepository.save(user);
    }

    public boolean validateMobileNumber(ValidateRequest request) {
        return userRepository.findByMobileNumber(request.getMobileNumber())
                .map(User::isActive)
                .orElse(false);
    }

    public Map<String, List<PlanResponse>> getAllPlans() {
        List<PlanResponse> plans = planRepository.findAll().stream()
                .filter(Plan::isActive)
                .map(plan -> new PlanResponse(
                        plan.getId(),
                        plan.getName(),
                        plan.getCategory(),
                        plan.getAmount(),
                        plan.getValidityDays(),
                        plan.getDescription()
                ))
                .collect(Collectors.toList());
        return plans.stream()
                .collect(Collectors.groupingBy(PlanResponse::getCategory));
    }

    public Long submitRecharge(RechargeRequest request) {
        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .filter(User::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Active user not found: " + request.getMobileNumber()));
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + request.getPlanId()));
        Date rechargeDate = new Date();
        Date baseDate = rechargeDate;

        // Check for latest active recharge
        Optional<Recharge> latestRecharge = rechargeRepository.findLatestByUser(user);
        if (latestRecharge.isPresent()) {
            Date latestExpiry = latestRecharge.get().getExpiryDate();
            if (latestExpiry.after(rechargeDate)) {
                baseDate = latestExpiry; // Extend from the latest expiry date
            }
        }

        // Calculate new expiry date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);
        calendar.add(Calendar.DAY_OF_MONTH, plan.getValidityDays());
        Date expiryDate = calendar.getTime();

        Recharge recharge = new Recharge(user, plan, "PENDING", plan.getAmount(), rechargeDate, expiryDate);
        recharge = rechargeRepository.save(recharge);
        return recharge.getId();
    }

    public void processPayment(PaymentRequest request) {
        Recharge recharge = rechargeRepository.findById(request.getRechargeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recharge not found: " + request.getRechargeId()));
        User user = recharge.getUser();
        validatePaymentDetails(request);
        recharge.setPaymentMode(request.getPaymentMode());
        rechargeRepository.save(recharge);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String emailBody = String.format(
                    "Dear %s,\n\nYour recharge for mobile number %s has been successful.\nPlan: %s\nAmount: %.2f\nPayment Mode: %s\nRecharge Date: %s\nExpiry Date: %s\n\nThank you,\nMobi-Comm Services",
                    user.getName(), user.getMobileNumber(), recharge.getPlan().getName(), recharge.getAmount(),
                    request.getPaymentMode(), dateFormat.format(recharge.getRechargeDate()), dateFormat.format(recharge.getExpiryDate()));
            emailService.sendConfirmationEmail(user.getEmail(), "Recharge Confirmation", emailBody);
            System.out.println("Confirmation email sent to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send email to " + user.getEmail() + ": " + e.getMessage());
        }
    }

    private void validatePaymentDetails(PaymentRequest request) {
        String details = request.getPaymentDetails();
        String mode = request.getPaymentMode();
        try {
            if ("UPI".equals(mode)) {
                if (!details.contains("upiId") || !details.matches(".*[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+.*")) {
                    throw new IllegalArgumentException("Invalid UPI ID format");
                }
            } else if ("Credit Card".equals(mode) || "Debit Card".equals(mode)) {
                if (!details.contains("cardNumber") || !details.contains("cardholderName") ||
                    !details.contains("expiryDate") || !details.contains("cvv")) {
                    throw new IllegalArgumentException("Missing card details");
                }
                if (!details.matches(".*cardNumber.*\\d{16}.*")) {
                    throw new IllegalArgumentException("Card number must be 16 digits");
                }
                if (!details.matches(".*cvv.*\\d{3}.*")) {
                    throw new IllegalArgumentException("CVV must be 3 digits");
                }
            } else if ("Bank Transfer".equals(mode)) {
                if (!details.contains("bankName") || !details.contains("accountNumber") ||
                    !details.contains("ifscCode") || !details.contains("accountHolderName")) {
                    throw new IllegalArgumentException("Missing bank details");
                }
                if (!details.matches(".*ifscCode.*[A-Z]{4}0[A-Z0-9]{6}.*")) {
                    throw new IllegalArgumentException("Invalid IFSC code");
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Payment details validation failed: " + e.getMessage());
        }
    }
}