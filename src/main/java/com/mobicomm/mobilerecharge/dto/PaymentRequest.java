package com.mobicomm.mobilerecharge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class PaymentRequest {
    @NotNull(message = "Recharge ID is mandatory")
    @Positive(message = "Recharge ID must be positive")
    private Long rechargeId;

    @NotBlank(message = "Payment mode is mandatory")
    @Pattern(regexp = "^(UPI|Bank Transfer|Credit Card|Debit Card)$", message = "Invalid payment mode")
    private String paymentMode;

    @NotBlank(message = "Payment details are mandatory")
    private String paymentDetails;

    public Long getRechargeId() { return rechargeId; }
    public void setRechargeId(Long rechargeId) { this.rechargeId = rechargeId; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public String getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }
}