package com.student.ui;

import com.student.dao.StudentDAO;
import com.student.model.Student;
import com.student.util.ExportUtil;
import com.student.util.ValidationUtil;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Premium glassmorphism student management view with full CRUD.
 */
public class StudentView {

    private final StudentDAO studentDAO;
    private TableView<Student> table;
    private ObservableList<Student> studentList;
    private TextField searchField;
    private ComboBox<String> departmentFilter;
    private ComboBox<String> statusFilter;
    private Label statusBar;

    public StudentView() {
        this.studentDAO = new StudentDAO();
    }

    public VBox getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(36));
        view.setStyle("-fx-background-color: #0F0A1A;");

        HBox header = createHeader();
        AppTheme.slideUp(header, 400);

        HBox filterBar = createFilterBar();
        AppTheme.slideUp(filterBar, 500);

        VBox tableCard = createTableCard();
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        AppTheme.slideUp(tableCard, 600);

        statusBar = new Label("");
        statusBar.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");
        updateStatusBar();

        view.getChildren().addAll(header, filterBar, tableCard, statusBar);
        return view;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Label title = AppTheme.createPageTitle("🎓 Students");
        Label subtitle = AppTheme.createSubtitle("Manage all student records");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = AppTheme.createPrimaryButton("➕ Add Student");
        addBtn.setOnAction(e -> showStudentForm(null));

        Button exportCsvBtn = AppTheme.createSuccessButton("📤 Export CSV");
        exportCsvBtn.setOnAction(e -> {
            Stage stage = (Stage) exportCsvBtn.getScene().getWindow();
            if (ExportUtil.exportToCSV(studentList, stage)) {
                AppTheme.showSuccess("Export Successful", "Student data exported to CSV!");
            }
        });

        Button reportBtn = AppTheme.createGhostButton("📊 Report");
        reportBtn.setOnAction(e -> {
            Stage stage = (Stage) reportBtn.getScene().getWindow();
            double avgGpa = studentDAO.getAverageGpa();
            int courses = new com.student.dao.CourseDAO().getTotalCount();
            if (ExportUtil.exportSummaryReport(studentList, courses, avgGpa, stage)) {
                AppTheme.showSuccess("Report Generated", "Summary report exported!");
            }
        });

        HBox btnRow = new HBox(10, addBtn, exportCsvBtn, reportBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(titleBox, spacer, btnRow);
        return header;
    }

    private HBox createFilterBar() {
        HBox filterBar = new HBox(12);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(14, 18, 14, 18));
        filterBar.setStyle("""
            -fx-background-color: rgba(255,255,255,0.04);
            -fx-background-radius: 16;
            -fx-border-radius: 16;
            -fx-border-color: rgba(255,255,255,0.08);
            -fx-border-width: 1;
        """);

        searchField = AppTheme.createTextField("🔍 Search by name, email, course...");
        searchField.setPrefWidth(320);
        searchField.textProperty().addListener((obs, old, newVal) -> applyFilters());

        departmentFilter = AppTheme.createComboBox("All Departments");
        departmentFilter.getItems().add("All Departments");
        departmentFilter.getItems().addAll(studentDAO.getAllDepartments());
        departmentFilter.setValue("All Departments");
        departmentFilter.setOnAction(e -> applyFilters());

        statusFilter = AppTheme.createComboBox("All Statuses");
        statusFilter.getItems().addAll("All Statuses", "ACTIVE", "INACTIVE", "GRADUATED", "SUSPENDED");
        statusFilter.setValue("All Statuses");
        statusFilter.setOnAction(e -> applyFilters());

        Button clearBtn = AppTheme.createGhostButton("✕ Clear");
        clearBtn.setOnAction(e -> {
            searchField.clear();
            departmentFilter.setValue("All Departments");
            statusFilter.setValue("All Statuses");
            applyFilters();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        filterBar.getChildren().addAll(searchField, departmentFilter, statusFilter, clearBtn, spacer);
        return filterBar;
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

        TableColumn<Student, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Student, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFullName()));
        nameCol.setPrefWidth(160);

        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);

        TableColumn<Student, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        courseCol.setPrefWidth(80);

        TableColumn<Student, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        deptCol.setPrefWidth(140);

        TableColumn<Student, Integer> semCol = new TableColumn<>("Sem");
        semCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        semCol.setPrefWidth(50);
        semCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Student, Double> gpaCol = new TableColumn<>("GPA");
        gpaCol.setCellValueFactory(new PropertyValueFactory<>("gpa"));
        gpaCol.setPrefWidth(60);
        gpaCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double gpa, boolean empty) {
                super.updateItem(gpa, empty);
                if (empty || gpa == null) { setText(null); setStyle(""); return; }
                setText(String.format("%.2f", gpa));
                String color = gpa >= 3.5 ? "#34D399" : gpa >= 2.5 ? "#FBBF24" : "#F87171";
                setStyle("-fx-alignment: CENTER; -fx-text-fill: " + color + "; -fx-font-weight: bold;");
            }
        });

        TableColumn<Student, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("enrollmentStatus"));
        statusCol.setPrefWidth(100);
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { setGraphic(null); } 
                else { setGraphic(AppTheme.createStatusBadge(status)); }
            }
        });

        TableColumn<Student, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = createActionBtn("👁", "View Details");
            private final Button editBtn = createActionBtn("✏️", "Edit");
            private final Button deleteBtn = createActionBtn("🗑", "Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                Student student = getTableView().getItems().get(getIndex());
                viewBtn.setOnAction(e -> showStudentDetails(student));
                editBtn.setOnAction(e -> showStudentForm(student));
                deleteBtn.setOnAction(e -> deleteStudent(student));
                HBox actions = new HBox(2, viewBtn, editBtn, deleteBtn);
                actions.setAlignment(Pos.CENTER);
                setGraphic(actions);
            }
        });

        table.getColumns().addAll(idCol, nameCol, emailCol, courseCol, deptCol, semCol, gpaCol, statusCol, actionsCol);
        studentList = studentDAO.getAllStudents();
        table.setItems(studentList);

        Label placeholder = new Label("No students found. Click '➕ Add Student' to get started!");
        placeholder.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14px;");
        table.setPlaceholder(placeholder);

        card.getChildren().add(table);
        return card;
    }

    private Button createActionBtn(String emoji, String tooltip) {
        Button btn = new Button(emoji);
        btn.setStyle("""
            -fx-background-color: rgba(255,255,255,0.05);
            -fx-cursor: hand;
            -fx-padding: 6 10;
            -fx-font-size: 13px;
            -fx-background-radius: 8;
        """);
        btn.setTooltip(new Tooltip(tooltip));
        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: rgba(255,255,255,0.12);
            -fx-cursor: hand;
            -fx-padding: 6 10;
            -fx-font-size: 13px;
            -fx-background-radius: 8;
        """));
        btn.setOnMouseExited(e -> btn.setStyle("""
            -fx-background-color: rgba(255,255,255,0.05);
            -fx-cursor: hand;
            -fx-padding: 6 10;
            -fx-font-size: 13px;
            -fx-background-radius: 8;
        """));
        return btn;
    }

    private void applyFilters() {
        String keyword = searchField.getText();
        String dept = departmentFilter.getValue();
        String status = statusFilter.getValue();

        ObservableList<Student> results;
        if (keyword != null && !keyword.trim().isEmpty()) {
            results = studentDAO.searchStudents(keyword.trim());
        } else {
            results = studentDAO.getAllStudents();
        }
        if (dept != null && !"All Departments".equals(dept)) {
            results = results.filtered(s -> dept.equals(s.getDepartment()));
        }
        if (status != null && !"All Statuses".equals(status)) {
            results = results.filtered(s -> status.equals(s.getEnrollmentStatus()));
        }

        studentList = results;
        table.setItems(studentList);
        updateStatusBar();
    }

    private void updateStatusBar() {
        int showing = table.getItems().size();
        int total = studentDAO.getTotalCount();
        statusBar.setText(String.format("Showing %d of %d students", showing, total));
    }

    private void showStudentForm(Student existingStudent) {
        boolean isEdit = existingStudent != null;
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "✏️ Edit Student" : "➕ Add New Student");
        dialog.setHeaderText(isEdit ? "Update student information" : "Fill in the student details");

        ButtonType saveType = new ButtonType(isEdit ? "Update" : "Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        grid.setPadding(new Insets(24));
        grid.setStyle("-fx-background-color: #1A1035;");

        TextField firstNameField = createFormField("First Name *");
        TextField lastNameField = createFormField("Last Name *");
        TextField emailField = createFormField("Email *");
        TextField phoneField = createFormField("Phone");
        TextField courseField = createFormField("Course Code *");
        TextField departmentField = createFormField("Department *");
        TextField semesterField = createFormField("Semester (1-8)");
        TextField gpaField = createFormField("GPA (0.0 - 4.0)");
        TextField addressField = createFormField("Address");

        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Date of Birth");
        dobPicker.setStyle("-fx-background-color: rgba(255,255,255,0.06); -fx-border-radius: 8; -fx-background-radius: 8;");

        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.setPromptText("Gender");
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setStyle("-fx-background-color: rgba(255,255,255,0.06); -fx-border-radius: 8; -fx-background-radius: 8;");

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.setPromptText("Status");
        statusCombo.getItems().addAll("ACTIVE", "INACTIVE", "GRADUATED", "SUSPENDED");
        statusCombo.setValue("ACTIVE");
        statusCombo.setStyle("-fx-background-color: rgba(255,255,255,0.06); -fx-border-radius: 8; -fx-background-radius: 8;");

        if (isEdit) {
            firstNameField.setText(existingStudent.getFirstName());
            lastNameField.setText(existingStudent.getLastName());
            emailField.setText(existingStudent.getEmail());
            phoneField.setText(existingStudent.getPhone());
            courseField.setText(existingStudent.getCourse());
            departmentField.setText(existingStudent.getDepartment());
            semesterField.setText(String.valueOf(existingStudent.getSemester()));
            gpaField.setText(String.format("%.2f", existingStudent.getGpa()));
            addressField.setText(existingStudent.getAddress());
            dobPicker.setValue(existingStudent.getDateOfBirth());
            genderCombo.setValue(existingStudent.getGender());
            statusCombo.setValue(existingStudent.getEnrollmentStatus());
        }

        int r = 0;
        grid.add(formLabel("First Name *"), 0, r); grid.add(firstNameField, 1, r);
        grid.add(formLabel("Last Name *"), 2, r);  grid.add(lastNameField, 3, r); r++;
        grid.add(formLabel("Email *"), 0, r);       grid.add(emailField, 1, r);
        grid.add(formLabel("Phone"), 2, r);         grid.add(phoneField, 3, r); r++;
        grid.add(formLabel("Course *"), 0, r);      grid.add(courseField, 1, r);
        grid.add(formLabel("Department *"), 2, r);  grid.add(departmentField, 3, r); r++;
        grid.add(formLabel("Semester"), 0, r);      grid.add(semesterField, 1, r);
        grid.add(formLabel("GPA"), 2, r);           grid.add(gpaField, 3, r); r++;
        grid.add(formLabel("Birth Date"), 0, r);    grid.add(dobPicker, 1, r);
        grid.add(formLabel("Gender"), 2, r);        grid.add(genderCombo, 3, r); r++;
        grid.add(formLabel("Address"), 0, r);       grid.add(addressField, 1, r, 3, 1); r++;
        grid.add(formLabel("Status"), 0, r);        grid.add(statusCombo, 1, r);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setPrefWidth(720);
        dialog.getDialogPane().setStyle("-fx-background-color: #1A1035;");

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveType) {
                String errors = ValidationUtil.validateStudentForm(
                    firstNameField.getText(), lastNameField.getText(),
                    emailField.getText(), phoneField.getText(),
                    courseField.getText(), departmentField.getText(),
                    semesterField.getText(), gpaField.getText());

                if (!errors.isEmpty()) {
                    AppTheme.showWarning("Validation Error", errors);
                    return null;
                }

                int excludeId = isEdit ? existingStudent.getId() : 0;
                if (studentDAO.emailExists(emailField.getText().trim(), excludeId)) {
                    AppTheme.showWarning("Duplicate Email", "A student with this email already exists.");
                    return null;
                }

                int sem = 1;
                try { sem = Integer.parseInt(semesterField.getText().trim()); } catch (Exception ignored) {}
                double gpa = 0.0;
                try { gpa = Double.parseDouble(gpaField.getText().trim()); } catch (Exception ignored) {}

                Student student;
                if (isEdit) {
                    student = existingStudent;
                    student.setFirstName(firstNameField.getText().trim());
                    student.setLastName(lastNameField.getText().trim());
                    student.setEmail(emailField.getText().trim());
                    student.setPhone(phoneField.getText().trim());
                    student.setCourse(courseField.getText().trim());
                    student.setDepartment(departmentField.getText().trim());
                    student.setSemester(sem);
                    student.setDateOfBirth(dobPicker.getValue());
                    student.setGender(genderCombo.getValue());
                    student.setAddress(addressField.getText().trim());
                    student.setGpa(gpa);
                    student.setEnrollmentStatus(statusCombo.getValue());
                } else {
                    student = new Student(
                        firstNameField.getText().trim(), lastNameField.getText().trim(),
                        emailField.getText().trim(), phoneField.getText().trim(),
                        courseField.getText().trim(), departmentField.getText().trim(),
                        sem, dobPicker.getValue(),
                        genderCombo.getValue(), addressField.getText().trim());
                    student.setGpa(gpa);
                    student.setEnrollmentStatus(statusCombo.getValue());
                }
                return student;
            }
            return null;
        });

        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(student -> {
            if (isEdit) {
                if (studentDAO.updateStudent(student)) {
                    AppTheme.showSuccess("Updated", "Student '" + student.getFullName() + "' updated!");
                } else {
                    AppTheme.showError("Error", "Failed to update student.");
                }
            } else {
                int newId = studentDAO.addStudent(student);
                if (newId > 0) {
                    AppTheme.showSuccess("Added", "Student '" + student.getFullName() + "' added!");
                } else {
                    AppTheme.showError("Error", "Failed to add student.");
                }
            }
            refreshTable();
        });
    }

    private void showStudentDetails(Student student) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Student Profile");
        dialog.setHeaderText("📋 " + student.getFullName());
        dialog.setContentText(String.format("""
            📧  Email:        %s
            📱  Phone:        %s
            📚  Course:       %s
            🏛  Department:   %s
            📅  Semester:     %d
            🎂  Birth Date:   %s
            👤  Gender:       %s
            🏠  Address:      %s
            📊  GPA:          %.2f
            🔖  Status:       %s
            🕐  Enrolled:     %s
            """,
            student.getEmail(),
            student.getPhone() != null ? student.getPhone() : "N/A",
            student.getCourse(),
            student.getDepartment(),
            student.getSemester(),
            student.getFormattedDob(),
            student.getGender() != null ? student.getGender() : "N/A",
            student.getAddress() != null ? student.getAddress() : "N/A",
            student.getGpa(),
            student.getEnrollmentStatus(),
            student.getFormattedCreatedAt()));
        dialog.getDialogPane().setPrefWidth(480);
        dialog.showAndWait();
    }

    private void deleteStudent(Student student) {
        if (AppTheme.showConfirmation("Delete Student",
                "Are you sure you want to delete '" + student.getFullName() + "'?\n\nThis cannot be undone.")) {
            if (studentDAO.deleteStudent(student.getId())) {
                AppTheme.showSuccess("Deleted", "Student removed successfully.");
                refreshTable();
            } else {
                AppTheme.showError("Error", "Failed to delete student.");
            }
        }
    }

    private void refreshTable() {
        studentList = studentDAO.getAllStudents();
        table.setItems(studentList);
        table.refresh();
        updateStatusBar();
        departmentFilter.getItems().clear();
        departmentFilter.getItems().add("All Departments");
        departmentFilter.getItems().addAll(studentDAO.getAllDepartments());
        departmentFilter.setValue("All Departments");
    }

    private TextField createFormField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle("-fx-background-color: rgba(255,255,255,0.06); -fx-border-color: rgba(255,255,255,0.10); " +
                   "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 14; -fx-font-size: 13px; -fx-text-fill: #F8FAFC; -fx-prompt-text-fill: #64748B;");
        return f;
    }

    private Label formLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 12px; -fx-text-fill: #94A3B8; -fx-font-weight: bold;");
        return l;
    }
}
