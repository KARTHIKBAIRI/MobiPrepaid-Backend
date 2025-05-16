package com.mobicomm.mobilerecharge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ValidateRequest {
    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number must be 10 digits starting with 6-9")
    private String mobileNumber;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}