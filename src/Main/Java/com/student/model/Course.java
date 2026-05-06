package com.student.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Course model class representing a course/program offered by the institution.
 */
public class Course {

    private final IntegerProperty id;
    private final StringProperty courseCode;
    private final StringProperty courseName;
    private final StringProperty department;
    private final IntegerProperty credits;
    private final StringProperty instructor;
    private final IntegerProperty maxCapacity;
    private final IntegerProperty enrolledCount;
    private final StringProperty description;
    private final ObjectProperty<LocalDateTime> createdAt;

    public Course(int id, String courseCode, String courseName, String department,
                  int credits, String instructor, int maxCapacity, int enrolledCount,
                  String description, LocalDateTime createdAt) {
        this.id = new SimpleIntegerProperty(id);
        this.courseCode = new SimpleStringProperty(courseCode);
        this.courseName = new SimpleStringProperty(courseName);
        this.department = new SimpleStringProperty(department);
        this.credits = new SimpleIntegerProperty(credits);
        this.instructor = new SimpleStringProperty(instructor);
        this.maxCapacity = new SimpleIntegerProperty(maxCapacity);
        this.enrolledCount = new SimpleIntegerProperty(enrolledCount);
        this.description = new SimpleStringProperty(description);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
    }

    public Course(String courseCode, String courseName, String department,
                  int credits, String instructor, int maxCapacity, String description) {
        this(0, courseCode, courseName, department, credits, instructor, maxCapacity, 0,
             description, LocalDateTime.now());
    }

    // ===== ID =====
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    // ===== Course Code =====
    public String getCourseCode() { return courseCode.get(); }
    public void setCourseCode(String code) { this.courseCode.set(code); }
    public StringProperty courseCodeProperty() { return courseCode; }

    // ===== Course Name =====
    public String getCourseName() { return courseName.get(); }
    public void setCourseName(String name) { this.courseName.set(name); }
    public StringProperty courseNameProperty() { return courseName; }

    // ===== Department =====
    public String getDepartment() { return department.get(); }
    public void setDepartment(String dept) { this.department.set(dept); }
    public StringProperty departmentProperty() { return department; }

    // ===== Credits =====
    public int getCredits() { return credits.get(); }
    public void setCredits(int credits) { this.credits.set(credits); }
    public IntegerProperty creditsProperty() { return credits; }

    // ===== Instructor =====
    public String getInstructor() { return instructor.get(); }
    public void setInstructor(String instructor) { this.instructor.set(instructor); }
    public StringProperty instructorProperty() { return instructor; }

    // ===== Max Capacity =====
    public int getMaxCapacity() { return maxCapacity.get(); }
    public void setMaxCapacity(int cap) { this.maxCapacity.set(cap); }
    public IntegerProperty maxCapacityProperty() { return maxCapacity; }

    // ===== Enrolled Count =====
    public int getEnrolledCount() { return enrolledCount.get(); }
    public void setEnrolledCount(int count) { this.enrolledCount.set(count); }
    public IntegerProperty enrolledCountProperty() { return enrolledCount; }

    // ===== Description =====
    public String getDescription() { return description.get(); }
    public void setDescription(String desc) { this.description.set(desc); }
    public StringProperty descriptionProperty() { return description; }

    // ===== Timestamps =====
    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }

    // ===== Computed =====
    public int getAvailableSeats() { return getMaxCapacity() - getEnrolledCount(); }
    public String getDisplayName() { return getCourseCode() + " - " + getCourseName(); }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
