package com.example.demo1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestApp extends Application {
    private Label statusLabel;
    private TextArea responseArea;

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        statusLabel = new Label("Checking backend...");
        statusLabel.setStyle("-fx-font-size: 14;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter charity name (e.g., American Red Cross)");
        nameField.setPrefWidth(300);

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount (e.g., 1000)");
        amountField.setPrefWidth(300);

        Button checkBtn = new Button("Check Fraud Risk");
        checkBtn.setStyle("-fx-padding: 10 20; -fx-font-size: 12;");

        responseArea = new TextArea();
        responseArea.setEditable(false);
        responseArea.setWrapText(true);
        responseArea.setPrefHeight(200);

        checkBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String amount = amountField.getText().trim();

            if (name.isEmpty() || amount.isEmpty()) {
                Platform.runLater(() -> {
                    statusLabel.setText("Please enter both name and amount");
                    statusLabel.setTextFill(Color.RED);
                });
                return;
            }

            checkFraud(name, amount);
        });

        root.getChildren().addAll(
                statusLabel,
                new Label("Charity Name:"),
                nameField,
                new Label("Amount:"),
                amountField,
                checkBtn,
                new Label("Response:"),
                responseArea
        );

        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Altruithm - Charity Fraud Detection");
        stage.setScene(scene);
        stage.show();

        // Check backend health on startup
        checkBackendHealth();
    }

    private void checkBackendHealth() {
        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://10.3.250.187:8080/api/test"))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    Platform.runLater(() -> {
                        statusLabel.setText("✓ Connected to backend");
                        statusLabel.setTextFill(Color.GREEN);
                    });
                } else {
                    Platform.runLater(() -> {
                        statusLabel.setText("Backend error: " + response.statusCode());
                        statusLabel.setTextFill(Color.RED);
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Backend not running: " + e.getMessage());
                    statusLabel.setTextFill(Color.RED);
                });
            }
        }).start();
    }

    private void checkFraud(String charityName, String amount) {
        new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    statusLabel.setText("Checking...");
                    statusLabel.setTextFill(Color.BLUE);
                });

                String json = "{\"charityName\":\"" + charityName + "\",\"amount\":" + amount + "}";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://10.3.250.187:8080/api/fraud/check"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                HttpResponse<String> response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String responseBody = response.body();
                    Platform.runLater(() -> {
                        responseArea.setText(responseBody);
                        statusLabel.setText("✓ Success!");
                        statusLabel.setTextFill(Color.GREEN);
                    });
                } else {
                    Platform.runLater(() -> {
                        responseArea.setText("Error: " + response.statusCode() + "\n" + response.body());
                        statusLabel.setText("Error: " + response.statusCode());
                        statusLabel.setTextFill(Color.RED);
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    responseArea.setText("Exception: " + e.getMessage());
                    statusLabel.setText("Failed: " + e.getMessage());
                    statusLabel.setTextFill(Color.RED);
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}