package com.example.demo1;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AltruithmApiService {

    private static final String BASE_URL = "http://10.3.250.187:8080/api";
    private final HttpClient httpClient;
    private final Gson gson;

    public AltruithmApiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    public boolean isApiHealthy() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/test"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("API Check Failed: " + e.getMessage());
            return false;
        }
    }

    public FraudCheckResponse checkFraud(String charityName, Double amount) throws Exception {
        FraudCheckRequest requestBody = new FraudCheckRequest(charityName, amount);
        String jsonBody = gson.toJson(requestBody);

        System.out.println("Sending: " + jsonBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/fraud/check"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(15))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Response: " + response.statusCode());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), FraudCheckResponse.class);
        } else {
            throw new Exception("Error: " + response.statusCode());
        }
    }

    public AltruithmDashboard.CharityRecommendationResponse getSimilarCharities(String charityName) throws Exception {
        SimilarCharitiesRequest requestBody = new SimilarCharitiesRequest(charityName);
        String jsonBody = gson.toJson(requestBody);

        System.out.println("Sending Similar Charities Request: " + jsonBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/charities/similar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(15))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Similar Charities Response: " + response.statusCode());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), AltruithmDashboard.CharityRecommendationResponse.class);
        } else {
            throw new Exception("Error getting similar charities: " + response.statusCode());
        }
    }

    public AltruithmDashboard.CharityRecommendationResponse getCharitiesByInterests(String interests) throws Exception {
        CharitiesByInterestsRequest requestBody = new CharitiesByInterestsRequest(interests);
        String jsonBody = gson.toJson(requestBody);

        System.out.println("Sending Charities By Interests Request: " + jsonBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/charities/by-interests"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(15))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Charities By Interests Response: " + response.statusCode());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), AltruithmDashboard.CharityRecommendationResponse.class);
        } else {
            throw new Exception("Error getting charities by interests: " + response.statusCode());
        }
    }

    // Request classes
    static class SimilarCharitiesRequest {
        private String charityName;

        public SimilarCharitiesRequest(String charityName) {
            this.charityName = charityName;
        }

        public String getCharityName() {
            return charityName;
        }
    }

    static class CharitiesByInterestsRequest {
        private String interests;

        public CharitiesByInterestsRequest(String interests) {
            this.interests = interests;
        }

        public String getInterests() {
            return interests;
        }
    }
}