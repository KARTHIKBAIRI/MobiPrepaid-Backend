package com.mobicomm.mobilerecharge.dto;

import java.util.Date;

public class RechargeHistoryResponse {
    private Long rechargeId;
    private String planName;
    private Double amount;
    private String paymentMode;
    private Date rechargeDate;
    private Date expiryDate;

    public RechargeHistoryResponse(Long rechargeId, String planName, Double amount, String paymentMode, Date rechargeDate, Date expiryDate) {
        this.rechargeId = rechargeId;
        this.planName = planName;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.rechargeDate = rechargeDate;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public Long getRechargeId() { return rechargeId; }
    public void setRechargeId(Long rechargeId) { this.rechargeId = rechargeId; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public Date getRechargeDate() { return rechargeDate; }
    public void setRechargeDate(Date rechargeDate) { this.rechargeDate = rechargeDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
}