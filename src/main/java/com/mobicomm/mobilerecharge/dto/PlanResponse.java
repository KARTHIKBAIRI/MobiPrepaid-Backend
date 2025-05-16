package com.mobicomm.mobilerecharge.dto;

public class PlanResponse {
    private Long id;
    private String name;
    private String category;
    private Double amount;
    private Integer validityDays;
    private String description;

    public PlanResponse(Long id, String name, String category, Double amount, Integer validityDays, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.validityDays = validityDays;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Integer getValidityDays() { return validityDays; }
    public void setValidityDays(Integer validityDays) { this.validityDays = validityDays; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}