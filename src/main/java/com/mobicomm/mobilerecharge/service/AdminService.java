package com.mobicomm.mobilerecharge.service;

import com.mobicomm.mobilerecharge.dto.AdminLoginRequest;
import com.mobicomm.mobilerecharge.dto.SubscriberResponse;
import com.mobicomm.mobilerecharge.dto.RechargeHistoryResponse;
import com.mobicomm.mobilerecharge.model.Admin;
import com.mobicomm.mobilerecharge.model.Recharge;
import com.mobicomm.mobilerecharge.model.User;
import com.mobicomm.mobilerecharge.repository.AdminRepository;
import com.mobicomm.mobilerecharge.repository.RechargeRepository;
import com.mobicomm.mobilerecharge.repository.UserRepository;
import com.mobicomm.mobilerecharge.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class AdminService {
    private static final Logger logger = Logger.getLogger(AdminService.class.getName());

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String configuredAdminUsername;

    @Value("${admin.password}")
    private String configuredAdminPassword;

    @PostConstruct
    public void initAdmin() {
        if (!adminRepository.findByUsername("admin").isPresent()) {
            logger.info("Creating admin user");
            createAdmin(configuredAdminUsername, configuredAdminPassword);
        } else {
            logger.info("Admin user already exists");
        }
    }

    public String login(AdminLoginRequest loginRequest) {
        logger.info("Attempting admin login for username: " + loginRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                logger.info("Authentication successful for username: " + loginRequest.getUsername());
                return jwtService.generateToken(loginRequest.getUsername());
            } else {
                logger.warning("Authentication failed for username: " + loginRequest.getUsername());
                throw new RuntimeException("Invalid credentials");
            }
        } catch (AuthenticationException e) {
            logger.severe("Authentication exception for username: " + loginRequest.getUsername() + ": " + e.getMessage());
            throw new RuntimeException("Invalid credentials: " + e.getMessage());
        }
    }

    public void createAdmin(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        adminRepository.save(admin);
        logger.info("Admin user created: " + username);
    }

    public List<SubscriberResponse> getExpiringSubscribers() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        Date threshold = calendar.getTime();

        // Fetch latest recharges expiring within 3 days
        List<Recharge> recharges = rechargeRepository.findLatestExpiringRecharges(currentDate, threshold);
        return recharges.stream()
                .map(recharge -> new SubscriberResponse(
                        recharge.getUser().getName(),
                        recharge.getUser().getMobileNumber(),
                        recharge.getUser().getEmail(),
                        recharge.getPlan().getName(),
                        recharge.getExpiryDate()))
                .distinct()
                .sorted((a, b) -> a.getExpiryDate().compareTo(b.getExpiryDate()))
                .collect(Collectors.toList());
    }

    public List<RechargeHistoryResponse> getRechargeHistory(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + mobileNumber));
        List<Recharge> recharges = rechargeRepository.findByUser(user);
        return recharges.stream()
                .map(recharge -> new RechargeHistoryResponse(
                        recharge.getId(),
                        recharge.getPlan().getName(),
                        recharge.getPlan().getAmount(), // Updated to use plan.amount
                        recharge.getPaymentMode(),
                        recharge.getRechargeDate(),
                        recharge.getExpiryDate()))
                .sorted((a, b) -> b.getRechargeDate().compareTo(a.getRechargeDate()))
                .collect(Collectors.toList());
    }
}