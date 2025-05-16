package com.mobicomm.mobilerecharge.controller;

import com.mobicomm.mobilerecharge.dto.PlanResponse;
import com.mobicomm.mobilerecharge.dto.RechargeRequest;
import com.mobicomm.mobilerecharge.dto.PaymentRequest;
import com.mobicomm.mobilerecharge.dto.RegisterRequest;
import com.mobicomm.mobilerecharge.dto.ValidateRequest;
import com.mobicomm.mobilerecharge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recharge")
public class RechargeController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Validated @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateMobileNumber(@Validated @RequestBody ValidateRequest request) {
        boolean isValid = userService.validateMobileNumber(request);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/plans")
    public ResponseEntity<Map<String, List<PlanResponse>>> getAllPlans() {
        Map<String, List<PlanResponse>> plans = userService.getAllPlans();
        return ResponseEntity.ok(plans);
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> submitRecharge(@Validated @RequestBody RechargeRequest request) {
        Long rechargeId = userService.submitRecharge(request);
        return ResponseEntity.ok(Map.of("rechargeId", rechargeId));
    }

    @PostMapping("/payment")
    public ResponseEntity<String> processPayment(@Validated @RequestBody PaymentRequest request) {
        userService.processPayment(request);
        return ResponseEntity.ok("Payment processed successfully");
    }
}