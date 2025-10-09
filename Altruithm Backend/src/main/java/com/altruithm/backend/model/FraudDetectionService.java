package com.altruithm.backend.model;

import com.altruithm.backend.Entity.CharityBasic;
import com.altruithm.backend.Entity.CharityFinancial;
import com.altruithm.backend.repository.CharityBasicRepository;
import com.altruithm.backend.repository.CharityFinancialRepository;
import com.altruithm.backend.model.FraudRiskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FraudDetectionService {

    @Autowired
    private CharityBasicRepository charityBasicRepository;

    @Autowired
    private CharityFinancialRepository charityFinancialRepository;

    public FraudRiskResponse analyzeFraudRisk(String charityName, Double donationAmount) {
        List<String> warnings = new ArrayList<>();
        double riskScore = 0.0;

        // Try to find charity in database
        Optional<CharityBasic> basicOpt = charityBasicRepository.findByNameIgnoreCase(charityName);
        Optional<CharityFinancial> financialOpt = charityFinancialRepository.findByNameIgnoreCase(charityName);

        if (basicOpt.isEmpty() && financialOpt.isEmpty()) {
            // Charity not found in database
            warnings.add("WARNING: Charity not found in our verified database");
            riskScore += 0.5;
            return buildResponse(riskScore, warnings, null, null);
        }

        CharityBasic basic = basicOpt.orElse(null);
        CharityFinancial financial = financialOpt.orElse(null);

        // Analysis 1: Overall Score Check
        if (basic != null && basic.getScore() != null) {
            double score = basic.getScore();
            if (score < 50) {
                warnings.add("CRITICAL: Low charity rating (Score: " + String.format("%.1f", score) + "/100)");
                riskScore += 0.3;
            } else if (score < 70) {
                warnings.add("WARNING: Below average charity rating (Score: " + String.format("%.1f", score) + "/100)");
                riskScore += 0.15;
            }
        }

        // Analysis 2: Fund Efficiency
        if (basic != null && basic.getFundEfficiency() != null) {
            double efficiency = basic.getFundEfficiency();
            if (efficiency < 0.6) {
                warnings.add("CRITICAL: Low fund efficiency (" + String.format("%.1f", efficiency * 100) + "% of donations reach programs)");
                riskScore += 0.25;
            } else if (efficiency < 0.75) {
                warnings.add("WARNING: Moderate fund efficiency (" + String.format("%.1f", efficiency * 100) + "%)");
                riskScore += 0.1;
            }
        }

        // Analysis 3: Administrative Expenses (from financial data)
        if (financial != null && financial.getAdminExpensePercent() != null) {
            double adminExpense = financial.getAdminExpensePercent();
            if (adminExpense > 25) {
                warnings.add("CRITICAL: High administrative expenses (" + String.format("%.1f", adminExpense) + "% of budget)");
                riskScore += 0.25;
            } else if (adminExpense > 15) {
                warnings.add("WARNING: Elevated administrative expenses (" + String.format("%.1f", adminExpense) + "%)");
                riskScore += 0.1;
            }
        }

        // Analysis 4: Program Expense Percentage
        if (financial != null && financial.getProgramExpensePercent() != null) {
            double programExpense = financial.getProgramExpensePercent();
            if (programExpense < 60) {
                warnings.add("CRITICAL: Low program spending (" + String.format("%.1f", programExpense) + "% goes to programs)");
                riskScore += 0.2;
            } else if (programExpense < 75) {
                warnings.add("WARNING: Moderate program spending (" + String.format("%.1f", programExpense) + "%)");
                riskScore += 0.1;
            }
        }

        // Analysis 5: Financial Transparency
        if (basic != null && basic.getFscore() != null) {
            double fscore = basic.getFscore();
            if (fscore < 50) {
                warnings.add("WARNING: Limited financial transparency (F-Score: " + String.format("%.1f", fscore) + "/100)");
                riskScore += 0.15;
            }
        }

        // Analysis 6: Status Check
        if (basic != null && basic.getStatus() != null && basic.getStatus() != 1) {
            warnings.add("CRITICAL: Charity status is not active");
            riskScore += 0.4;
        }

        // Analysis 7: Impact Efficiency
        if (basic != null && basic.getImpactEfficiency() != null) {
            double impactEff = basic.getImpactEfficiency();
            if (impactEff < 0.5) {
                warnings.add("WARNING: Low impact efficiency score (" + String.format("%.2f", impactEff) + ")");
                riskScore += 0.1;
            }
        }

        // Analysis 8: Donation Amount Check
        if (donationAmount != null && basic != null && basic.getTotalRevenue() != null) {
            if (donationAmount > basic.getTotalRevenue() * 0.1) {
                warnings.add("WARNING: Donation is unusually large (>10% of charity's annual revenue)");
                riskScore += 0.05;
            }
        }

        // Positive indicators
        if (warnings.isEmpty()) {
            warnings.add("SUCCESS: All fraud checks passed successfully");
            warnings.add("SUCCESS: Charity is in good standing");
        }

        // Cap risk score at 1.0
        riskScore = Math.min(riskScore, 1.0);

        return buildResponse(riskScore, warnings, basic, financial);
    }

    private FraudRiskResponse buildResponse(double riskScore, List<String> warnings,
                                            CharityBasic basic, CharityFinancial financial) {
        FraudRiskResponse response = new FraudRiskResponse();
        response.setRiskScore(riskScore);
        response.setWarnings(warnings);

        // Determine risk level
        if (riskScore < 0.3) {
            response.setRiskLevel("LOW");
            response.setRecommendation("SUCCESS: This charity appears safe to donate to.");
        } else if (riskScore < 0.6) {
            response.setRiskLevel("MEDIUM");
            response.setRecommendation("WARNING: Exercise caution. Review the warnings before donating.");
        } else {
            response.setRiskLevel("HIGH");
            response.setRecommendation("CRITICAL: High risk detected. Consider donating to a different charity.");
        }

        // Add charity details if available
        if (basic != null) {
            response.setCharityName(basic.getName());
            response.setCharityScore(basic.getScore());
            response.setCategory(basic.getCategory());
            response.setLocation(basic.getCity() + ", " + basic.getStateCode());
        }

        if (financial != null && response.getCharityName() == null) {
            response.setCharityName(financial.getName());
            response.setCharityScore(financial.getScore());
            response.setCategory(financial.getCategory());
        }

        return response;
    }

    public List<CharityBasic> getRecommendedCharities(String category, int limit) {
        List<CharityBasic> charities;

        if (category != null && !category.isEmpty()) {
            charities = charityBasicRepository.findByCategory(category);
        } else {
            charities = charityBasicRepository.findAll();
        }

        // Filter and sort by score
        return charities.stream()
                .filter(c -> c.getScore() != null && c.getScore() >= 75)
                .filter(c -> c.getFundEfficiency() != null && c.getFundEfficiency() >= 0.7)
                .sorted((c1, c2) -> Double.compare(c2.getScore(), c1.getScore()))
                .limit(limit)
                .toList();
    }
}