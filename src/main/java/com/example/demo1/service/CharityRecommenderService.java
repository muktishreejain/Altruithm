package com.example.demo1.service;

import com.example.demo1.model.Charity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CharityRecommenderService {

    private static final String BASE_URL = "http://127.0.0.1:5000"; // Flask API base

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Charity> getCharitiesByInterests(String interests) throws IOException, InterruptedException {
        String url = BASE_URL + "/recommend_interest?interest=" + encode(interests);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<Charity>>() {});
    }

    public List<ScoredCharity> getSimilarCharities(String charityName, int topN) throws IOException, InterruptedException {
        String url = BASE_URL + "/recommend_similar?charity_name=" + encode(charityName) + "&top_n=" + topN;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Charity> charities = mapper.readValue(response.body(), new TypeReference<List<Charity>>() {});
        return charities.stream()
                .map(c -> new ScoredCharity(c, 1.0)) // optional score placeholder
                .toList();
    }

    private String encode(String text) {
        return text.replace(" ", "%20");
    }

    // Inner record for scored charities
    public record ScoredCharity(Charity charity, double score) {}
}
