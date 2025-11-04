package com.example.demo1.controller;

import com.example.demo1.model.Charity;
import com.example.demo1.service.CharityRecommenderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML
    private TextField interestInput;  // user types "education", etc.
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Charity> resultsTable;
    @FXML
    private TableColumn<Charity, String> nameColumn;
    @FXML
    private TableColumn<Charity, String> categoryColumn;
    @FXML
    private TableColumn<Charity, String> descriptionColumn;

    private final CharityRecommenderService recommender = new CharityRecommenderService();

    @FXML
    public void initialize() {
        // Map table columns
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        categoryColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));
        descriptionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        // On click: call Flask API
        searchButton.setOnAction(e -> searchByInterest());
    }

    private void searchByInterest() {
        String interest = interestInput.getText().trim();
        if (interest.isEmpty()) {
            showAlert("Please enter an interest or topic.");
            return;
        }

        new Thread(() -> {
            try {
                List<Charity> charities = recommender.getCharitiesByInterests(interest);
                ObservableList<Charity> data = FXCollections.observableArrayList(charities);

                // Update UI (must be on FX thread)
                javafx.application.Platform.runLater(() -> resultsTable.setItems(data));

            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
                javafx.application.Platform.runLater(() -> showAlert("Error fetching data: " + ex.getMessage()));
            }
        }).start();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
