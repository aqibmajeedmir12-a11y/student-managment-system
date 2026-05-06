package com.student.ui;

import com.student.dao.CourseDAO;
import com.student.dao.StudentDAO;
import com.student.database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

import java.util.List;
import java.util.Map;

/**
 * Premium glassmorphism dashboard with real data, animated charts, and activity feed.
 */
public class DashboardView {

    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public DashboardView() {
        this.studentDAO = new StudentDAO();
        this.courseDAO = new CourseDAO();
    }

    public VBox getView() {
        VBox dashboard = new VBox(28);
        dashboard.setPadding(new Insets(36));
        dashboard.setStyle("-fx-background-color: #0F0A1A;");

        // Header
        VBox header = new VBox(6);
        Label title = AppTheme.createPageTitle("Dashboard");
        Label subtitle = AppTheme.createSubtitle("Real-time overview of your institution");
        header.getChildren().addAll(title, subtitle);
        AppTheme.slideUp(header, 400);

        // Stat Cards
        HBox statsRow = createStatCards();
        AppTheme.slideUp(statsRow, 500);

        // Charts Row
        HBox chartsRow = new HBox(20);
        VBox pieCard = createDepartmentChart();
        VBox barCard = createStatusChart();
        HBox.setHgrow(pieCard, Priority.ALWAYS);
        HBox.setHgrow(barCard, Priority.ALWAYS);
        chartsRow.getChildren().addAll(pieCard, barCard);
        AppTheme.slideUp(chartsRow, 600);

        // Bottom Row
        HBox bottomRow = new HBox(20);
        VBox activityCard = createActivityCard();
        VBox infoCard = createInfoCard();
        HBox.setHgrow(activityCard, Priority.ALWAYS);
        HBox.setHgrow(infoCard, Priority.ALWAYS);
        bottomRow.getChildren().addAll(activityCard, infoCard);
        AppTheme.slideUp(bottomRow, 700);

        dashboard.getChildren().addAll(header, statsRow, chartsRow, bottomRow);
        return dashboard;
    }

    private HBox createStatCards() {
        int totalStudents = studentDAO.getTotalCount();
        int activeStudents = studentDAO.getActiveCount();
        int totalCourses = courseDAO.getTotalCount();
        double avgGpa = studentDAO.getAverageGpa();

        HBox row = new HBox(16);
        row.getChildren().addAll(
            AppTheme.createStatCard("Total Students", String.valueOf(totalStudents), "🎓", "#7C3AED"),
            AppTheme.createStatCard("Active Students", String.valueOf(activeStudents), "✅", "#10B981"),
            AppTheme.createStatCard("Total Courses", String.valueOf(totalCourses), "📚", "#3B82F6"),
            AppTheme.createStatCard("Average GPA", String.format("%.2f", avgGpa), "⭐", "#F59E0B")
        );

        for (javafx.scene.Node node : row.getChildren()) {
            HBox.setHgrow(node, Priority.ALWAYS);
            if (node instanceof VBox vbox) vbox.setMaxWidth(Double.MAX_VALUE);
        }

        return row;
    }

    private VBox createDepartmentChart() {
        VBox card = AppTheme.createGlassCard();
        Label chartTitle = AppTheme.createSectionTitle("📊 Students by Department");

        Map<String, Integer> deptCounts = studentDAO.getStudentCountByDepartment();
        PieChart pieChart = new PieChart();

        if (deptCounts.isEmpty()) {
            Label empty = AppTheme.createSubtitle("No data yet. Add students to see distribution.");
            empty.setWrapText(true);
            card.getChildren().addAll(chartTitle, empty);
            return card;
        }

        for (Map.Entry<String, Integer> entry : deptCounts.entrySet()) {
            pieChart.getData().add(new PieChart.Data(
                    entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }

        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(false);
        pieChart.setPrefHeight(280);
        pieChart.setStyle("-fx-font-size: 11px; -fx-text-fill: #94A3B8;");

        card.getChildren().addAll(chartTitle, pieChart);
        return card;
    }

    private VBox createStatusChart() {
        VBox card = AppTheme.createGlassCard();
        Label chartTitle = AppTheme.createSectionTitle("📈 Enrollment Status");

        Map<String, Integer> statusCounts = studentDAO.getStudentCountByStatus();

        if (statusCounts.isEmpty()) {
            Label empty = AppTheme.createSubtitle("No data yet. Add students to see status overview.");
            empty.setWrapText(true);
            card.getChildren().addAll(chartTitle, empty);
            return card;
        }

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("");
        yAxis.setLabel("");
        xAxis.setStyle("-fx-tick-label-fill: #94A3B8;");
        yAxis.setStyle("-fx-tick-label-fill: #94A3B8;");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(280);
        barChart.setCategoryGap(30);
        barChart.setBarGap(5);
        barChart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(series);

        card.getChildren().addAll(chartTitle, barChart);
        return card;
    }

    private VBox createActivityCard() {
        VBox card = AppTheme.createGlassCard();
        Label title = AppTheme.createSectionTitle("📋 Recent Activity");

        VBox activityList = new VBox(8);
        List<String[]> activities = DatabaseManager.getInstance().getRecentActivity(6);

        if (activities.isEmpty()) {
            Label empty = AppTheme.createSubtitle("No activity yet. Start adding students and courses!");
            empty.setWrapText(true);
            activityList.getChildren().add(empty);
        } else {
            for (String[] activity : activities) {
                String emoji = switch (activity[0]) {
                    case "CREATE" -> "🟢";
                    case "UPDATE" -> "🔵";
                    case "DELETE" -> "🔴";
                    default -> "⚪";
                };
                String time = activity[3] != null ? activity[3].substring(11, 16) : "";
                activityList.getChildren().add(createActivityItem(emoji, activity[2], time));
            }
        }

        card.getChildren().addAll(title, activityList);
        return card;
    }

    private HBox createActivityItem(String dot, String text, String time) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 14, 10, 14));
        item.setStyle("""
            -fx-background-color: rgba(255,255,255,0.03);
            -fx-background-radius: 12;
        """);

        Label dotLabel = new Label(dot);
        dotLabel.setStyle("-fx-font-size: 14;");

        Label textLabel = new Label(text != null ? text : "System action");
        textLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #CBD5E1;");
        textLabel.setMaxWidth(250);
        textLabel.setWrapText(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #64748B;");

        item.getChildren().addAll(dotLabel, textLabel, spacer, timeLabel);

        item.setOnMouseEntered(e -> item.setStyle("""
            -fx-background-color: rgba(255,255,255,0.06);
            -fx-background-radius: 12;
        """));
        item.setOnMouseExited(e -> item.setStyle("""
            -fx-background-color: rgba(255,255,255,0.03);
            -fx-background-radius: 12;
        """));

        return item;
    }

    private VBox createInfoCard() {
        VBox card = AppTheme.createGlassCard();
        Label title = AppTheme.createSectionTitle("⚡ System Info");

        VBox infoList = new VBox(10);
        infoList.getChildren().addAll(
            createInfoRow("🔧 Engine", "JavaFX 17 + SQLite"),
            createInfoRow("☕ Java", System.getProperty("java.version")),
            createInfoRow("💻 OS", System.getProperty("os.name")),
            createInfoRow("📁 Arch", System.getProperty("os.arch")),
            createInfoRow("🏷 Version", "2.0.0 Enterprise")
        );

        card.getChildren().addAll(title, infoList);
        return card;
    }

    private HBox createInfoRow(String key, String value) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(8, 14, 8, 14));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("""
            -fx-background-color: rgba(255,255,255,0.03);
            -fx-background-radius: 10;
        """);

        Label keyLabel = new Label(key);
        keyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94A3B8; -fx-min-width: 90;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label valLabel = new Label(value);
        valLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #CBD5E1; -fx-font-weight: bold;");

        row.getChildren().addAll(keyLabel, spacer, valLabel);
        return row;
    }
}
