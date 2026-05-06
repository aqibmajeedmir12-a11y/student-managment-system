package com.student.model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Student model class representing a student entity in the system.
 * Uses JavaFX properties for seamless TableView binding.
 */
public class Student {

    private final IntegerProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty course;
    private final StringProperty department;
    private final IntegerProperty semester;
    private final ObjectProperty<LocalDate> dateOfBirth;
    private final StringProperty gender;
    private final StringProperty address;
    private final StringProperty enrollmentStatus; // ACTIVE, INACTIVE, GRADUATED, SUSPENDED
    private final DoubleProperty gpa;
    private final ObjectProperty<LocalDateTime> createdAt;
    private final ObjectProperty<LocalDateTime> updatedAt;

    /**
     * Full constructor for creating a Student with all fields.
     */
    public Student(int id, String firstName, String lastName, String email, String phone,
                   String course, String department, int semester, LocalDate dateOfBirth,
                   String gender, String address, String enrollmentStatus, double gpa,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.course = new SimpleStringProperty(course);
        this.department = new SimpleStringProperty(department);
        this.semester = new SimpleIntegerProperty(semester);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
        this.gender = new SimpleStringProperty(gender);
        this.address = new SimpleStringProperty(address);
        this.enrollmentStatus = new SimpleStringProperty(enrollmentStatus);
        this.gpa = new SimpleDoubleProperty(gpa);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
        this.updatedAt = new SimpleObjectProperty<>(updatedAt);
    }

    /**
     * Simplified constructor for quick creation (auto-generates timestamps).
     */
    public Student(String firstName, String lastName, String email, String phone,
                   String course, String department, int semester, LocalDate dateOfBirth,
                   String gender, String address) {
        this(0, firstName, lastName, email, phone, course, department, semester,
             dateOfBirth, gender, address, "ACTIVE", 0.0,
             LocalDateTime.now(), LocalDateTime.now());
    }

    // ===== ID =====
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    // ===== First Name =====
    public String getFirstName() { return firstName.get(); }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public StringProperty firstNameProperty() { return firstName; }

    // ===== Last Name =====
    public String getLastName() { return lastName.get(); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    public StringProperty lastNameProperty() { return lastName; }

    // ===== Full Name (computed) =====
    public String getFullName() { return getFirstName() + " " + getLastName(); }

    // ===== Email =====
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    // ===== Phone =====
    public String getPhone() { return phone.get(); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public StringProperty phoneProperty() { return phone; }

    // ===== Course =====
    public String getCourse() { return course.get(); }
    public void setCourse(String course) { this.course.set(course); }
    public StringProperty courseProperty() { return course; }

    // ===== Department =====
    public String getDepartment() { return department.get(); }
    public void setDepartment(String department) { this.department.set(department); }
    public StringProperty departmentProperty() { return department; }

    // ===== Semester =====
    public int getSemester() { return semester.get(); }
    public void setSemester(int semester) { this.semester.set(semester); }
    public IntegerProperty semesterProperty() { return semester; }

    // ===== Date of Birth =====
    public LocalDate getDateOfBirth() { return dateOfBirth.get(); }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth.set(dateOfBirth); }
    public ObjectProperty<LocalDate> dateOfBirthProperty() { return dateOfBirth; }

    // ===== Gender =====
    public String getGender() { return gender.get(); }
    public void setGender(String gender) { this.gender.set(gender); }
    public StringProperty genderProperty() { return gender; }

    // ===== Address =====
    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address.set(address); }
    public StringProperty addressProperty() { return address; }

    // ===== Enrollment Status =====
    public String getEnrollmentStatus() { return enrollmentStatus.get(); }
    public void setEnrollmentStatus(String status) { this.enrollmentStatus.set(status); }
    public StringProperty enrollmentStatusProperty() { return enrollmentStatus; }

    // ===== GPA =====
    public double getGpa() { return gpa.get(); }
    public void setGpa(double gpa) { this.gpa.set(gpa); }
    public DoubleProperty gpaProperty() { return gpa; }

    // ===== Timestamps =====
    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt.get(); }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt.set(updatedAt); }
    public ObjectProperty<LocalDateTime> updatedAtProperty() { return updatedAt; }

    // ===== Formatted Date Strings =====
    public String getFormattedDob() {
        if (dateOfBirth.get() == null) return "";
        return dateOfBirth.get().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public String getFormattedCreatedAt() {
        if (createdAt.get() == null) return "";
        return createdAt.get().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("Student[id=%d, name=%s %s, email=%s, course=%s, dept=%s, status=%s]",
                getId(), getFirstName(), getLastName(), getEmail(), getCourse(),
                getDepartment(), getEnrollmentStatus());
    }
}
