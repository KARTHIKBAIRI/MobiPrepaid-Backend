package com.mobicomm.mobilerecharge.dto;
import java.util.Date;
public class ValidateResponse {
    private boolean hasActivePlan;
    private String message;
    private String planName;
    private Date expiryDate;
    public ValidateResponse(boolean hasActivePlan, String message, String planName, Date expiryDate) {
        this.hasActivePlan = hasActivePlan;
        this.message = message;
        this.planName = planName;
        this.expiryDate = expiryDate;
    }
    public boolean isHasActivePlan() { return hasActivePlan; }
    public String getMessage() { return message; }
    public String getPlanName() { return planName; }
    public Date getExpiryDate() { return expiryDate; }
}