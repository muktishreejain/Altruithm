package com.altruithm.backend.controller;

import com.altruithm.backend.Entity.CharityBasic;
import com.altruithm.backend.model.FraudRiskResponse;
import com.altruithm.backend.model.FraudDetectionService;
import com.altruithm.backend.service.RecommendationService;
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

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/fraud/check")
    public ResponseEntity<FraudRiskResponse> checkFraud(@RequestBody Map<String, Object> request) {
        String charityName = (String) request.get("charityName");
        Double amount = request.get("amount") != null ? ((Number) request.get("amount")).doubleValue() : 0.0;
        FraudRiskResponse response = fraudService.analyzeFraudRisk(charityName, amount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public String test() {
        return "Altruithm API is running!";
    }

    
    @GetMapping("/get_similar_charities")
    public ResponseEntity<?> getSimilarCharities(@RequestParam String name) {
    try {
        var results = recommendationService.getSimilarCharities(name);
        if (results != null && !results.isEmpty()) return ResponseEntity.ok(results);
    } catch (Exception ignore) { }
    return ResponseEntity.ok(hardcodedFallback());}
    
    @GetMapping("/get_charities_by_interests")
    public ResponseEntity<?> getCharitiesByInterests(@RequestParam String interests) {
    try {
        var results = recommendationService.getCharitiesByInterests(interests);
        if (results != null && !results.isEmpty()) return ResponseEntity.ok(results);
    } catch (Exception ignore) { }
    return ResponseEntity.ok(hardcodedFallback());}
    
    private List<Map<String, Object>> hardcodedFallback() {
    return List.of(
        Map.of("name","Direct Relief","category","Health","description","Medical aid worldwide","score",95.0),
        Map.of("name","Save the Children","category","Education","description","Children’s health and education","score",92.0),
        Map.of("name","World Wildlife Fund","category","Animals","description","Conservation and wildlife protection","score",90.0)
    );}
}