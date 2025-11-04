    package com.example.demo1;
    import javafx.animation.KeyFrame;
    import javafx.animation.Timeline;
    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.concurrent.Task;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.Node;
    import javafx.scene.Scene;
    import javafx.scene.chart.*;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.effect.DropShadow;
    import javafx.scene.effect.Glow;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;
    import javafx.scene.shape.Rectangle;
    import javafx.scene.text.Font;
    import javafx.scene.text.FontWeight;
    import javafx.stage.Stage;
    import javafx.util.Duration;
    import javafx.scene.web.WebView;
    import javafx.scene.web.WebEngine;
    import javafx.concurrent.Worker;
    import javafx.scene.layout.Region;
    import javafx.scene.layout.Priority;




    public class AltruithmDashboard extends Application {

        private TableView<FundingProject> projectTable;
        private LineChart<String, Number> impactTrendChart;
        private PieChart fundingDistributionChart;
        private BarChart<String, Number> regionalImpactChart;
        private StackPane contentArea;
        private Stage primaryStage;

        @Override
        // Add these lines in the start() method after creating the scene:


        public void start(Stage primaryStage) {
            this.primaryStage = primaryStage;
            primaryStage.setTitle("Altruithm - Global Trust Infrastructure for Social Impact");

            BorderPane mainLayout = new BorderPane();
            mainLayout.getStyleClass().add("main-layout");

            VBox sidebar = createVerticalSidebar();
            mainLayout.setLeft(sidebar);

            contentArea = new StackPane();
            contentArea.getStyleClass().add("content-area");

            showOverviewContent();
            mainLayout.setCenter(contentArea);

            HBox statusBar = createStatusBar();
            mainLayout.setBottom(statusBar);

            Scene scene = new Scene(mainLayout, 1400, 900);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            primaryStage.setScene(scene);

            // ADD THESE LINES TO ENABLE FULLSCREEN:
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("Press ESC to exit fullscreen");
            // Optional: Disable the ESC key exit if you want to keep it locked in fullscreen
            // primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            primaryStage.show();

            startAnimations();
        }

        private VBox createVerticalSidebar() {
            VBox sidebar = new VBox();
            sidebar.getStyleClass().add("vertical-sidebar");
            sidebar.setPrefWidth(280);
            sidebar.setMinWidth(280);
            sidebar.setMaxWidth(280);

            VBox brandSection = new VBox(8);
            brandSection.getStyleClass().add("brand-section");
            brandSection.setAlignment(Pos.CENTER);
            brandSection.setPadding(new Insets(30, 20, 30, 20));

            Label logoIcon = new Label("◈");
            logoIcon.getStyleClass().add("logo-icon");
            logoIcon.setFont(Font.font("Arial", FontWeight.BOLD, 42));

            Label brandLabel = new Label("ALTRUITHM");
            brandLabel.getStyleClass().add("brand-label");
            brandLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

            Label brandSubtitle = new Label("Trust Infrastructure");
            brandSubtitle.getStyleClass().add("brand-subtitle");
            brandSubtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

            brandSection.getChildren().addAll(logoIcon, brandLabel, brandSubtitle);

            VBox metricsSection = createSidebarMetrics();

            VBox navSection = new VBox(8);
            navSection.getStyleClass().add("nav-section");
            navSection.setPadding(new Insets(20, 15, 20, 15));

            Label navTitle = new Label("NAVIGATION");
            navTitle.getStyleClass().add("nav-title");
            navTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));

            Button overviewBtn = createNavButton("", "Global Overview", true);
            Button projectsBtn = createNavButton("", "Project Monitoring", false);
            Button fraudBtn = createNavButton("", "Fraud Detection", false);
            Button recommenderBtn = createNavButton("", "Charity Recommender", false);

            overviewBtn.setOnAction(e -> {
                setActiveButton(overviewBtn, projectsBtn, fraudBtn, recommenderBtn);
                showOverviewContent();
            });
            projectsBtn.setOnAction(e -> {
                setActiveButton(projectsBtn, overviewBtn, fraudBtn, recommenderBtn);
                showProjectsContent();
            });
            fraudBtn.setOnAction(e -> {
                setActiveButton(fraudBtn, overviewBtn, projectsBtn, recommenderBtn);
                showFraudDetectionContent();
            });
            recommenderBtn.setOnAction(e -> {
                setActiveButton(recommenderBtn, overviewBtn, projectsBtn, fraudBtn);
                showCharityRecommenderContent();
            });

            navSection.getChildren().addAll(navTitle, overviewBtn, projectsBtn, fraudBtn, recommenderBtn);

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            VBox systemInfo = createSystemInfo();

            sidebar.getChildren().addAll(brandSection, metricsSection, navSection, spacer, systemInfo);
            return sidebar;
        }

        private VBox createSidebarMetrics() {
            VBox metricsSection = new VBox(10);
            metricsSection.getStyleClass().add("sidebar-metrics");
            metricsSection.setPadding(new Insets(15, 15, 20, 15));

            Label metricsTitle = new Label("KEY METRICS");
            metricsTitle.getStyleClass().add("nav-title");
            metricsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));

            VBox metric1 = createCompactMetric("₹70,322 Cr", "Total Funding", "metric-green");
            VBox metric2 = createCompactMetric("94.7%", "Impact Rate", "metric-red");
            VBox metric3 = createCompactMetric("₹8,783 Cr", "Fraud Prevented", "metric-orange");
            VBox metric4 = createCompactMetric("2,811", "Active Projects", "metric-purple");

            metricsSection.getChildren().addAll(metricsTitle, metric1, metric2, metric3, metric4);
            return metricsSection;
        }

        private VBox createCompactMetric(String value, String label, String colorClass) {
            VBox metric = new VBox(3);
            metric.getStyleClass().add("compact-metric");
            metric.setPadding(new Insets(10, 12, 10, 12));

            Label valueLabel = new Label(value);
            valueLabel.getStyleClass().addAll("compact-value", colorClass);
            valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

            Label labelLabel = new Label(label);
            labelLabel.getStyleClass().add("compact-label");
            labelLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));

            metric.getChildren().addAll(valueLabel, labelLabel);
            return metric;
        }

        private Button createNavButton(String icon, String text, boolean active) {
            Button button = new Button();
            button.getStyleClass().add("nav-button");
            if (active) {
                button.getStyleClass().add("nav-button-active");
            }

            HBox content = new HBox(12);
            content.setAlignment(Pos.CENTER_LEFT);

            Label iconLabel = new Label(icon);
            iconLabel.getStyleClass().add("nav-icon");
            iconLabel.setFont(Font.font(18));

            Label textLabel = new Label(text);
            textLabel.getStyleClass().add("nav-text");
            textLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));

            content.getChildren().addAll(iconLabel, textLabel);
            button.setGraphic(content);
            button.setMaxWidth(Double.MAX_VALUE);

            return button;
        }

        private void setActiveButton(Button active, Button... others) {
            active.getStyleClass().add("nav-button-active");
            for (Button btn : others) {
                btn.getStyleClass().remove("nav-button-active");
            }
        }

        private VBox createSystemInfo() {
            VBox systemInfo = new VBox(8);
            systemInfo.getStyleClass().add("system-info");
            systemInfo.setPadding(new Insets(15));
            systemInfo.setAlignment(Pos.CENTER);

            Label statusDot = new Label("●");
            statusDot.getStyleClass().addAll("status-dot", "pulse-animation");
            statusDot.setFont(Font.font(12));

            Label statusText = new Label("All Systems Operational");
            statusText.getStyleClass().add("status-info-text");
            statusText.setFont(Font.font("Arial", FontWeight.NORMAL, 11));

            Button settingsBtn = new Button("⚙");
            settingsBtn.getStyleClass().add("icon-button");
            settingsBtn.setFont(Font.font(18));

            Button exitBtn = new Button("⎋");
            exitBtn.getStyleClass().add("icon-button");
            exitBtn.setFont(Font.font(18));
            exitBtn.setOnAction(e -> Platform.exit());

            HBox buttonRow = new HBox(10);
            buttonRow.setAlignment(Pos.CENTER);
            buttonRow.getChildren().addAll(settingsBtn, exitBtn);

            systemInfo.getChildren().addAll(statusDot, statusText, buttonRow);
            return systemInfo;
        }

        private void showOverviewContent() {
            ScrollPane content = createOverviewContent();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
        }

        private void showProjectsContent() {
            ScrollPane content = createProjectsContent();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
        }

        private void showFraudDetectionContent() {
            ScrollPane content = createFraudDetectionContent();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
        }

        private void showCharityRecommenderContent() {
            ScrollPane content = createCharityRecommenderContent();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
        }

        private ScrollPane createOverviewContent() {
            VBox content = new VBox(25);
            content.setPadding(new Insets(30));
            content.getStyleClass().add("tab-content");

            VBox header = createContentHeader("🌍 Global Overview", "Real-time monitoring of global funding distribution");

            VBox mapSection = createMapSection();

            HBox chartsRow = new HBox(20);
            chartsRow.setAlignment(Pos.CENTER);

            fundingDistributionChart = createEnhancedPieChart();
            VBox pieChartContainer = createChartContainer("Funding by Category", fundingDistributionChart);

            chartsRow.getChildren().addAll(pieChartContainer);
            content.getChildren().addAll(header, mapSection, chartsRow);

            ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.getStyleClass().add("scroll-pane");
            scrollPane.setFitToWidth(true);
            return scrollPane;
        }
    
        private VBox createContentHeader(String title, String subtitle) {
            VBox header = new VBox(5);
            header.setPadding(new Insets(0, 0, 10, 0));
    
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("content-title");
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
    
            Label subtitleLabel = new Label(subtitle);
            subtitleLabel.getStyleClass().add("content-subtitle");
            subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
    
            header.getChildren().addAll(titleLabel, subtitleLabel);
            return header;
        }

        private VBox createMapSection() {
            VBox mapSection = new VBox(12);
            mapSection.getStyleClass().add("card-container");

            // Create WebView
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webView.setPrefHeight(400);
            webView.setPrefWidth(800);

            // CRITICAL: Enable JavaScript
            webEngine.setJavaScriptEnabled(true);

            // Add error handling
            webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldExc, newExc) -> {
                if (newExc != null) {
                    System.err.println("WebEngine error: " + newExc.getMessage());
                    newExc.printStackTrace();
                }
            });

            // Monitor loading state
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                System.out.println("WebView state: " + newState);
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    System.out.println("Map loaded successfully!");
                } else if (newState == javafx.concurrent.Worker.State.FAILED) {
                    System.out.println("Map failed to load!");
                }
            });

            // Simplified HTML with embedded map
            String mapHTML = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                * { margin: 0; padding: 0; }
                html, body { height: 100%; width: 100%; }
                #map { height: 100%; width: 100%; }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script>
                try {
                    console.log('Initializing map...');
                    
                    var map = L.map('map', {
                        preferCanvas: true,
                        tap: false,
                        dragging: true,
                        touchZoom: true,
                        scrollWheelZoom: true
                    }).setView([20.5937, 78.9629], 5);
                    
                    console.log('Adding tile layer...');
                    
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 19,
                        attribution: '© OpenStreetMap'
                    }).addTo(map);
                    
                    console.log('Adding markers...');
                    
                    L.marker([28.6139, 77.2090]).addTo(map)
                        .bindPopup('<b>Clean Water Initiative</b><br>₹2.5 Cr');
                    L.marker([19.0760, 72.8777]).addTo(map)
                        .bindPopup('<b>Solar Schools</b><br>₹1.8 Cr');
                    L.marker([13.0827, 80.2707]).addTo(map)
                        .bindPopup('<b>Maternal Health</b><br>₹3.8 Cr');
                    L.marker([22.5726, 88.3639]).addTo(map)
                        .bindPopup('<b>Digital Literacy</b><br>₹2.2 Cr');
                    
                    setTimeout(function() {
                        map.invalidateSize();
                        console.log('Map ready!');
                    }, 250);
                    
                } catch(e) {
                    console.error('Map error: ' + e);
                    document.body.innerHTML = '<h2 style="color:red;padding:20px;">Error loading map: ' + e.message + '</h2>';
                }
            </script>
        </body>
        </html>
        """;

            webEngine.loadContent(mapHTML, "text/html");

            mapSection.getChildren().add(webView);
            return mapSection;
        }






        private ScrollPane createProjectsContent() {
            VBox content = new VBox(25);
            content.setPadding(new Insets(30));
            content.getStyleClass().add("tab-content");
    
            VBox header = createContentHeader("📊 Project Monitoring", "Track and analyze all active funding projects");
    
            HBox filterRow = createFilterRow();
            projectTable = createEnhancedProjectTable();
    
            content.getChildren().addAll(header, filterRow, projectTable);
    
            ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.getStyleClass().add("scroll-pane");
            scrollPane.setFitToWidth(true);
            return scrollPane;
        }

        private HBox createFilterRow() {
            HBox filterRow = new HBox(15);
            filterRow.getStyleClass().add("filter-container");
            filterRow.setAlignment(Pos.CENTER_LEFT);
            filterRow.setPadding(new Insets(15));
    
            TextField searchField = new TextField();
            searchField.setPromptText("🔍 Search projects...");
            searchField.getStyleClass().add("search-field");
            searchField.setPrefWidth(300);
    
            ComboBox<String> categoryFilter = new ComboBox<>(FXCollections.observableArrayList(
                    "All Categories", "Climate Action", "Disaster Relief", "Education", "Healthcare", "Poverty Alleviation"));
            categoryFilter.setValue("All Categories");
            categoryFilter.getStyleClass().add("filter-combo");
    
            ComboBox<String> statusFilter = new ComboBox<>(FXCollections.observableArrayList(
                    "All Status", "Active", "Completed", "Under Review", "Flagged"));
            statusFilter.setValue("All Status");
            statusFilter.getStyleClass().add("filter-combo");
    
            Button refreshButton = new Button("🔄 Refresh Data");
            refreshButton.getStyleClass().addAll("refresh-button", "button-hover");
            addButtonHoverEffect(refreshButton);
    
            filterRow.getChildren().addAll(searchField, categoryFilter, statusFilter, refreshButton);
            return filterRow;
        }
    
        private ScrollPane createFraudDetectionContent() {
            VBox content = new VBox(25);
            content.setPadding(new Insets(30));
            content.getStyleClass().add("tab-content");
    
            VBox header = createContentHeader("🛡 Fraud Detection", "AI-powered fraud detection and risk analysis");
    
            VBox form = new VBox(15.0);
            form.getStyleClass().add("card-container");
            form.setPadding(new Insets(25.0));
            form.setMaxWidth(600);
    
            Label formTitle = new Label("🔍 Check Charity Fraud Risk");
            formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20.0));
            formTitle.setTextFill(Color.WHITE);
    
            TextField charityField = new TextField();
            charityField.setPromptText("e.g., American Red Cross");
            charityField.setStyle("-fx-background-color: #252b47; -fx-text-fill: white; -fx-prompt-text-fill: #6677aa;");
    
            TextField amountField = new TextField();
            amountField.setPromptText("e.g., 1000");
            amountField.setStyle("-fx-background-color: #252b47; -fx-text-fill: white; -fx-prompt-text-fill: #6677aa;");
    
            Label charityLabel = new Label("Charity Name:");
            charityLabel.setTextFill(Color.web("#b0b8d0"));
            Label amountLabel = new Label("Donation Amount (₹):");
            amountLabel.setTextFill(Color.web("#b0b8d0"));
    
            Button checkButton = new Button("🔍 Check Risk");
            checkButton.setStyle("-fx-background-color: #7289da; -fx-text-fill: white; -fx-padding: 10 20;");
    
            Button clearButton = new Button("Clear");
            clearButton.setStyle("-fx-background-color: #2a3557; -fx-text-fill: white; -fx-padding: 10 20;");
    
            ProgressIndicator loadingIndicator = new ProgressIndicator();
            loadingIndicator.setVisible(false);
            loadingIndicator.setPrefSize(30, 30);
    
            HBox buttonBox = new HBox(15, checkButton, clearButton, loadingIndicator);
            buttonBox.setAlignment(Pos.CENTER_LEFT);
    
            VBox resultsBox = new VBox(15.0);
            resultsBox.setStyle("-fx-background-color: #252b47; -fx-background-radius: 10; -fx-padding: 20;");
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
                    if (amount <= 0) {
                        showAlert("Error", "Amount must be greater than 0");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Error", "Invalid amount");
                    return;
                }
    
                loadingIndicator.setVisible(true);
                checkButton.setDisable(true);
    
				// Disconnect API/model: simple rule-based risk for demo
				resultsBox.setVisible(true);
				String riskLevel;
                int riskS=0;
				if (charityName.equalsIgnoreCase("Trump Foundation") || amount > 10000) {
					riskLevel = "HIGH";
                    riskS=7;
					riskLevelLabel.setTextFill(Color.web("#ff4444"));
				} else if (charityName.equalsIgnoreCase("American Red Cross") || amount > 1000) {
					riskLevel = "MEDIUM";
                    riskS=5;
					riskLevelLabel.setTextFill(Color.web("#ffaa00"));
				} else {
					riskLevel = "LOW";
                    riskS=3;
					riskLevelLabel.setTextFill(Color.web("#00ff88"));
				}
				riskLevelLabel.setText("Risk Level: " + riskLevel);
				riskScoreLabel.setText("Risk Score: " + riskS + " on a Scale of 10");
				recommendationLabel.setText("Recommendation: This is a Basic Model Based on MCDM Decision Making Follow at your own risk");
				warningsArea.setText("");
				loadingIndicator.setVisible(false);
				checkButton.setDisable(false);
            });
    
            form.getChildren().addAll(
                    formTitle,
                    charityLabel, charityField,
                    amountLabel, amountField,
                    buttonBox,
                    resultsBox
            );
    
            content.getChildren().addAll(header, form);
    
            ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.getStyleClass().add("scroll-pane");
            scrollPane.setFitToWidth(true);
            return scrollPane;
        }
    
        private ScrollPane createCharityRecommenderContent() {
            VBox content = new VBox(25);
            content.setPadding(new Insets(30));
            content.getStyleClass().add("tab-content");
    
            VBox header = createContentHeader("💡 Charity Recommender", "Find charities similar to ones you trust or based on your interests");
    
            HBox modeToggle = new HBox(15);
            modeToggle.setAlignment(Pos.CENTER_LEFT);
            modeToggle.setPadding(new Insets(10, 0, 10, 0));
    
            ToggleGroup modeGroup = new ToggleGroup();
    
            RadioButton charityModeBtn = new RadioButton("Find Similar Charities");
            charityModeBtn.setToggleGroup(modeGroup);
            charityModeBtn.setSelected(true);
            charityModeBtn.setTextFill(Color.WHITE);
            charityModeBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    
            RadioButton interestModeBtn = new RadioButton("Recommend by Interests");
            interestModeBtn.setToggleGroup(modeGroup);
            interestModeBtn.setTextFill(Color.WHITE);
            interestModeBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    
            modeToggle.getChildren().addAll(charityModeBtn, interestModeBtn);
    
            VBox formContainer = new VBox(20);
            formContainer.getStyleClass().add("card-container");
            formContainer.setPadding(new Insets(25));
            formContainer.setMaxWidth(700);
    
            VBox charityForm = new VBox(15);
    
            Label charityFormTitle = new Label("🔍 Find Similar Charities");
            charityFormTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            charityFormTitle.setTextFill(Color.WHITE);
    
            Label charityLabel = new Label("Enter Charity Name:");
            charityLabel.setTextFill(Color.web("#b0b8d0"));
            charityLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
    
            TextField charityNameField = new TextField();
            charityNameField.setPromptText("e.g., Oxfam, Doctors Without Borders, WWF");
            charityNameField.setStyle("-fx-background-color: #252b47; -fx-text-fill: white; -fx-prompt-text-fill: #6677aa; -fx-padding: 10;");
            charityNameField.setFont(Font.font("Arial", 14));
    
            VBox interestForm = new VBox(10);
            interestForm.setVisible(false);
    
            Label interestFormTitle = new Label("🎯 Recommend by Interests");
            interestFormTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            interestFormTitle.setTextFill(Color.WHITE);
    
            Label interestLabel = new Label("Enter Your Interests (comma-separated):");
            interestLabel.setTextFill(Color.web("#b0b8d0"));
            interestLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
    
            TextArea interestsField = new TextArea();
            interestsField.setPromptText("e.g., climate change, education, healthcare, animal welfare");
            interestsField.setPrefRowCount(3);
            interestsField.setWrapText(true);
            interestsField.setStyle("-fx-control-inner-background: #252b47; -fx-text-fill: white; -fx-prompt-text-fill: #6677aa;");
            interestsField.setFont(Font.font("Arial", 14));
    
            ProgressIndicator loadingIndicator = new ProgressIndicator();
            loadingIndicator.setVisible(false);
            loadingIndicator.setPrefSize(30, 30);
    
            Button recommendButton = new Button("🚀 Get Recommendations");
            recommendButton.setStyle("-fx-background-color: #7289da; -fx-text-fill: white; -fx-padding: 12 24; -fx-font-size: 14;");
            addButtonHoverEffect(recommendButton);
    
            Button clearButton = new Button("Clear");
            clearButton.setStyle("-fx-background-color: #2a3557; -fx-text-fill: white; -fx-padding: 12 24; -fx-font-size: 14;");
            addButtonHoverEffect(clearButton);
    
            HBox buttonBox = new HBox(15, recommendButton, clearButton, loadingIndicator);
            buttonBox.setAlignment(Pos.CENTER_LEFT);
    
            VBox resultsContainer = new VBox(15);
            resultsContainer.setVisible(false);
            resultsContainer.setStyle("-fx-background-color: #252b47; -fx-background-radius: 10; -fx-padding: 20;");
    
            Label resultsTitle = new Label("Recommended Charities");
            resultsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            resultsTitle.setTextFill(Color.WHITE);
    
            VBox charitiesList = new VBox(12);
    
            resultsContainer.getChildren().addAll(resultsTitle, charitiesList);
    
            charityModeBtn.setOnAction(e -> {
                charityForm.setVisible(true);
                interestForm.setVisible(false);
                resultsContainer.setVisible(false);
            });
    
            interestModeBtn.setOnAction(e -> {
                charityForm.setVisible(false);
                interestForm.setVisible(true);
                resultsContainer.setVisible(false);
            });
    
            clearButton.setOnAction(e -> {
                charityNameField.clear();
                interestsField.clear();
                resultsContainer.setVisible(false);
            });
    
            recommendButton.setOnAction(e -> {
                boolean isCharityMode = charityModeBtn.isSelected();
                String input = isCharityMode ? charityNameField.getText().trim() : interestsField.getText().trim();

                if (input.isEmpty()) {
                    showAlert("Error", "Please enter " + (isCharityMode ? "a charity name" : "your interests"));
                    return;
                }

                loadingIndicator.setVisible(true);
                recommendButton.setDisable(true);

                Task<CharityRecommendationResponse> recommendTask = new Task<CharityRecommendationResponse>() {
                    @Override
                    protected CharityRecommendationResponse call() throws Exception {
                        com.example.demo1.service.CharityRecommenderService service = new com.example.demo1.service.CharityRecommenderService();
                        if (isCharityMode) {
                            java.util.List<com.example.demo1.service.CharityRecommenderService.ScoredCharity> scored = service.getSimilarCharities(input, 10);
                            java.util.List<CharityRecommendation> items = new java.util.ArrayList<>();
                            for (com.example.demo1.service.CharityRecommenderService.ScoredCharity sc : scored) {
                                com.example.demo1.model.Charity c = sc.charity();
                                items.add(new CharityRecommendation(
                                        c.getName(), c.getCategory(), c.getDescription(),
                                        sc.score() * 100.0, 0.0, 0
                                ));
                            }
                            return new CharityRecommendationResponse(items);
                        } else {
                            java.util.List<com.example.demo1.model.Charity> matches = service.getCharitiesByInterests(input);
                            java.util.List<CharityRecommendation> items = new java.util.ArrayList<>();
                            for (com.example.demo1.model.Charity c : matches) {
                                items.add(new CharityRecommendation(c.getName(), c.getCategory(), c.getDescription(), 0.0, 0.0, 0));
                            }
                            return new CharityRecommendationResponse(items);
                        }
                    }
                };

                recommendTask.setOnSucceeded(event -> {
                    CharityRecommendationResponse response = recommendTask.getValue();
                    charitiesList.getChildren().clear();

                    if (response.getCharities() != null && !response.getCharities().isEmpty()) {
                        for (CharityRecommendation charity : response.getCharities()) {
                            VBox charityCard = createCharityCard(charity);
                            charitiesList.getChildren().add(charityCard);
                        }
                        resultsContainer.setVisible(true);
                    } else {
                        showAlert("No Results", "No charities found. Please try different search terms.");
                    }

                    loadingIndicator.setVisible(false);
                    recommendButton.setDisable(false);
                });

                recommendTask.setOnFailed(event -> {
                    showAlert("Error", "Failed to get recommendations: " + recommendTask.getException().getMessage());
                    loadingIndicator.setVisible(false);
                    recommendButton.setDisable(false);
                });

                new Thread(recommendTask).start();
            });
    
            charityForm.getChildren().addAll(charityFormTitle, charityLabel, charityNameField);
            interestForm.getChildren().addAll(interestFormTitle, interestLabel, interestsField);
    
            formContainer.getChildren().addAll(charityForm, interestForm, buttonBox, resultsContainer);
    
            content.getChildren().addAll(header, modeToggle, formContainer);
    
            ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.getStyleClass().add("scroll-pane");
            scrollPane.setFitToWidth(true);
            return scrollPane;
        }
    
        private VBox createCharityCard(CharityRecommendation charity) {
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: #1a1f3a; -fx-background-radius: 8; -fx-padding: 15; -fx-border-color: #7289da; -fx-border-width: 1; -fx-border-radius: 8;");
            addFixedHoverAnimation(card);
    
            Label nameLabel = new Label(charity.getName());
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            nameLabel.setTextFill(Color.WHITE);
    
            Label categoryLabel = new Label("Category: " + charity.getCategory());
            categoryLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            categoryLabel.setTextFill(Color.web("#b0b8d0"));
    
            Label descLabel = new Label(charity.getDescription());
            descLabel.setWrapText(true);
            descLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            descLabel.setTextFill(Color.web("#d0d8e8"));
    
            HBox metricsBox = new HBox(20);
            metricsBox.setAlignment(Pos.CENTER_LEFT);
    
            Label matchScoreLabel = new Label(String.format("Match: %.0f%%", charity.getMatchScore()));
            matchScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            matchScoreLabel.setTextFill(Color.web("#00ff88"));
    
            Label impactLabel = new Label("Impact: " + charity.getImpactRating() + "/5");
            impactLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            impactLabel.setTextFill(Color.web("#ffaa00"));
    
            Label trustLabel = new Label("Trust: " + charity.getTrustScore() + "%");
            trustLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            trustLabel.setTextFill(Color.web("#7289da"));
    
            metricsBox.getChildren().addAll(matchScoreLabel, impactLabel, trustLabel);
    
            Button learnMoreBtn = new Button("🔗 Learn More");
            learnMoreBtn.setStyle("-fx-background-color: #7289da; -fx-text-fill: white; -fx-padding: 8 16;");
            addButtonHoverEffect(learnMoreBtn);
    
            card.getChildren().addAll(nameLabel, categoryLabel, descLabel, metricsBox, learnMoreBtn);
            return card;
        }
    
        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    
        private TableView<FundingProject> createEnhancedProjectTable() {
            TableView<FundingProject> table = new TableView<>();
            table.getStyleClass().add("enhanced-table");
            table.setPrefHeight(400);
    
            TableColumn<FundingProject, String> nameCol = createTableColumn("Project Name", "name", 230);
            TableColumn<FundingProject, String> categoryCol = createTableColumn("Category", "category", 130);
            TableColumn<FundingProject, String> fundingCol = createTableColumn("Funding", "funding", 110);
            TableColumn<FundingProject, String> impactCol = createTableColumn("Impact Score", "impactScore", 110);
            TableColumn<FundingProject, String> statusCol = createTableColumn("Status", "status", 110);
            TableColumn<FundingProject, String> riskCol = createTableColumn("Risk Level", "riskLevel", 110);
    
            table.getColumns().addAll(nameCol, categoryCol, fundingCol, impactCol, statusCol, riskCol);
    
            ObservableList<FundingProject> data = FXCollections.observableArrayList(
                    new FundingProject("Clean Water Initiative", "Healthcare", "₹2.5 Cr", "94%", "Active", "Low"),
                    new FundingProject("Solar Schools Program", "Education", "₹1.8 Cr", "87%", "Active", "Low"),
                    new FundingProject("Disaster Relief Fund", "Emergency", "₹5.2 Cr", "91%", "Active", "Medium"),
                    new FundingProject("Reforestation Project", "Climate", "₹3.1 Cr", "89%", "Completed", "Low"),
                    new FundingProject("Food Security Initiative", "Poverty", "₹4.7 Cr", "76%", "Under Review", "High"),
                    new FundingProject("Digital Literacy Program", "Education", "₹2.2 Cr", "92%", "Active", "Low"),
                    new FundingProject("Maternal Health Initiative", "Healthcare", "₹3.8 Cr", "88%", "Active", "Medium")
            );
    
            table.setItems(data);
            return table;
        }
    
        private TableColumn<FundingProject, String> createTableColumn(String text, String property, double width) {
            TableColumn<FundingProject, String> column = new TableColumn<>(text);
            column.setCellValueFactory(new PropertyValueFactory<>(property));
            column.setPrefWidth(width);
            column.getStyleClass().add("table-column");
            return column;
        }
    
        private VBox createChartContainer(String title, Node chart) {
            VBox container = new VBox(12);
            container.getStyleClass().addAll("chart-container", "card-hover-fixed");
            container.setPadding(new Insets(20));
    
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("chart-title");
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    
            addFixedHoverAnimation(container);
    
            container.getChildren().addAll(titleLabel, chart);
            return container;
        }
    
        private PieChart createEnhancedPieChart() {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Climate Action", 28),
                    new PieChart.Data("Healthcare", 22),
                    new PieChart.Data("Education", 18),
                    new PieChart.Data("Disaster Relief", 16),
                    new PieChart.Data("Poverty Alleviation", 16)
            );
    
            PieChart chart = new PieChart(pieChartData);
            chart.getStyleClass().add("enhanced-pie-chart");
            chart.setPrefSize(400, 300);
            chart.setLegendVisible(true);
            return chart;
        }
    
        private BarChart<String, Number> createEnhancedBarChart() {
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.getStyleClass().add("chart-axis");
            yAxis.getStyleClass().add("chart-axis");
    
            BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
            chart.getStyleClass().add("enhanced-bar-chart");
    
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Impact Score");
            series.getData().addAll(
                    new XYChart.Data<>("North America", 92),
                    new XYChart.Data<>("Europe", 88),
                    new XYChart.Data<>("Asia", 85),
                    new XYChart.Data<>("Africa", 79),
                    new XYChart.Data<>("South America", 82)
            );
    
            chart.getData().add(series);
            chart.setPrefSize(400, 300);
            chart.setLegendVisible(false);
            return chart;
        }
    
        private HBox createStatusBar() {
            HBox statusBar = new HBox();
            statusBar.getStyleClass().add("status-bar");
            statusBar.setAlignment(Pos.CENTER_LEFT);
            statusBar.setPadding(new Insets(12));
    
            Label statusIndicator = new Label("●");
            statusIndicator.getStyleClass().addAll("status-indicator", "pulse-animation");
            statusIndicator.setFont(Font.font(14));
    
            Label statusLabel = new Label(" System Status: All services operational");
            statusLabel.getStyleClass().add("status-text");
            statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
    
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
    
            Label updateLabel = new Label("Last Updated: " + java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            updateLabel.getStyleClass().add("update-text");
            updateLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
    
            statusBar.getChildren().addAll(statusIndicator, statusLabel, spacer, updateLabel);
            return statusBar;
        }
    
        private void addFixedHoverAnimation(Node node) {
            DropShadow hoverShadow = new DropShadow(20, Color.rgb(114, 137, 218, 0.4));
    
            node.setOnMouseEntered(e -> {
                node.setEffect(hoverShadow);
                node.setScaleX(1.02);
                node.setScaleY(1.02);
            });
    
            node.setOnMouseExited(e -> {
                node.setEffect(null);
                node.setScaleX(1.0);
                node.setScaleY(1.0);
            });
        }
    
        private void addButtonHoverEffect(Button button) {
            button.setOnMouseEntered(e -> button.setEffect(new Glow(0.3)));
            button.setOnMouseExited(e -> button.setEffect(null));
        }
    
        private void startAnimations() {
            Timeline pulseTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, e -> {}),
                    new KeyFrame(Duration.millis(1000), e -> {})
            );
            pulseTimeline.setCycleCount(Timeline.INDEFINITE);
            pulseTimeline.play();
        }
    
        public static void main(String[] args) {
            launch(args);
        }
    
        // Data Classes
        public static class FundingProject {
            private String name;
            private String category;
            private String funding;
            private String impactScore;
            private String status;
            private String riskLevel;
    
            public FundingProject(String name, String category, String funding, String impactScore, String status, String riskLevel) {
                this.name = name;
                this.category = category;
                this.funding = funding;
                this.impactScore = impactScore;
                this.status = status;
                this.riskLevel = riskLevel;
            }
    
            public String getName() { return name; }
            public String getCategory() { return category; }
            public String getFunding() { return funding; }
            public String getImpactScore() { return impactScore; }
            public String getStatus() { return status; }
            public String getRiskLevel() { return riskLevel; }
    
            public void setName(String name) { this.name = name; }
            public void setCategory(String category) { this.category = category; }
            public void setFunding(String funding) { this.funding = funding; }
            public void setImpactScore(String impactScore) { this.impactScore = impactScore; }
            public void setStatus(String status) { this.status = status; }
            public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
        }
    
        public static class CharityRecommendation {
            private String name;
            private String category;
            private String description;
            private double matchScore;
            private double impactRating;
            private int trustScore;
    
            public CharityRecommendation(String name, String category, String description,
                                         double matchScore, double impactRating, int trustScore) {
                this.name = name;
                this.category = category;
                this.description = description;
                this.matchScore = matchScore;
                this.impactRating = impactRating;
                this.trustScore = trustScore;
            }
    
            public String getName() { return name; }
            public String getCategory() { return category; }
            public String getDescription() { return description; }
            public double getMatchScore() { return matchScore; }
            public double getImpactRating() { return impactRating; }
            public int getTrustScore() { return trustScore; }
        }
    
        public static class CharityRecommendationResponse {
            private java.util.List<CharityRecommendation> charities;
    
            public CharityRecommendationResponse(java.util.List<CharityRecommendation> charities) {
                this.charities = charities;
            }
    
            public java.util.List<CharityRecommendation> getCharities() { return charities; }
        }
    }