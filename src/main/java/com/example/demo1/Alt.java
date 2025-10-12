package com.example.demo1;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class Alt extends Application {

    private TableView<FundingProject> projectTable;
    private LineChart<String, Number> impactTrendChart;
    private PieChart fundingDistributionChart;
    private BarChart<String, Number> regionalImpactChart;


    public void start(Stage primaryStage) {
        primaryStage.setTitle("Altruithm - Global Trust Infrastructure for Social Impact");

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        VBox header = createHeader();
        mainLayout.setTop(header);

        // Main content area with tabs
        TabPane tabPane = createMainTabs();
        mainLayout.setCenter(tabPane);

        // Status bar
        HBox statusBar = createStatusBar();
        mainLayout.setBottom(statusBar);

        Scene scene = new Scene(mainLayout, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/dashboard-styles.css") != null ?
                getClass().getResource("/dashboard-styles.css").toExternalForm() :
                "data:text/css," + getInlineCSS());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #3498db); -fx-padding: 20;");

        // Title
        Label titleLabel = new Label("ALTRUITHM");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Global Trust Infrastructure for Social Impact Funding");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);

        // Key metrics row
        HBox metricsRow = createMetricsRow();

        header.getChildren().addAll(titleLabel, subtitleLabel, metricsRow);
        header.setAlignment(Pos.CENTER);
        header.setSpacing(10);

        return header;
    }

    private HBox createMetricsRow() {
        HBox metricsRow = new HBox(30);
        metricsRow.setAlignment(Pos.CENTER);
        metricsRow.setPadding(new Insets(20, 0, 0, 0));

        // Key metrics cards
        VBox totalFunding = createMetricCard("$847.2B", "Total Funding Tracked", "#27ae60");
        VBox verifiedImpact = createMetricCard("94.7%", "Verified Impact Rate", "#e74c3c");
        VBox fraudPrevented = createMetricCard("$12.3B", "Fraud Prevented", "#f39c12");
        VBox activeProjects = createMetricCard("15,847", "Active Projects", "#9b59b6");

        metricsRow.getChildren().addAll(totalFunding, verifiedImpact, fraudPrevented, activeProjects);
        return metricsRow;
    }

    private VBox createMetricCard(String value, String label, String color) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 10; -fx-padding: 15;");
        card.setMinWidth(150);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.setTextFill(Color.web(color));

        Label labelLabel = new Label(label);
        labelLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        labelLabel.setTextFill(Color.WHITE);
        labelLabel.setWrapText(true);
        labelLabel.setAlignment(Pos.CENTER);

        card.getChildren().addAll(valueLabel, labelLabel);
        return card;
    }

    private TabPane createMainTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Overview Tab
        Tab overviewTab = new Tab("Global Overview");
        overviewTab.setContent(createOverviewContent());

        // Projects Tab
        Tab projectsTab = new Tab("Project Monitoring");
        projectsTab.setContent(createProjectsContent());

        // Fraud Detection Tab
        Tab fraudTab = new Tab("Fraud Detection");
        fraudTab.setContent(createFraudDetectionContent());

        // Impact Analytics Tab
        Tab analyticsTab = new Tab("Impact Analytics");
        analyticsTab.setContent(createAnalyticsContent());

        tabPane.getTabs().addAll(overviewTab, projectsTab, fraudTab, analyticsTab);
        return tabPane;
    }

    private ScrollPane createOverviewContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // World map section (placeholder)
        VBox mapSection = new VBox(10);
        Label mapTitle = new Label("Global Funding Distribution");
        mapTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Rectangle mapPlaceholder = new Rectangle(800, 400);
        mapPlaceholder.setFill(Color.LIGHTBLUE);
        mapPlaceholder.setStroke(Color.DARKBLUE);
        Label mapLabel = new Label("Interactive World Map\n(Real-time funding flows by region)");
        mapLabel.setAlignment(Pos.CENTER);

        StackPane mapContainer = new StackPane(mapPlaceholder, mapLabel);
        mapSection.getChildren().addAll(mapTitle, mapContainer);

        // Charts row
        HBox chartsRow = new HBox(20);

        // Funding distribution pie chart
        fundingDistributionChart = createFundingDistributionChart();
        VBox pieChartContainer = createChartContainer("Funding by Category", fundingDistributionChart);

        // Regional impact bar chart
        regionalImpactChart = createRegionalImpactChart();
        VBox barChartContainer = createChartContainer("Impact by Region", regionalImpactChart);

        chartsRow.getChildren().addAll(pieChartContainer, barChartContainer);

        content.getChildren().addAll(mapSection, chartsRow);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private ScrollPane createProjectsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Search and filters
        HBox filterRow = new HBox(15);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search projects...");
        searchField.setPrefWidth(300);

        ComboBox<String> categoryFilter = new ComboBox<>(FXCollections.observableArrayList(
                "All Categories", "Climate Action", "Disaster Relief", "Education", "Healthcare", "Poverty Alleviation"));
        categoryFilter.setValue("All Categories");

        ComboBox<String> statusFilter = new ComboBox<>(FXCollections.observableArrayList(
                "All Status", "Active", "Completed", "Under Review", "Flagged"));
        statusFilter.setValue("All Status");

        Button refreshButton = new Button("Refresh Data");
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        filterRow.getChildren().addAll(new Label("Search:"), searchField, new Label("Category:"),
                categoryFilter, new Label("Status:"), statusFilter, refreshButton);

        // Project table
        projectTable = createProjectTable();

        content.getChildren().addAll(filterRow, projectTable);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private ScrollPane createFraudDetectionContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Alert section
        VBox alertSection = createAlertSection();

        // Fraud risk chart
        impactTrendChart = createImpactTrendChart();
        VBox trendChartContainer = createChartContainer("Fraud Risk Trends", impactTrendChart);

        // Risk factors table
        TableView<RiskFactor> riskTable = createRiskFactorTable();

        content.getChildren().addAll(alertSection, trendChartContainer, riskTable);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private ScrollPane createAnalyticsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Impact ROI section
        HBox roiSection = new HBox(20);

        VBox roiMetrics = new VBox(10);
        roiMetrics.getChildren().addAll(
                createAnalyticsCard("Overall Impact ROI", "327%", "#27ae60"),
                createAnalyticsCard("Avg. Project Efficiency", "84.2%", "#3498db"),
                createAnalyticsCard("Resource Optimization", "+23%", "#f39c12")
        );

        // Impact trend chart
        LineChart<String, Number> impactChart = createDetailedImpactChart();
        VBox impactChartContainer = createChartContainer("Impact Trends Over Time", impactChart);

        roiSection.getChildren().addAll(roiMetrics, impactChartContainer);

        content.getChildren().add(roiSection);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox createAnalyticsCard(String title, String value, String color) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        card.setMinWidth(200);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        valueLabel.setTextFill(Color.web(color));

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private VBox createAlertSection() {
        VBox alertSection = new VBox(10);

        Label alertTitle = new Label("Fraud Detection Alerts");
        alertTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // High priority alert
        HBox highAlert = createAlert("HIGH", "Suspicious funding pattern detected in Project #4127", "#e74c3c");
        HBox mediumAlert = createAlert("MEDIUM", "Unusual disbursement velocity in Climate Fund XY", "#f39c12");
        HBox lowAlert = createAlert("LOW", "Verification pending for 3 new NGO registrations", "#95a5a6");

        alertSection.getChildren().addAll(alertTitle, highAlert, mediumAlert, lowAlert);
        return alertSection;
    }

    private HBox createAlert(String level, String message, String color) {
        HBox alert = new HBox(15);
        alert.setAlignment(Pos.CENTER_LEFT);
        alert.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 15; " +
                "-fx-border-color: " + color + "; -fx-border-width: 0 0 0 5;");

        Label levelLabel = new Label(level);
        levelLabel.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-background-radius: 3; -fx-padding: 5 10;");
        levelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Button actionButton = new Button("Investigate");
        actionButton.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");

        alert.getChildren().addAll(levelLabel, messageLabel, actionButton);
        HBox.setHgrow(messageLabel, Priority.ALWAYS);

        return alert;
    }

    private TableView<FundingProject> createProjectTable() {
        TableView<FundingProject> table = new TableView<>();
        table.setPrefHeight(400);

        TableColumn<FundingProject, String> nameCol = new TableColumn<>("Project Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<FundingProject, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(120);

        TableColumn<FundingProject, String> fundingCol = new TableColumn<>("Funding");
        fundingCol.setCellValueFactory(new PropertyValueFactory<>("funding"));
        fundingCol.setPrefWidth(100);

        TableColumn<FundingProject, String> impactCol = new TableColumn<>("Impact Score");
        impactCol.setCellValueFactory(new PropertyValueFactory<>("impactScore"));
        impactCol.setPrefWidth(100);

        TableColumn<FundingProject, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        TableColumn<FundingProject, String> riskCol = new TableColumn<>("Risk Level");
        riskCol.setCellValueFactory(new PropertyValueFactory<>("riskLevel"));
        riskCol.setPrefWidth(100);

        table.getColumns().addAll(nameCol, categoryCol, fundingCol, impactCol, statusCol, riskCol);

        // Sample data
        ObservableList<FundingProject> data = FXCollections.observableArrayList(
                new FundingProject("Clean Water Initiative", "Healthcare", "$2.5M", "94%", "Active", "Low"),
                new FundingProject("Solar Schools Program", "Education", "$1.8M", "87%", "Active", "Low"),
                new FundingProject("Disaster Relief Fund", "Emergency", "$5.2M", "91%", "Active", "Medium"),
                new FundingProject("Reforestation Project", "Climate", "$3.1M", "89%", "Completed", "Low"),
                new FundingProject("Food Security Initiative", "Poverty", "$4.7M", "76%", "Under Review", "High")
        );

        table.setItems(data);
        return table;
    }

    private TableView<RiskFactor> createRiskFactorTable() {
        TableView<RiskFactor> table = new TableView<>();
        table.setPrefHeight(300);

        TableColumn<RiskFactor, String> factorCol = new TableColumn<>("Risk Factor");
        factorCol.setCellValueFactory(new PropertyValueFactory<>("factor"));
        factorCol.setPrefWidth(300);

        TableColumn<RiskFactor, String> levelCol = new TableColumn<>("Risk Level");
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        levelCol.setPrefWidth(100);

        TableColumn<RiskFactor, String> projectsCol = new TableColumn<>("Affected Projects");
        projectsCol.setCellValueFactory(new PropertyValueFactory<>("affectedProjects"));
        projectsCol.setPrefWidth(150);

        table.getColumns().addAll(factorCol, levelCol, projectsCol);

        ObservableList<RiskFactor> riskData = FXCollections.observableArrayList(
                new RiskFactor("Unusual disbursement patterns", "High", "3"),
                new RiskFactor("Missing documentation", "Medium", "12"),
                new RiskFactor("Delayed impact reporting", "Medium", "8"),
                new RiskFactor("Geographic risk factors", "Low", "25"),
                new RiskFactor("New organization flags", "Low", "7")
        );

        table.setItems(riskData);
        return table;
    }

    private VBox createChartContainer(String title, Node chart) {
        VBox container = new VBox(10);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        container.getChildren().addAll(titleLabel, chart);
        return container;
    }

    private PieChart createFundingDistributionChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Climate Action", 28),
                new PieChart.Data("Healthcare", 22),
                new PieChart.Data("Education", 18),
                new PieChart.Data("Disaster Relief", 16),
                new PieChart.Data("Poverty Alleviation", 16)
        );

        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Funding Distribution by Category");
        chart.setPrefSize(400, 300);
        return chart;
    }

    private BarChart<String, Number> createRegionalImpactChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Region");
        yAxis.setLabel("Impact Score");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Regional Impact Effectiveness");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Impact Score");
        series.getData().add(new XYChart.Data<>("North America", 92));
        series.getData().add(new XYChart.Data<>("Europe", 88));
        series.getData().add(new XYChart.Data<>("Asia", 85));
        series.getData().add(new XYChart.Data<>("Africa", 79));
        series.getData().add(new XYChart.Data<>("South America", 82));

        chart.getData().add(series);
        chart.setPrefSize(400, 300);
        return chart;
    }

    private LineChart<String, Number> createImpactTrendChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Fraud Risk Score");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Monthly Fraud Risk Trends");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Risk Score");
        series.getData().add(new XYChart.Data<>("Jan", 15));
        series.getData().add(new XYChart.Data<>("Feb", 12));
        series.getData().add(new XYChart.Data<>("Mar", 18));
        series.getData().add(new XYChart.Data<>("Apr", 8));
        series.getData().add(new XYChart.Data<>("May", 6));
        series.getData().add(new XYChart.Data<>("Jun", 9));

        chart.getData().add(series);
        chart.setPrefSize(600, 300);
        return chart;
    }

    private LineChart<String, Number> createDetailedImpactChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Quarter");
        yAxis.setLabel("Impact Points (Millions)");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Quarterly Impact Delivery");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Verified Impact");
        series1.getData().add(new XYChart.Data<>("Q1 2024", 234));
        series1.getData().add(new XYChart.Data<>("Q2 2024", 287));
        series1.getData().add(new XYChart.Data<>("Q3 2024", 342));
        series1.getData().add(new XYChart.Data<>("Q4 2024", 398));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Projected Impact");
        series2.getData().add(new XYChart.Data<>("Q1 2024", 245));
        series2.getData().add(new XYChart.Data<>("Q2 2024", 295));
        series2.getData().add(new XYChart.Data<>("Q3 2024", 335));
        series2.getData().add(new XYChart.Data<>("Q4 2024", 385));

        chart.getData().addAll(series1, series2);
        chart.setPrefSize(500, 300);
        return chart;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setStyle("-fx-background-color: #34495e; -fx-padding: 10;");
        statusBar.setAlignment(Pos.CENTER_LEFT);

        Label statusLabel = new Label("System Status: All services operational");
        statusLabel.setTextFill(Color.LIGHTGREEN);
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label updateLabel = new Label("Last Updated: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        updateLabel.setTextFill(Color.LIGHTGRAY);
        updateLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        statusBar.getChildren().addAll(statusLabel, spacer, updateLabel);
        return statusBar;
    }

    private String getInlineCSS() {
        return """
                .tab-pane .tab-header-area .tab-header-background {
                    -fx-background-color: #ecf0f1;
                }
                .tab-pane .tab {
                    -fx-background-color: #bdc3c7;
                    -fx-background-radius: 5 5 0 0;
                }
                .tab-pane .tab:selected {
                    -fx-background-color: white;
                }
                .table-view {
                    -fx-background-color: white;
                }
                .table-view .column-header {
                    -fx-background-color: #3498db;
                    -fx-text-fill: white;
                }
                """;
    }

    // Data model classes
    public static class FundingProject {
        private String name, category, funding, impactScore, status, riskLevel;

        public FundingProject(String name, String category, String funding,
                              String impactScore, String status, String riskLevel) {
            this.name = name;
            this.category = category;
            this.funding = funding;
            this.impactScore = impactScore;
            this.status = status;
            this.riskLevel = riskLevel;
        }

        // Getters
        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getFunding() { return funding; }
        public String getImpactScore() { return impactScore; }
        public String getStatus() { return status; }
        public String getRiskLevel() { return riskLevel; }
    }

    public static class RiskFactor {
        private String factor, level, affectedProjects;

        public RiskFactor(String factor, String level, String affectedProjects) {
            this.factor = factor;
            this.level = level;
            this.affectedProjects = affectedProjects;
        }

        public String getFactor() { return factor; }
        public String getLevel() { return level; }
        public String getAffectedProjects() { return affectedProjects; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}