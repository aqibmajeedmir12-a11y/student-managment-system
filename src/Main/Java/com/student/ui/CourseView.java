package com.student.ui;

import com.student.dao.CourseDAO;
import com.student.model.Course;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.Optional;

/**
 * Premium glassmorphism course management view.
 */
public class CourseView {

    private final CourseDAO courseDAO;
    private TableView<Course> table;
    private ObservableList<Course> courseList;
    private TextField searchField;

    public CourseView() {
        this.courseDAO = new CourseDAO();
    }

    public VBox getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(36));
        view.setStyle("-fx-background-color: #0F0A1A;");

        HBox header = createHeader();
        AppTheme.slideUp(header, 400);

        HBox searchBar = createSearchBar();
        AppTheme.slideUp(searchBar, 500);

        VBox tableCard = createTableCard();
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        AppTheme.slideUp(tableCard, 600);

        view.getChildren().addAll(header, searchBar, tableCard);
        return view;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        titleBox.getChildren().addAll(
            AppTheme.createPageTitle("📚 Courses"),
            AppTheme.createSubtitle("Manage courses and programs")
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = AppTheme.createPrimaryButton("➕ Add Course");
        addBtn.setOnAction(e -> showCourseForm(null));

        header.getChildren().addAll(titleBox, spacer, addBtn);
        return header;
    }

    private HBox createSearchBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 18, 14, 18));
        bar.setStyle("""
            -fx-background-color: rgba(255,255,255,0.04);
            -fx-background-radius: 16;
            -fx-border-radius: 16;
            -fx-border-color: rgba(255,255,255,0.08);
            -fx-border-width: 1;
        """);

        searchField = AppTheme.createTextField("🔍 Search courses by code, name, department...");
        searchField.setPrefWidth(400);
        searchField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                courseList = courseDAO.getAllCourses();
            } else {
                courseList = courseDAO.searchCourses(newVal.trim());
            }
            table.setItems(courseList);
        });

        bar.getChildren().add(searchField);
        return bar;
    }

    @SuppressWarnings("unchecked")
    private VBox createTableCard() {
        VBox card = new VBox(0);
        card.setStyle("""
            -fx-background-color: rgba(255,255,255,0.03);
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: rgba(255,255,255,0.06);
            -fx-border-width: 1;
            -fx-padding: 4;
        """);
        VBox.setVgrow(card, Priority.ALWAYS);

        table = new TableView<>();
        AppTheme.styleTable(table);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Course, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Course, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        codeCol.setPrefWidth(80);

        TableColumn<Course, String> nameCol = new TableColumn<>("Course Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        nameCol.setPrefWidth(220);

        TableColumn<Course, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        deptCol.setPrefWidth(160);

        TableColumn<Course, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("credits"));
        creditsCol.setPrefWidth(70);
        creditsCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Course, String> instructorCol = new TableColumn<>("Instructor");
        instructorCol.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        instructorCol.setPrefWidth(150);

        TableColumn<Course, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        capacityCol.setPrefWidth(80);
        capacityCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Course, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(120);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = actionBtn("✏️", "Edit");
            private final Button deleteBtn = actionBtn("🗑", "Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Course course = getTableView().getItems().get(getIndex());
                editBtn.setOnAction(e -> showCourseForm(course));
                deleteBtn.setOnAction(e -> deleteCourse(course));
                HBox actions = new HBox(4, editBtn, deleteBtn);
                actions.setAlignment(Pos.CENTER);
                setGraphic(actions);
            }
        });

        table.getColumns().addAll(idCol, codeCol, nameCol, deptCol, creditsCol, instructorCol, capacityCol, actionsCol);
        courseList = courseDAO.getAllCourses();
        table.setItems(courseList);

        Label placeholder = new Label("No courses found. Click '➕ Add Course' to create one!");
        placeholder.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14px;");
        table.setPlaceholder(placeholder);

        card.getChildren().add(table);
        return card;
    }

    private Button actionBtn(String emoji, String tip) {
        Button btn = new Button(emoji);
        btn.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-cursor: hand; -fx-padding: 6 10; -fx-font-size: 13px; -fx-background-radius: 8;");
        btn.setTooltip(new Tooltip(tip));
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: rgba(255,255,255,0.12); -fx-cursor: hand; -fx-padding: 6 10; -fx-font-size: 13px; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-cursor: hand; -fx-padding: 6 10; -fx-font-size: 13px; -fx-background-radius: 8;"));
        return btn;
    }

    private void showCourseForm(Course existing) {
        boolean isEdit = existing != null;
        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "✏️ Edit Course" : "➕ Add New Course");
        dialog.setHeaderText(isEdit ? "Update course information" : "Enter course details");

        ButtonType saveType = new ButtonType(isEdit ? "Update" : "Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(14); grid.setVgap(14); grid.setPadding(new Insets(24));
        grid.setStyle("-fx-background-color: #1A1035;");

        TextField codeField = formField("Course Code (e.g., CS101) *");
        TextField nameField = formField("Course Name *");
        TextField deptField = formField("Department *");
        TextField creditsField = formField("Credits");
        TextField instructorField = formField("Instructor");
        TextField capacityField = formField("Max Capacity");
        TextArea descField = new TextArea();
        descField.setPromptText("Course description...");
        descField.setPrefRowCount(3);
        descField.setStyle("-fx-control-inner-background: rgba(255,255,255,0.06); -fx-text-fill: #F8FAFC;");

        if (isEdit) {
            codeField.setText(existing.getCourseCode());
            nameField.setText(existing.getCourseName());
            deptField.setText(existing.getDepartment());
            creditsField.setText(String.valueOf(existing.getCredits()));
            instructorField.setText(existing.getInstructor());
            capacityField.setText(String.valueOf(existing.getMaxCapacity()));
            descField.setText(existing.getDescription());
        }

        int r = 0;
        grid.add(lbl("Code *"), 0, r);      grid.add(codeField, 1, r); r++;
        grid.add(lbl("Name *"), 0, r);       grid.add(nameField, 1, r); r++;
        grid.add(lbl("Department *"), 0, r); grid.add(deptField, 1, r); r++;
        grid.add(lbl("Credits"), 0, r);      grid.add(creditsField, 1, r); r++;
        grid.add(lbl("Instructor"), 0, r);   grid.add(instructorField, 1, r); r++;
        grid.add(lbl("Capacity"), 0, r);     grid.add(capacityField, 1, r); r++;
        grid.add(lbl("Description"), 0, r);  grid.add(descField, 1, r);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setPrefWidth(500);
        dialog.getDialogPane().setStyle("-fx-background-color: #1A1035;");

        dialog.setResultConverter(bt -> {
            if (bt == saveType) {
                if (codeField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()
                        || deptField.getText().trim().isEmpty()) {
                    AppTheme.showWarning("Validation Error", "Code, Name, and Department are required.");
                    return null;
                }
                int cr = 3; try { cr = Integer.parseInt(creditsField.getText().trim()); } catch (Exception ignored) {}
                int cap = 60; try { cap = Integer.parseInt(capacityField.getText().trim()); } catch (Exception ignored) {}

                if (isEdit) {
                    existing.setCourseCode(codeField.getText().trim());
                    existing.setCourseName(nameField.getText().trim());
                    existing.setDepartment(deptField.getText().trim());
                    existing.setCredits(cr);
                    existing.setInstructor(instructorField.getText().trim());
                    existing.setMaxCapacity(cap);
                    existing.setDescription(descField.getText().trim());
                    return existing;
                }
                return new Course(codeField.getText().trim(), nameField.getText().trim(),
                    deptField.getText().trim(), cr, instructorField.getText().trim(), cap,
                    descField.getText().trim());
            }
            return null;
        });

        Optional<Course> result = dialog.showAndWait();
        result.ifPresent(course -> {
            if (isEdit) {
                courseDAO.updateCourse(course);
                AppTheme.showSuccess("Updated", "Course updated!");
            } else {
                int id = courseDAO.addCourse(course);
                if (id > 0) AppTheme.showSuccess("Added", "Course added!");
                else AppTheme.showError("Error", "Failed. Code may already exist.");
            }
            refreshTable();
        });
    }

    private void deleteCourse(Course course) {
        if (AppTheme.showConfirmation("Delete Course", "Delete '" + course.getDisplayName() + "'?")) {
            if (courseDAO.deleteCourse(course.getId())) {
                AppTheme.showSuccess("Deleted", "Course deleted.");
                refreshTable();
            } else AppTheme.showError("Error", "Failed to delete.");
        }
    }

    private void refreshTable() {
        courseList = courseDAO.getAllCourses();
        table.setItems(courseList);
        table.refresh();
    }

    private TextField formField(String p) {
        TextField f = new TextField(); f.setPromptText(p);
        f.setStyle("-fx-background-color: rgba(255,255,255,0.06); -fx-border-color: rgba(255,255,255,0.10); " +
            "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 14; -fx-font-size: 13px; -fx-text-fill: #F8FAFC; -fx-prompt-text-fill: #64748B;");
        return f;
    }

    private Label lbl(String t) {
        Label l = new Label(t); l.setStyle("-fx-font-size: 12px; -fx-text-fill: #94A3B8; -fx-font-weight: bold;");
        return l;
    }
}
