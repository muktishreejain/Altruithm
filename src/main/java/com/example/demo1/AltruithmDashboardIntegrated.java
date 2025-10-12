package com.example.demo1;

import com.example.demo1.model.FraudCheckResponse;
import com.example.demo1.service.AltruithmApiService;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.ScrollPane;

public class AltruithmDashboardIntegrated extends AltruithmMainDashboard {

    protected AltruithmApiService apiService;
    protected Label connectionStatus;

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        this.apiService = new AltruithmApiService();
        super.start(primaryStage);
        // Will check backend on form display, no need here
    }

    // Only 1 method with this signature!
    protected void checkBackendConnection() {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return apiService.isApiHealthy();
            }
        };
        task.setOnSucceeded(e -> {
            boolean isHealthy = task.getValue();
            if (connectionStatus != null) {
                if (isHealthy) {
                    connectionStatus.setText("✓ Backend Connected");
                    connectionStatus.setTextFill(Color.web("#00ff88"));
                } else {
                    connectionStatus.setText("✗ Backend Offline");
                    connectionStatus.setTextFill(Color.web("#ff4444"));
                }
            }
        });
        new Thread(task).start();
    }

    // Match 'protected' like in superclass, and correct return type
    protected ScrollPane createFraudDetectionContent() {
        VBox content = new VBox(25.0);
        content.setPadding(new Insets(30.0));
        content.getStyleClass().add("tab-content");

        Label title = new Label("🛡 Fraud Detection");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28.0));
        title.setTextFill(Color.WHITE);

        connectionStatus = new Label("Checking backend...");
        connectionStatus.setFont(Font.font("Arial", FontWeight.BOLD, 14.0));
        connectionStatus.setTextFill(Color.YELLOW);

        checkBackendConnection();

        VBox fraudCheckForm = createRealFraudCheckForm();

        content.getChildren().addAll(title, connectionStatus, fraudCheckForm);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    private VBox createRealFraudCheckForm() {
        VBox form = new VBox(15.0);
        form.getStyleClass().add("card-container");
        form.setPadding(new Insets(25.0));
        form.setMaxWidth(600);

        Label formTitle = new Label("🔍 Check Charity Fraud Risk");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20.0));
        formTitle.setTextFill(Color.WHITE);

        VBox charityBox = new VBox(8.0);
        Label charityLabel = new Label("Charity Name:");
        charityLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14.0));
        charityLabel.setTextFill(Color.web("#b0b8d0"));
        TextField charityField = new TextField();
        charityField.setPromptText("e.g., American Red Cross");
        charityField.setPrefWidth(500.0);
        charityBox.getChildren().addAll(charityLabel, charityField);

        VBox amountBox = new VBox(8.0);
        Label amountLabel = new Label("Donation Amount (₹):");
        amountLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14.0));
        amountLabel.setTextFill(Color.web("#b0b8d0"));
        TextField amountField = new TextField();
        amountField.setPromptText("e.g., 1000");
        amountField.setPrefWidth(500.0);
        amountBox.getChildren().addAll(amountLabel, amountField);

        HBox buttonBox = new HBox(15.0);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button checkButton = new Button("🔍 Check Risk");
        checkButton.getStyleClass().add("refresh-button");
        // correct access, or cast to Parent if still protected/private
        // this.addButtonHoverEffect(checkButton);

        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("refresh-button");
        // this.addButtonHoverEffect(clearButton);

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);
        loadingIndicator.setPrefSize(30.0, 30.0);

        buttonBox.getChildren().addAll(checkButton, clearButton, loadingIndicator);

        VBox resultsBox = new VBox(15.0);
        resultsBox.getStyleClass().add("card-container");
        resultsBox.setPadding(new Insets(20.0));
        resultsBox.setVisible(false);

        Label riskLevelLabel = new Label();
        riskLevelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24.0));

        Label riskScoreLabel = new Label();
        riskScoreLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16.0));
        riskScoreLabel.setTextFill(Color.WHITE);

        Label recommendationLabel = new Label();
        recommendationLabel.setWrapText(true);
        recommendationLabel.setTextFill(Color.web("#b0b8d0"));

        TextArea warningsArea = new TextArea();
        warningsArea.setEditable(false);
        warningsArea.setWrapText(true);
        warningsArea.setPrefHeight(100.0);
        warningsArea.setStyle("-fx-control-inner-background: #1a1f3a; -fx-text-fill: white;");

        resultsBox.getChildren().addAll(riskLevelLabel, riskScoreLabel, recommendationLabel, warningsArea);

        clearButton.setOnAction(e -> {
            charityField.clear();
            amountField.clear();
            resultsBox.setVisible(false);
        });

        checkButton.setOnAction(e -> {
            String charityName = charityField.getText().trim();
            String amountText = amountField.getText().trim();

            if (charityName.isEmpty() || amountText.isEmpty()) {
                showAlert("Error", "Please fill all fields");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (Exception ex) {
                showAlert("Error", "Invalid amount");
                return;
            }

            loadingIndicator.setVisible(true);
            checkButton.setDisable(true);

            Task<FraudCheckResponse> fraudTask = new Task<>() {
                @Override
                protected FraudCheckResponse call() throws Exception {
                    return apiService.checkFraud(charityName, amount);
                }
            };

            fraudTask.setOnSucceeded(event -> {
                FraudCheckResponse response = fraudTask.getValue();
                resultsBox.setVisible(true);

                riskLevelLabel.setText("Risk: " + response.getRiskLevel());
                switch (response.getRiskLevel()) {
                    case "LOW":
                        riskLevelLabel.setTextFill(Color.web("#00ff88"));
                        break;
                    case "MEDIUM":
                        riskLevelLabel.setTextFill(Color.web("#ffaa00"));
                        break;
                    case "HIGH":
                        riskLevelLabel.setTextFill(Color.web("#ff4444"));
                        break;
                }

                riskScoreLabel.setText(String.format("Score: %.2f", response.getRiskScore()));
                recommendationLabel.setText(response.getRecommendation());

                StringBuilder warnings = new StringBuilder();
                if (response.getWarnings() != null) {
                    for (String warning : response.getWarnings()) {
                        warnings.append("• ").append(warning).append("\n");
                    }
                }
                warningsArea.setText(warnings.toString());

                loadingIndicator.setVisible(false);
                checkButton.setDisable(false);
            });

            fraudTask.setOnFailed(event -> {
                showAlert("Error", "Failed: " + fraudTask.getException().getMessage());
                loadingIndicator.setVisible(false);
                checkButton.setDisable(false);
            });

            new Thread(fraudTask).start();
        });

        form.getChildren().addAll(
                formTitle,
                charityBox,
                amountBox,
                buttonBox,
                resultsBox
        );

        return form;
    }

    private void showAlert(String error, String pleaseFillAllFields) {

    }

    // Use superclass' showAlert for error dialogs
}
