package com.student.ui;

import com.student.dao.StudentDAO;
import com.student.dao.CourseDAO;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Map;

/**
 * Premium glassmorphism settings and about view.
 */
public class SettingsView {

    public VBox getView() {
        VBox view = new VBox(24);
        view.setPadding(new Insets(36));
        view.setStyle("-fx-background-color: #0F0A1A;");

        Label title = AppTheme.createPageTitle("⚙️ Settings & About");
        Label subtitle = AppTheme.createSubtitle("Application information and configuration");
        AppTheme.slideUp(title, 400);

        // About Card
        VBox aboutCard = AppTheme.createGlassCard();
        Label aboutTitle = AppTheme.createSectionTitle("🔧 System");
        aboutCard.getChildren().addAll(aboutTitle,
            infoRow("Version", "2.0.0 Enterprise"),
            infoRow("Engine", "JavaFX 17 + SQLite"),
            infoRow("Java", System.getProperty("java.version")),
            infoRow("OS", System.getProperty("os.name") + " " + System.getProperty("os.version")),
            infoRow("Architecture", System.getProperty("os.arch"))
        );
        AppTheme.slideUp(aboutCard, 500);

        // DB Card
        VBox dbCard = AppTheme.createGlassCard();
        Label dbTitle = AppTheme.createSectionTitle("🗄️ Database");
        StudentDAO dao = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        dbCard.getChildren().addAll(dbTitle,
            infoRow("Type", "SQLite (Local File)"),
            infoRow("Total Students", String.valueOf(dao.getTotalCount())),
            infoRow("Active Students", String.valueOf(dao.getActiveCount())),
            infoRow("Total Courses", String.valueOf(courseDAO.getTotalCount())),
            infoRow("Departments", String.valueOf(dao.getStudentCountByDepartment().size()))
        );
        AppTheme.slideUp(dbCard, 600);

        // Features Card
        VBox featuresCard = AppTheme.createGlassCard();
        Label featuresTitle = AppTheme.createSectionTitle("✨ Features");
        VBox featureList = new VBox(6);
        String[] features = {
            "✅ Full CRUD for Students and Courses",
            "✅ Real-time search across all fields",
            "✅ Filter by Department & Status",
            "✅ Interactive Dashboard with Charts",
            "✅ CSV Export & Report Generation",
            "✅ SQLite Database — no server needed",
            "✅ Enterprise input validation",
            "✅ Activity logging audit trail",
            "✅ Glassmorphism premium UI",
            "✅ Smooth animations & transitions"
        };
        for (String f : features) {
            Label fl = new Label(f);
            fl.setStyle("-fx-font-size: 13px; -fx-text-fill: #CBD5E1;");
            featureList.getChildren().add(fl);
        }
        featuresCard.getChildren().addAll(featuresTitle, featureList);
        AppTheme.slideUp(featuresCard, 700);

        // Danger Zone
        VBox dangerCard = AppTheme.createGlassCard();
        dangerCard.setStyle(dangerCard.getStyle() + "-fx-border-color: rgba(239,68,68,0.2);");
        Label dangerTitle = AppTheme.createSectionTitle("⚠️ Danger Zone");

        Button resetBtn = AppTheme.createDangerButton("🗑 Reset Database");
        resetBtn.setOnAction(e -> {
            if (AppTheme.showConfirmation("Reset Database",
                    "This will DELETE ALL data. Are you absolutely sure?")) {
                // Delete the database file
                String userHome = System.getProperty("user.home");
                java.io.File dbFile = new java.io.File(userHome + "/.student-management/student_management.db");
                java.io.File walFile = new java.io.File(userHome + "/.student-management/student_management.db-wal");
                java.io.File shmFile = new java.io.File(userHome + "/.student-management/student_management.db-shm");
                com.student.database.DatabaseManager.getInstance().close();
                dbFile.delete();
                walFile.delete();
                shmFile.delete();
                AppTheme.showSuccess("Reset Complete", "Database has been reset. Please restart the application.");
            }
        });

        Label dangerNote = new Label("This will permanently delete all students, courses, and activity logs.");
        dangerNote.setStyle("-fx-text-fill: #F87171; -fx-font-size: 11px;");
        dangerCard.getChildren().addAll(dangerTitle, dangerNote, resetBtn);
        AppTheme.slideUp(dangerCard, 800);

        view.getChildren().addAll(title, subtitle, aboutCard, dbCard, featuresCard, dangerCard);

        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #0F0A1A; -fx-border-color: transparent;");

        VBox wrapper = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        return wrapper;
    }

    private HBox infoRow(String key, String value) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(8, 14, 8, 14));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: rgba(255,255,255,0.03); -fx-background-radius: 10;");

        Label k = new Label(key);
        k.setStyle("-fx-font-size: 12px; -fx-text-fill: #94A3B8; -fx-min-width: 120;");
        Region s = new Region();
        HBox.setHgrow(s, Priority.ALWAYS);
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 12px; -fx-text-fill: #CBD5E1; -fx-font-weight: bold;");

        row.getChildren().addAll(k, s, v);
        return row;
    }
}
