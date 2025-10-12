package com.altruithm.backend.controller;

import com.altruithm.backend.Entity.CharityBasic;
import com.altruithm.backend.model.FraudDetectionService;
import com.altruithm.backend.model.FraudRiskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AltruithmController {

    @Autowired
    private FraudDetectionService fraudService;

    @GetMapping("/test")
    public String test() {
        return "Altruithm Fraud Detection API is running!";
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") int limit) {

        List<CharityBasic> charities = fraudService.getRecommendedCharities(category, limit);
        return ResponseEntity.ok(charities);
    }

    @PostMapping("/fraud/check")
    public ResponseEntity<FraudRiskResponse> checkFraud(@RequestBody Map<String, Object> request) {
        String charityName = (String) request.get("charityName");
        Double amount = request.get("amount") != null ?
                ((Number) request.get("amount")).doubleValue() : 0.0;

        FraudRiskResponse response = fraudService.analyzeFraudRisk(charityName, amount);
        return ResponseEntity.ok(response);
    }
}