package com.example.demo1.service;

import com.example.demo1.model.Charity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CharityRecommenderService {

    private static final String BASE_URL = "http://127.0.0.1:5000"; // Flask API base

    private final HttpClient client = HttpClient.newHttpClient();

    public List<Charity> getCharitiesByInterests(String interests) throws IOException, InterruptedException {
        String url = BASE_URL + "/recommend_interest?interest=" + encode(interests);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return parseCharityList(response.body());
    }

    public List<ScoredCharity> getSimilarCharities(String charityName, int topN) throws IOException, InterruptedException {
        String url = BASE_URL + "/recommend_similar?charity_name=" + encode(charityName) + "&top_n=" + topN;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Charity> charities = parseCharityList(response.body());
        List<ScoredCharity> scoredList = new ArrayList<>();
        for (Charity c : charities) {
            scoredList.add(new ScoredCharity(c, 1.0)); // Optional score placeholder
        }
        return scoredList;
    }

    private List<Charity> parseCharityList(String json) {
        JSONArray arr = new JSONArray(json);
        List<Charity> charities = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            Charity charity = new Charity();
            charity.setName(obj.optString("name"));
            charity.setCategory(obj.optString("category"));
            charity.setDescription(obj.optString("description"));
            charities.add(charity);
        }
        return charities;
    }

    private String encode(String text) {
        return text.replace(" ", "%20");
    }

    public record ScoredCharity(Charity charity, double score) {}
}

