package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HelloController {

    @FXML private TextField inputField;
    @FXML private RadioButton similarRadio;
    @FXML private RadioButton interestRadio;
    @FXML private TextArea resultArea;
    @FXML private ToggleGroup modeGroup;

    @FXML
    protected void onGetRecommendations() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) {
            resultArea.setText("Please enter a charity name or interest!");
            return;
        }

        String mode = similarRadio.isSelected() ? "similar" : "interest";
        List<String> recommendations = getRecommendations(mode, input);

        resultArea.clear();
        if (recommendations.isEmpty()) {
            resultArea.setText("No results found!");
        } else {
            for (String rec : recommendations) {
                resultArea.appendText(rec + "\n");
            }
        }
    }

    private List<String> getRecommendations(String mode, String input) {
        List<String> results = new ArrayList<>();
        try {
            //ProcessBuilder pb = new ProcessBuilder("python", "model/api_server.py", "interest", userInput);
           // pb.redirectErrorStream(true);
           // Process process = pb.start();
            
            //BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line;
            //while ((line = reader.readLine()) != null) {
            //    System.out.println(line);
            //}


             //process.waitFor();
        } catch (Exception e) {
            results.add("Error: " + e.getMessage());
        }
        return results;
    }
}