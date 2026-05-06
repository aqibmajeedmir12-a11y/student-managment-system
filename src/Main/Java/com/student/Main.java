package com.student;

import com.student.database.DatabaseManager;
import com.student.ui.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main application — Premium glassmorphism UI with animated transitions.
 */
public class Main extends Application {

    private BorderPane root;
    private StackPane contentArea;
    private String activePage = "Dashboard";
    private VBox menuItems;

    @Override
    public void start(Stage stage) {
        DatabaseManager.getInstance();

        root = new BorderPane();
        root.setStyle("-fx-background-color: #0F0A1A;");

        VBox sidebar = createSidebar();
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #0F0A1A;");

        switchPage("Dashboard");

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 1320, 850);

        // Apply dark theme CSS stylesheet
        scene.setFill(Color.web("#0F0A1A"));
        String css = getClass().getResource("/styles/dark-theme.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Student Management System v2.0 — Enterprise");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setOnCloseRequest(e -> {
            DatabaseManager.getInstance().close();
        });

        stage.show();

        // Animate sidebar entrance
        AppTheme.fadeIn(sidebar, 500);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        sidebar.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #0A0612, #12082A, #0A0612);
            -fx-border-color: rgba(255,255,255,0.06);
            -fx-border-width: 0 1 0 0;
        """);

        // Logo
        VBox logoSection = new VBox(6);
        logoSection.setPadding(new Insets(32, 24, 32, 24));
        logoSection.setAlignment(Pos.CENTER_LEFT);

        Label logoIcon = new Label("🎓");
        logoIcon.setStyle("-fx-font-size: 36;");

        Label appName = new Label("Student MS");
        appName.setStyle("""
            -fx-text-fill: #F8FAFC;
            -fx-font-size: 24px;
            -fx-font-weight: bold;
        """);

        Label tagline = new Label("Enterprise Edition");
        tagline.setStyle("""
            -fx-text-fill: rgba(124,58,237,0.7);
            -fx-font-size: 11px;
            -fx-font-weight: bold;
        """);

        logoSection.getChildren().addAll(logoIcon, appName, tagline);

        // Separator
        Region sep = new Region();
        sep.setPrefHeight(1);
        sep.setMaxWidth(Double.MAX_VALUE);
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.06);");
        VBox.setMargin(sep, new Insets(0, 24, 12, 24));

        // Menu label
        Label menuLabel = new Label("NAVIGATION");
        menuLabel.setStyle("""
            -fx-text-fill: #64748B;
            -fx-font-size: 10px;
            -fx-font-weight: bold;
            -fx-padding: 12 24 8 24;
        """);

        // Menu items
        menuItems = new VBox(6);
        menuItems.setPadding(new Insets(0, 14, 0, 14));
        menuItems.getChildren().addAll(
            createNavButton("📊  Dashboard", "Dashboard"),
            createNavButton("🎓  Students", "Students"),
            createNavButton("📚  Courses", "Courses"),
            createNavButton("⚙️  Settings", "Settings")
        );

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Footer
        VBox footer = new VBox(6);
        footer.setPadding(new Insets(20, 24, 24, 24));
        footer.setAlignment(Pos.CENTER);

        Region footerSep = new Region();
        footerSep.setPrefHeight(1);
        footerSep.setMaxWidth(Double.MAX_VALUE);
        footerSep.setStyle("-fx-background-color: rgba(255,255,255,0.06);");

        Label footerText = new Label("© 2026 Student MS");
        footerText.setStyle("-fx-text-fill: #4A4A6A; -fx-font-size: 10px;");

        Label footerV = new Label("v2.0.0 • Glassmorphism");
        footerV.setStyle("-fx-text-fill: rgba(124,58,237,0.4); -fx-font-size: 9px;");

        footer.getChildren().addAll(footerSep, footerText, footerV);

        sidebar.getChildren().addAll(logoSection, sep, menuLabel, menuItems, spacer, footer);

        // Glow effect on sidebar
        DropShadow sidebarGlow = new DropShadow();
        sidebarGlow.setColor(Color.rgb(124, 58, 237, 0.05));
        sidebarGlow.setRadius(30);
        sidebarGlow.setOffsetX(5);
        sidebar.setEffect(sidebarGlow);

        return sidebar;
    }

    private Button createNavButton(String text, String page) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(AppTheme.sidebarButtonStyle(page.equals(activePage)));

        btn.setOnAction(e -> {
            if (!page.equals(activePage)) {
                switchPage(page);
                updateNavStyles(btn);
            }
        });

        if (!page.equals(activePage)) {
            btn.setOnMouseEntered(ev -> btn.setStyle(AppTheme.sidebarButtonHoverStyle()));
            btn.setOnMouseExited(ev -> btn.setStyle(AppTheme.sidebarButtonStyle(false)));
        }

        return btn;
    }

    private void updateNavStyles(Button activeBtn) {
        for (javafx.scene.Node node : menuItems.getChildren()) {
            if (node instanceof Button navBtn) {
                boolean isActive = navBtn == activeBtn;
                navBtn.setStyle(AppTheme.sidebarButtonStyle(isActive));

                if (!isActive) {
                    navBtn.setOnMouseEntered(ev -> navBtn.setStyle(AppTheme.sidebarButtonHoverStyle()));
                    navBtn.setOnMouseExited(ev -> navBtn.setStyle(AppTheme.sidebarButtonStyle(false)));
                } else {
                    navBtn.setOnMouseEntered(null);
                    navBtn.setOnMouseExited(null);
                }
            }
        }
    }

    private void switchPage(String page) {
        activePage = page;

        // Create new view
        javafx.scene.Node newContent;
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: #0F0A1A; -fx-border-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        switch (page) {
            case "Dashboard" -> scrollPane.setContent(new DashboardView().getView());
            case "Students" -> scrollPane.setContent(new StudentView().getView());
            case "Courses" -> scrollPane.setContent(new CourseView().getView());
            case "Settings" -> scrollPane.setContent(new SettingsView().getView());
        }

        newContent = scrollPane;

        // Animated transition
        if (!contentArea.getChildren().isEmpty()) {
            javafx.scene.Node oldContent = contentArea.getChildren().get(0);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), oldContent);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            newContent.setOpacity(0);
            newContent.setTranslateY(15);

            fadeOut.setOnFinished(e -> {
                contentArea.getChildren().setAll(newContent);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newContent);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);

                TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), newContent);
                slideIn.setFromY(15);
                slideIn.setToY(0);
                slideIn.setInterpolator(Interpolator.EASE_OUT);

                new ParallelTransition(fadeIn, slideIn).play();
            });

            fadeOut.play();
        } else {
            contentArea.getChildren().setAll(newContent);
            AppTheme.fadeIn(newContent, 400);
        }
    }

    @Override
    public void stop() {
        DatabaseManager.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}