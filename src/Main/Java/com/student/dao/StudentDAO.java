package com.student.dao;

import com.student.database.DatabaseManager;
import com.student.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object for Student entity.
 * Handles all CRUD operations and queries against the students table.
 */
public class StudentDAO {

    private final DatabaseManager db;

    public StudentDAO() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Retrieve all students from the database.
     */
    public ObservableList<Student> getAllStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = "SELECT * FROM students ORDER BY id DESC";

        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Retrieve a single student by ID.
     */
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Insert a new student into the database.
     * Returns the generated ID, or -1 on failure.
     */
    public int addStudent(Student student) {
        String sql = """
            INSERT INTO students (first_name, last_name, email, phone, course, department,
                                  semester, date_of_birth, gender, address, enrollment_status, gpa)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setStudentParameters(pstmt, student);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    db.logActivity("CREATE", "STUDENT", newId,
                            "Added student: " + student.getFirstName() + " " + student.getLastName());
                    return newId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Update an existing student record.
     */
    public boolean updateStudent(Student student) {
        String sql = """
            UPDATE students SET first_name=?, last_name=?, email=?, phone=?, course=?,
                                department=?, semester=?, date_of_birth=?, gender=?, address=?,
                                enrollment_status=?, gpa=?, updated_at=datetime('now','localtime')
            WHERE id=?
        """;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            setStudentParameters(pstmt, student);
            pstmt.setInt(13, student.getId());
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                db.logActivity("UPDATE", "STUDENT", student.getId(),
                        "Updated student: " + student.getFullName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete a student by ID.
     */
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                db.logActivity("DELETE", "STUDENT", id, "Deleted student ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
        return false;
    }

    /**
     * Search students by keyword across multiple fields.
     */
    public ObservableList<Student> searchStudents(String keyword) {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = """
            SELECT * FROM students
            WHERE first_name LIKE ? OR last_name LIKE ? OR email LIKE ?
               OR course LIKE ? OR department LIKE ? OR phone LIKE ?
            ORDER BY id DESC
        """;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            for (int i = 1; i <= 6; i++) {
                pstmt.setString(i, pattern);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Filter students by department.
     */
    public ObservableList<Student> getStudentsByDepartment(String department) {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = "SELECT * FROM students WHERE department = ? ORDER BY id DESC";

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error filtering by department: " + e.getMessage());
        }
        return students;
    }

    /**
     * Filter students by enrollment status.
     */
    public ObservableList<Student> getStudentsByStatus(String status) {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = "SELECT * FROM students WHERE enrollment_status = ? ORDER BY id DESC";

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error filtering by status: " + e.getMessage());
        }
        return students;
    }

    /**
     * Get total student count.
     */
    public int getTotalCount() {
        return getCount("SELECT COUNT(*) FROM students");
    }

    /**
     * Get active student count.
     */
    public int getActiveCount() {
        return getCount("SELECT COUNT(*) FROM students WHERE enrollment_status = 'ACTIVE'");
    }

    /**
     * Get graduated student count.
     */
    public int getGraduatedCount() {
        return getCount("SELECT COUNT(*) FROM students WHERE enrollment_status = 'GRADUATED'");
    }

    /**
     * Get count of students per department.
     */
    public Map<String, Integer> getStudentCountByDepartment() {
        Map<String, Integer> counts = new HashMap<>();
        String sql = "SELECT department, COUNT(*) as cnt FROM students GROUP BY department ORDER BY cnt DESC";

        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                counts.put(rs.getString("department"), rs.getInt("cnt"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting department counts: " + e.getMessage());
        }
        return counts;
    }

    /**
     * Get count of students per enrollment status.
     */
    public Map<String, Integer> getStudentCountByStatus() {
        Map<String, Integer> counts = new HashMap<>();
        String sql = "SELECT enrollment_status, COUNT(*) as cnt FROM students GROUP BY enrollment_status";

        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                counts.put(rs.getString("enrollment_status"), rs.getInt("cnt"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting status counts: " + e.getMessage());
        }
        return counts;
    }

    /**
     * Get average GPA across all active students.
     */
    public double getAverageGpa() {
        String sql = "SELECT AVG(gpa) FROM students WHERE enrollment_status = 'ACTIVE' AND gpa > 0";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return Math.round(rs.getDouble(1) * 100.0) / 100.0;
            }
        } catch (SQLException e) {
            System.err.println("Error getting average GPA: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Get all distinct departments.
     */
    public ObservableList<String> getAllDepartments() {
        ObservableList<String> departments = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT department FROM students ORDER BY department";

        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departments.add(rs.getString("department"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting departments: " + e.getMessage());
        }
        return departments;
    }

    /**
     * Check if email already exists (for validation).
     */
    public boolean emailExists(String email, int excludeId) {
        String sql = "SELECT COUNT(*) FROM students WHERE email = ? AND id != ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setInt(2, excludeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
        }
        return false;
    }

    // ===== Private Helper Methods =====

    private int getCount(String sql) {
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error getting count: " + e.getMessage());
        }
        return 0;
    }

    private void setStudentParameters(PreparedStatement pstmt, Student student) throws SQLException {
        pstmt.setString(1, student.getFirstName());
        pstmt.setString(2, student.getLastName());
        pstmt.setString(3, student.getEmail());
        pstmt.setString(4, student.getPhone());
        pstmt.setString(5, student.getCourse());
        pstmt.setString(6, student.getDepartment());
        pstmt.setInt(7, student.getSemester());
        pstmt.setString(8, student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null);
        pstmt.setString(9, student.getGender());
        pstmt.setString(10, student.getAddress());
        pstmt.setString(11, student.getEnrollmentStatus());
        pstmt.setDouble(12, student.getGpa());
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        LocalDate dob = rs.getString("date_of_birth") != null ?
                LocalDate.parse(rs.getString("date_of_birth")) : null;
        LocalDateTime created = rs.getString("created_at") != null ?
                LocalDateTime.parse(rs.getString("created_at").replace(" ", "T")) : null;
        LocalDateTime updated = rs.getString("updated_at") != null ?
                LocalDateTime.parse(rs.getString("updated_at").replace(" ", "T")) : null;

        return new Student(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("course"),
                rs.getString("department"),
                rs.getInt("semester"),
                dob,
                rs.getString("gender"),
                rs.getString("address"),
                rs.getString("enrollment_status"),
                rs.getDouble("gpa"),
                created,
                updated
        );
    }
}
