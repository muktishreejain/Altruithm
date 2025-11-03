package com.altruithm.backend.model;

import lombok.Data;
import java.util.List;

@Data
public class FraudRiskResponse {
    private String riskLevel;
    private Double riskScore;
    private List<String> warnings;
    private String recommendation;

    // Additional charity info
    private String charityName;
    private Double charityScore;
    private String category;
    private String location;
}