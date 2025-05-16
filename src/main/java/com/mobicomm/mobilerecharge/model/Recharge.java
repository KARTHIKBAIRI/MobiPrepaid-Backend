package com.mobicomm.mobilerecharge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Recharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @NotBlank(message = "Payment mode is mandatory")
    private String paymentMode; // e.g., PENDING, UPI, Card, Bank Transfer

    @NotNull(message = "Amount is mandatory")
    private Double amount;

    @NotNull(message = "Recharge date is mandatory")
    private Date rechargeDate;

    @NotNull(message = "Expiry date is mandatory")
    private Date expiryDate;

    public Recharge() {}

    public Recharge(User user, Plan plan, String paymentMode, Double amount, Date rechargeDate, Date expiryDate) {
        this.user = user;
        this.plan = plan;
        this.paymentMode = paymentMode;
        this.amount = amount;
        this.rechargeDate = rechargeDate;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Date getRechargeDate() { return rechargeDate; }
    public void setRechargeDate(Date rechargeDate) { this.rechargeDate = rechargeDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
}