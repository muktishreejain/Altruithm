package com.example.demo1;

import java.util.List;

public class FraudCheckResponse {
    private String riskLevel;
    private Double riskScore;
    private List<String> warnings;
    private String recommendation;
    private String charityName;
    private Double charityScore;
    private String category;
    private String location;

    // Getters
    public String getRiskLevel() { return riskLevel; }
    public Double getRiskScore() { return riskScore; }
    public List<String> getWarnings() { return warnings; }
    public String getRecommendation() { return recommendation; }
    public String getCharityName() { return charityName; }
    public Double getCharityScore() { return charityScore; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
}