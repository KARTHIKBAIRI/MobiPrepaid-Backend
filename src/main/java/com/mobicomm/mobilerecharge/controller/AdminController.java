package com.mobicomm.mobilerecharge.controller;

import com.mobicomm.mobilerecharge.dto.AdminLoginRequest;
import com.mobicomm.mobilerecharge.dto.SubscriberResponse;
import com.mobicomm.mobilerecharge.dto.RechargeHistoryResponse;
import com.mobicomm.mobilerecharge.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequest request) {
        String token = adminService.login(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/subscribers/expiring")
    public ResponseEntity<List<SubscriberResponse>> getExpiringSubscribers() {
        List<SubscriberResponse> subscribers = adminService.getExpiringSubscribers();
        return ResponseEntity.ok(subscribers);
    }

    @GetMapping("/subscribers/{mobileNumber}/history")
    public ResponseEntity<List<RechargeHistoryResponse>> getRechargeHistory(@PathVariable String mobileNumber) {
        List<RechargeHistoryResponse> history = adminService.getRechargeHistory(mobileNumber);
        return ResponseEntity.ok(history);
    }
}