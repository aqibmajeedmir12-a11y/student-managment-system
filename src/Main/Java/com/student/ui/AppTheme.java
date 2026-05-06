package com.student.ui;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.util.Duration;

/**
 * Premium glassmorphism theme inspired by iPhone 17 Pro Max design language.
 * Features frosted glass effects, smooth animations, and premium aesthetics.
 */
public class AppTheme {

    // ===== Premium Color Palette =====
    public static final String PRIMARY = "#7C3AED";          // Deep Violet
    public static final String PRIMARY_DARK = "#6D28D9";
    public static final String PRIMARY_LIGHT = "#A78BFA";
    public static final String SECONDARY = "#1E1B4B";        // Dark Indigo
    public static final String ACCENT = "#10B981";           // Emerald
    public static final String ACCENT_LIGHT = "#34D399";
    public static final String WARNING = "#F59E0B";          // Amber
    public static final String DANGER = "#EF4444";           // Red
    public static final String DANGER_DARK = "#DC2626";
    public static final String INFO = "#3B82F6";             // Blue
    public static final String PINK = "#EC4899";             // Pink accent

    // Glass backgrounds
    public static final String BG_GRADIENT = "linear-gradient(to bottom right, #0F0A1A, #1A1035, #0D1B2A)";
    public static final String BG_MAIN = "#0F0A1A";
    public static final String BG_CARD = "rgba(255,255,255,0.06)";
    public static final String BG_CARD_HOVER = "rgba(255,255,255,0.10)";
    public static final String GLASS_BORDER = "rgba(255,255,255,0.12)";
    public static final String GLASS_BORDER_LIGHT = "rgba(255,255,255,0.18)";

    // Text colors
    public static final String TEXT_PRIMARY = "#F8FAFC";
    public static final String TEXT_SECONDARY = "#94A3B8";
    public static final String TEXT_MUTED = "#64748B";

    // Sidebar
    public static final String SIDEBAR_BG = "linear-gradient(to bottom, #0F0A1A, #1A0F2E)";

    // ===== Animations =====

    /** Fade in a node */
    public static void fadeIn(Node node, double durationMs) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setInterpolator(Interpolator.EASE_BOTH);
        ft.play();
    }

    /** Slide up + fade in */
    public static void slideUp(Node node, double durationMs) {
        node.setOpacity(0);
        node.setTranslateY(30);
        TranslateTransition tt = new TranslateTransition(Duration.millis(durationMs), node);
        tt.setFromY(30);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), node);
        ft.setFromValue(0);
        ft.setToValue(1);

        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.play();
    }

    /** Staggered animation for children */
    public static void staggerChildren(Pane parent, double delayMs, double durationMs) {
        for (int i = 0; i < parent.getChildren().size(); i++) {
            Node child = parent.getChildren().get(i);
            child.setOpacity(0);
            child.setTranslateY(20);

            TranslateTransition tt = new TranslateTransition(Duration.millis(durationMs), child);
            tt.setFromY(20);
            tt.setToY(0);
            tt.setDelay(Duration.millis(i * delayMs));
            tt.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition ft = new FadeTransition(Duration.millis(durationMs), child);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setDelay(Duration.millis(i * delayMs));

            ParallelTransition pt = new ParallelTransition(tt, ft);
            pt.play();
        }
    }

    /** Scale pulse on hover */
    public static void addHoverScale(Node node) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), node);
        scaleUp.setToX(1.03);
        scaleUp.setToY(1.03);
        scaleUp.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), node);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(Interpolator.EASE_OUT);

        node.setOnMouseEntered(e -> scaleUp.playFromStart());
        node.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    // ===== Sidebar =====

    public static String sidebarStyle() {
        return """
            -fx-background-color: linear-gradient(to bottom, #0A0612, #12082A, #0A0612);
            -fx-padding: 0;
        """;
    }

    public static String sidebarButtonStyle(boolean isActive) {
        if (isActive) {
            return """
                -fx-background-color: linear-gradient(to right, rgba(124,58,237,0.4), rgba(124,58,237,0.15));
                -fx-text-fill: #F8FAFC;
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                -fx-padding: 14 20;
                -fx-cursor: hand;
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-border-color: rgba(124,58,237,0.3);
                -fx-border-width: 1;
            """;
        }
        return """
            -fx-background-color: transparent;
            -fx-text-fill: #94A3B8;
            -fx-font-size: 13px;
            -fx-padding: 14 20;
            -fx-cursor: hand;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
        """;
    }

    public static String sidebarButtonHoverStyle() {
        return """
            -fx-background-color: rgba(255,255,255,0.06);
            -fx-text-fill: #F8FAFC;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-padding: 14 20;
            -fx-cursor: hand;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-color: rgba(255,255,255,0.08);
            -fx-border-width: 1;
        """;
    }

    // ===== Glass Card =====

    public static VBox createGlassCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(24));
        card.setStyle("""
            -fx-background-color: rgba(255,255,255,0.05);
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: rgba(255,255,255,0.10);
            -fx-border-width: 1;
        """);

        // Glow effect
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(124, 58, 237, 0.08));
        glow.setRadius(20);
        glow.setSpread(0);
        card.setEffect(glow);

        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle("""
                -fx-background-color: rgba(255,255,255,0.08);
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-border-color: rgba(255,255,255,0.15);
                -fx-border-width: 1;
            """);
            DropShadow hoverGlow = new DropShadow();
            hoverGlow.setColor(Color.rgb(124, 58, 237, 0.15));
            hoverGlow.setRadius(30);
            card.setEffect(hoverGlow);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("""
                -fx-background-color: rgba(255,255,255,0.05);
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-border-color: rgba(255,255,255,0.10);
                -fx-border-width: 1;
            """);
            DropShadow normalGlow = new DropShadow();
            normalGlow.setColor(Color.rgb(124, 58, 237, 0.08));
            normalGlow.setRadius(20);
            card.setEffect(normalGlow);
        });

        return card;
    }

    // ===== Stat Card =====

    public static VBox createStatCard(String title, String value, String iconEmoji, String accentColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(22));
        card.setPrefWidth(220);
        card.setMinHeight(140);
        card.setStyle(String.format("""
            -fx-background-color: rgba(255,255,255,0.05);
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: rgba(255,255,255,0.08);
            -fx-border-width: 1;
        """));

        // Icon with colored background
        Label icon = new Label(iconEmoji);
        icon.setStyle(String.format("""
            -fx-font-size: 24;
            -fx-background-color: %s22;
            -fx-padding: 8 12;
            -fx-background-radius: 12;
        """, accentColor));

        Label valueLabel = new Label(value);
        valueLabel.setStyle("""
            -fx-font-size: 32px;
            -fx-font-weight: bold;
            -fx-text-fill: #F8FAFC;
        """);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("""
            -fx-font-size: 12px;
            -fx-text-fill: #94A3B8;
            -fx-font-weight: bold;
        """);

        card.getChildren().addAll(icon, valueLabel, titleLabel);

        // Hover with glow
        addHoverScale(card);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web(accentColor, 0.1));
        glow.setRadius(15);
        card.setEffect(glow);

        card.setOnMouseEntered(e -> {
            card.setStyle(String.format("""
                -fx-background-color: rgba(255,255,255,0.08);
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-border-color: %s44;
                -fx-border-width: 1;
            """, accentColor));
            DropShadow hoverGlow = new DropShadow();
            hoverGlow.setColor(Color.web(accentColor, 0.2));
            hoverGlow.setRadius(25);
            card.setEffect(hoverGlow);
        });

        card.setOnMouseExited(e -> {
            card.setStyle("""
                -fx-background-color: rgba(255,255,255,0.05);
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-border-color: rgba(255,255,255,0.08);
                -fx-border-width: 1;
            """);
            DropShadow normalGlow = new DropShadow();
            normalGlow.setColor(Color.web(accentColor, 0.1));
            normalGlow.setRadius(15);
            card.setEffect(normalGlow);
        });

        return card;
    }

    // ===== Buttons =====

    public static Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        String baseStyle = """
            -fx-background-color: linear-gradient(to right, #7C3AED, #6D28D9);
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-padding: 12 28;
            -fx-cursor: hand;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(124,58,237,0.3), 10, 0, 0, 2);
        """;
        btn.setStyle(baseStyle);

        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: linear-gradient(to right, #8B5CF6, #7C3AED);
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-padding: 12 28;
            -fx-cursor: hand;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(124,58,237,0.5), 15, 0, 0, 3);
        """));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }

    public static Button createDangerButton(String text) {
        Button btn = new Button(text);
        String baseStyle = """
            -fx-background-color: linear-gradient(to right, #EF4444, #DC2626);
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-padding: 12 28;
            -fx-cursor: hand;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(239,68,68,0.3), 10, 0, 0, 2);
        """;
        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: linear-gradient(to right, #F87171, #EF4444);
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-padding: 12 28;
            -fx-cursor: hand;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(239,68,68,0.5), 15, 0, 0, 3);
        """));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }

    public static Button createSuccessButton(String text) {
        Button btn = new Button(text);
        String baseStyle = """
            -fx-background-color: linear-gradient(to right, #10B981, #059669);
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-padding: 12 28;
            -fx-cursor: hand;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.3), 10, 0, 0, 2);
        """;
        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: linear-gradient(to right, #34D399, #10B981);
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-font-weight: bold;
            -fx-padding: 12 28;
            -fx-cursor: hand;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.5), 15, 0, 0, 3);
        """));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }

    public static Button createGhostButton(String text) {
        Button btn = new Button(text);
        String baseStyle = """
            -fx-background-color: rgba(255,255,255,0.06);
            -fx-text-fill: #94A3B8;
            -fx-font-size: 12px;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: rgba(255,255,255,0.08);
            -fx-border-width: 1;
        """;
        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: rgba(255,255,255,0.10);
            -fx-text-fill: #F8FAFC;
            -fx-font-size: 12px;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: rgba(255,255,255,0.15);
            -fx-border-width: 1;
        """));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        return btn;
    }

    // ===== Text Fields =====

    public static TextField createTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        String baseStyle = """
            -fx-background-color: rgba(255,255,255,0.06);
            -fx-border-color: rgba(255,255,255,0.10);
            -fx-border-radius: 12;
            -fx-background-radius: 12;
            -fx-padding: 12 16;
            -fx-font-size: 13px;
            -fx-text-fill: #F8FAFC;
            -fx-prompt-text-fill: #64748B;
        """;
        field.setStyle(baseStyle);

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.08);
                    -fx-border-color: rgba(124,58,237,0.5);
                    -fx-border-radius: 12;
                    -fx-background-radius: 12;
                    -fx-padding: 12 16;
                    -fx-font-size: 13px;
                    -fx-text-fill: #F8FAFC;
                    -fx-prompt-text-fill: #64748B;
                    -fx-effect: dropshadow(gaussian, rgba(124,58,237,0.15), 8, 0, 0, 0);
                """);
            } else {
                field.setStyle(baseStyle);
            }
        });

        return field;
    }

    public static <T> ComboBox<T> createComboBox(String promptText) {
        ComboBox<T> combo = new ComboBox<>();
        combo.setPromptText(promptText);
        combo.setStyle("""
            -fx-background-color: rgba(255,255,255,0.06);
            -fx-border-color: rgba(255,255,255,0.10);
            -fx-border-radius: 12;
            -fx-background-radius: 12;
            -fx-padding: 4 8;
            -fx-font-size: 13px;
        """);
        return combo;
    }

    // ===== Typography =====

    public static Label createPageTitle(String text) {
        Label title = new Label(text);
        title.setStyle("""
            -fx-font-size: 30px;
            -fx-font-weight: bold;
            -fx-text-fill: #F8FAFC;
        """);
        return title;
    }

    public static Label createSectionTitle(String text) {
        Label title = new Label(text);
        title.setStyle("""
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-text-fill: #F8FAFC;
        """);
        return title;
    }

    public static Label createSubtitle(String text) {
        Label label = new Label(text);
        label.setStyle("""
            -fx-font-size: 13px;
            -fx-text-fill: #94A3B8;
        """);
        return label;
    }

    // ===== Table Styling =====

    public static <T> void styleTable(TableView<T> table) {
        table.setStyle("""
            -fx-background-color: transparent;
            -fx-table-cell-border-color: rgba(255,255,255,0.04);
            -fx-border-color: transparent;
            -fx-control-inner-background: rgba(255,255,255,0.03);
            -fx-control-inner-background-alt: rgba(255,255,255,0.05);
            -fx-selection-bar: rgba(124,58,237,0.3);
            -fx-selection-bar-non-focused: rgba(124,58,237,0.15);
            -fx-font-size: 13px;
            -fx-text-fill: #E2E8F0;
            -fx-fixed-cell-size: 42;
        """);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // ===== Status Badge =====

    public static Label createStatusBadge(String status) {
        Label badge = new Label(status);
        String bgColor = switch (status) {
            case "ACTIVE" -> "rgba(16,185,129,0.2)";
            case "GRADUATED" -> "rgba(124,58,237,0.2)";
            case "SUSPENDED" -> "rgba(239,68,68,0.2)";
            case "INACTIVE" -> "rgba(100,116,139,0.2)";
            default -> "rgba(148,163,184,0.2)";
        };
        String textColor = switch (status) {
            case "ACTIVE" -> "#34D399";
            case "GRADUATED" -> "#A78BFA";
            case "SUSPENDED" -> "#F87171";
            case "INACTIVE" -> "#94A3B8";
            default -> "#94A3B8";
        };

        badge.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: %s;
            -fx-padding: 5 14;
            -fx-background-radius: 20;
            -fx-font-size: 11px;
            -fx-font-weight: bold;
        """, bgColor, textColor));

        return badge;
    }

    // ===== Dialogs =====

    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleDialog(alert);
        alert.showAndWait();
    }

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleDialog(alert);
        alert.showAndWait();
    }

    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleDialog(alert);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleDialog(alert);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private static void styleDialog(Alert alert) {
        DialogPane pane = alert.getDialogPane();
        pane.setStyle("""
            -fx-background-color: #1A1035;
            -fx-border-color: rgba(255,255,255,0.10);
            -fx-border-radius: 16;
            -fx-background-radius: 16;
        """);
        // Safe lookup — node may not exist yet before show
        pane.applyCss();
        javafx.scene.Node contentLabel = pane.lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 13px;");
        }
    }
}
