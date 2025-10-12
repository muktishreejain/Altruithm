package com.example.demo1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.animation.TranslateTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class AltruithmDashboard extends Application {

    private TableView<FundingProject> projectTable;
    private LineChart<String, Number> impactTrendChart;
    private PieChart fundingDistributionChart;
    private BarChart<String, Number> regionalImpactChart;
    private StackPane contentArea;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Altruithm - Global Trust Infrastructure for Social Impact");

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("main-layout");

        // Vertical sidebar
        VBox sidebar = createVerticalSidebar();
        mainLayout.setLeft(sidebar);

        // Main content area
        contentArea = new StackPane();
        contentArea.getStyleClass().add("content-area");

        // Show overview by default
        showOverviewContent();
        mainLayout.setCenter(contentArea);

        // Status bar
        HBox statusBar = createStatusBar();
        mainLayout.setBottom(statusBar);

        Scene scene = new Scene(mainLayout, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        startAnimations();
    }

    private VBox createVerticalSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("vertical-sidebar");
        sidebar.setPrefWidth(280);
        sidebar.setMinWidth(280);
        sidebar.setMaxWidth(280);

        // Logo/Brand section
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

        // Metrics section
        VBox metricsSection = createSidebarMetrics();

        // Navigation section
        VBox navSection = new VBox(8);
        navSection.getStyleClass().add("nav-section");
        navSection.setPadding(new Insets(20, 15, 20, 15));

        Label navTitle = new Label("NAVIGATION");
        navTitle.getStyleClass().add("nav-title");
        navTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));

        Button overviewBtn = createNavButton("", "Global Overview", true);
        Button projectsBtn = createNavButton("", "Project Monitoring", false);
        Button fraudBtn = createNavButton("", "Fraud Detection", false);
        Button analyticsBtn = createNavButton("", "Impact Analytics", false);

        overviewBtn.setOnAction(e -> {
            setActiveButton(overviewBtn, projectsBtn, fraudBtn, analyticsBtn);
            showOverviewContent();
        });
        projectsBtn.setOnAction(e -> {
            setActiveButton(projectsBtn, overviewBtn, fraudBtn, analyticsBtn);
            showProjectsContent();
        });
        fraudBtn.setOnAction(e -> {
            setActiveButton(fraudBtn, overviewBtn, projectsBtn, analyticsBtn);
            showFraudDetectionContent();
        });
        analyticsBtn.setOnAction(e -> {
            setActiveButton(analyticsBtn, overviewBtn, projectsBtn, fraudBtn);
            showAnalyticsContent();
        });

        navSection.getChildren().addAll(navTitle, overviewBtn, projectsBtn, fraudBtn, analyticsBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // System info at bottom
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
        VBox metric2 = createCompactMetric("94.7%", "Impact Rate", "metric-green");
        VBox metric3 = createCompactMetric("₹8,783 Cr", "Fraud Prevented", "metric-green");
        VBox metric4 = createCompactMetric("2,811", "Active Projects", "metric-green");

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

    private void showAnalyticsContent() {
        ScrollPane content = createAnalyticsContent();
        contentArea.getChildren().clear();
        contentArea.getChildren().add(content);
    }

    private ScrollPane createOverviewContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.getStyleClass().add("tab-content");

        // Header
        VBox header = createContentHeader("🌍 Global Overview", "Real-time monitoring of global funding distribution");

        // World map section
        VBox mapSection = createMapSection();

        // Charts row
        HBox chartsRow = new HBox(20);
        chartsRow.setAlignment(Pos.CENTER);

        fundingDistributionChart = createEnhancedPieChart();
        VBox pieChartContainer = createChartContainer("Funding by Category", fundingDistributionChart);

        regionalImpactChart = createEnhancedBarChart();
        VBox barChartContainer = createChartContainer("Impact by Region", regionalImpactChart);

        chartsRow.getChildren().addAll(pieChartContainer, barChartContainer);
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

        StackPane mapContainer = new StackPane();
        mapContainer.getStyleClass().add("map-container");
        mapContainer.setPrefHeight(350);

        Rectangle mapBackground = new Rectangle(800, 330);
        mapBackground.getStyleClass().add("map-background");

        VBox mapContent = new VBox(8);
        mapContent.setAlignment(Pos.CENTER);

        Label mapIcon = new Label("🗺️");
        mapIcon.setFont(Font.font(56));
        mapIcon.getStyleClass().add("floating-animation");

        Label mapLabel = new Label("Interactive World Map");
        mapLabel.getStyleClass().add("map-title");
        mapLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label mapSubLabel = new Label("Real-time funding flows by region");
        mapSubLabel.getStyleClass().add("map-subtitle");
        mapSubLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        mapContent.getChildren().addAll(mapIcon, mapLabel, mapSubLabel);
        mapContainer.getChildren().addAll(mapBackground, mapContent);
        mapSection.getChildren().add(mapContainer);

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

        VBox alertSection = createEnhancedAlertSection();
        impactTrendChart = createEnhancedLineChart();
        VBox trendChartContainer = createChartContainer("Fraud Risk Trends", impactTrendChart);

        content.getChildren().addAll(header, alertSection, trendChartContainer);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private ScrollPane createAnalyticsContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.getStyleClass().add("tab-content");

        VBox header = createContentHeader("📈 Impact Analytics", "Comprehensive analysis of social impact metrics");

        HBox roiSection = new HBox(20);
        roiSection.setAlignment(Pos.CENTER);

        VBox roiMetrics = new VBox(15);
        roiMetrics.getChildren().addAll(
                createAnalyticsCard("Overall Impact ROI", "327%", "analytics-green"),
                createAnalyticsCard("Avg. Project Efficiency", "84.2%", "analytics-blue"),
                createAnalyticsCard("Resource Optimization", "+23%", "analytics-orange")
        );

        LineChart<String, Number> impactChart = createDetailedImpactChart();
        VBox impactChartContainer = createChartContainer("Impact Trends Over Time", impactChart);

        roiSection.getChildren().addAll(roiMetrics, impactChartContainer);
        content.getChildren().addAll(header, roiSection);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox createAnalyticsCard(String title, String value, String colorClass) {
        VBox card = new VBox(8);
        card.getStyleClass().addAll("analytics-card", "card-hover-fixed");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(230);
        card.setPrefHeight(90);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("analytics-title");
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().addAll("analytics-value", colorClass);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        addFixedHoverAnimation(card);

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private VBox createEnhancedAlertSection() {
        VBox alertSection = new VBox(12);
        alertSection.getStyleClass().add("alert-section");

        HBox highAlert = createEnhancedAlert("HIGH", "Suspicious funding pattern detected in Project #4127", "alert-high");
        HBox mediumAlert = createEnhancedAlert("MEDIUM", "Unusual disbursement velocity in Climate Fund XY", "alert-medium");
        HBox lowAlert = createEnhancedAlert("LOW", "Verification pending for 3 new NGO registrations", "alert-low");

        alertSection.getChildren().addAll(highAlert, mediumAlert, lowAlert);
        return alertSection;
    }

    private HBox createEnhancedAlert(String level, String message, String alertClass) {
        HBox alert = new HBox(15);
        alert.getStyleClass().addAll("alert-container", alertClass, "card-hover-fixed");
        alert.setAlignment(Pos.CENTER_LEFT);
        alert.setPadding(new Insets(15));

        Label levelLabel = new Label(level);
        levelLabel.getStyleClass().add("alert-level");
        levelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));

        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("alert-message");
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Button actionButton = new Button("🔍 Investigate");
        actionButton.getStyleClass().addAll("alert-button", "button-hover");
        addButtonHoverEffect(actionButton);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        addFixedHoverAnimation(alert);

        alert.getChildren().addAll(levelLabel, messageLabel, spacer, actionButton);
        return alert;
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

    private LineChart<String, Number> createEnhancedLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.getStyleClass().add("chart-axis");
        yAxis.getStyleClass().add("chart-axis");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.getStyleClass().add("enhanced-line-chart");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Risk Score");
        series.getData().addAll(
                new XYChart.Data<>("Jan", 15),
                new XYChart.Data<>("Feb", 12),
                new XYChart.Data<>("Mar", 18),
                new XYChart.Data<>("Apr", 8),
                new XYChart.Data<>("May", 6),
                new XYChart.Data<>("Jun", 9)
        );

        chart.getData().add(series);
        chart.setPrefSize(600, 300);
        return chart;
    }

    private LineChart<String, Number> createDetailedImpactChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.getStyleClass().add("chart-axis");
        yAxis.getStyleClass().add("chart-axis");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.getStyleClass().add("enhanced-line-chart");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Verified Impact");
        series1.getData().addAll(
                new XYChart.Data<>("Q1 2024", 234),
                new XYChart.Data<>("Q2 2024", 287),
                new XYChart.Data<>("Q3 2024", 342),
                new XYChart.Data<>("Q4 2024", 398)
        );

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Projected Impact");
        series2.getData().addAll(
                new XYChart.Data<>("Q1 2024", 245),
                new XYChart.Data<>("Q2 2024", 295),
                new XYChart.Data<>("Q3 2024", 335),
                new XYChart.Data<>("Q4 2024", 385)
        );

        chart.getData().addAll(series1, series2);
        chart.setPrefSize(500, 300);
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
}