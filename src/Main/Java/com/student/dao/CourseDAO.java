package com.student.dao;

import com.student.database.DatabaseManager;
import com.student.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Data Access Object for Course entity.
 */
public class CourseDAO {

    private final DatabaseManager db;

    public CourseDAO() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Retrieve all courses.
     */
    public ObservableList<Course> getAllCourses() {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String sql = "SELECT * FROM courses ORDER BY course_code";

        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching courses: " + e.getMessage());
        }
        return courses;
    }

    /**
     * Insert a new course.
     */
    public int addCourse(Course course) {
        String sql = """
            INSERT INTO courses (course_code, course_name, department, credits, instructor, max_capacity, description)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getDepartment());
            pstmt.setInt(4, course.getCredits());
            pstmt.setString(5, course.getInstructor());
            pstmt.setInt(6, course.getMaxCapacity());
            pstmt.setString(7, course.getDescription());
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    db.logActivity("CREATE", "COURSE", newId, "Added course: " + course.getDisplayName());
                    return newId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Update an existing course.
     */
    public boolean updateCourse(Course course) {
        String sql = """
            UPDATE courses SET course_code=?, course_name=?, department=?, credits=?,
                               instructor=?, max_capacity=?, description=?
            WHERE id=?
        """;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getDepartment());
            pstmt.setInt(4, course.getCredits());
            pstmt.setString(5, course.getInstructor());
            pstmt.setInt(6, course.getMaxCapacity());
            pstmt.setString(7, course.getDescription());
            pstmt.setInt(8, course.getId());
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                db.logActivity("UPDATE", "COURSE", course.getId(), "Updated course: " + course.getDisplayName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete a course by ID.
     */
    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                db.logActivity("DELETE", "COURSE", id, "Deleted course ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get total course count.
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM courses";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error getting course count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Search courses by keyword.
     */
    public ObservableList<Course> searchCourses(String keyword) {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String sql = """
            SELECT * FROM courses
            WHERE course_code LIKE ? OR course_name LIKE ? OR department LIKE ? OR instructor LIKE ?
            ORDER BY course_code
        """;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) {
                pstmt.setString(i, pattern);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching courses: " + e.getMessage());
        }
        return courses;
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        LocalDateTime created = rs.getString("created_at") != null ?
                LocalDateTime.parse(rs.getString("created_at").replace(" ", "T")) : null;

        return new Course(
                rs.getInt("id"),
                rs.getString("course_code"),
                rs.getString("course_name"),
                rs.getString("department"),
                rs.getInt("credits"),
                rs.getString("instructor"),
                rs.getInt("max_capacity"),
                rs.getInt("enrolled_count"),
                rs.getString("description"),
                created
        );
    }
}
