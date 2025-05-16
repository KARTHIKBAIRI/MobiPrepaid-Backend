package com.mobicomm.mobilerecharge.dto;

import java.util.Date;
import java.util.Objects;

public class SubscriberResponse {
    private String name;
    private String mobileNumber;
    private String email;
    private String planName;
    private Date expiryDate;

    public SubscriberResponse(String name, String mobileNumber, String email, String planName, Date expiryDate) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.planName = planName;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriberResponse that = (SubscriberResponse) o;
        return Objects.equals(mobileNumber, that.mobileNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobileNumber);
    }
}