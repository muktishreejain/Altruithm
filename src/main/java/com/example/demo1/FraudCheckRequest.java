package com.example.demo1;

public class FraudCheckRequest {
    private String charityName;
    private Double amount;

    public FraudCheckRequest() {} // for reflective serializers

    public FraudCheckRequest(String charityName, Double amount) {
        this.charityName = charityName;
        this.amount = amount;
    }

    public String getCharityName() { return charityName; }
    public void setCharityName(String charityName) { this.charityName = charityName; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}