package com.student.database;

import java.sql.*;
import java.io.File;

/**
 * Singleton database connection manager for SQLite.
 * Handles connection lifecycle, database initialization, and schema management.
 */
public class DatabaseManager {

    private static final String DB_NAME = "student_management.db";
    private static final String DB_PATH;
    private static DatabaseManager instance;
    private Connection connection;

    static {
        String userHome = System.getProperty("user.home");
        String appDir = userHome + File.separator + ".student-management";
        new File(appDir).mkdirs();
        DB_PATH = "jdbc:sqlite:" + appDir + File.separator + DB_NAME;
    }

    private DatabaseManager() {
        connect();
        initializeTables();
    }

    /**
     * Get the singleton instance of DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Get the active database connection.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection: " + e.getMessage());
            connect();
        }
        return connection;
    }

    /**
     * Establish connection to SQLite database.
     */
    private void connect() {
        try {
            connection = DriverManager.getConnection(DB_PATH);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;");
                stmt.execute("PRAGMA foreign_keys=ON;");
            }
            System.out.println("Database connected: " + DB_PATH);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    /**
     * Initialize all database tables with proper schema.
     */
    private void initializeTables() {
        try (Statement stmt = connection.createStatement()) {

            // Students Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    phone TEXT,
                    course TEXT NOT NULL,
                    department TEXT NOT NULL,
                    semester INTEGER DEFAULT 1,
                    date_of_birth TEXT,
                    gender TEXT CHECK(gender IN ('Male', 'Female', 'Other')),
                    address TEXT,
                    enrollment_status TEXT DEFAULT 'ACTIVE'
                        CHECK(enrollment_status IN ('ACTIVE', 'INACTIVE', 'GRADUATED', 'SUSPENDED')),
                    gpa REAL DEFAULT 0.0,
                    created_at TEXT DEFAULT (datetime('now', 'localtime')),
                    updated_at TEXT DEFAULT (datetime('now', 'localtime'))
                );
            """);

            // Courses Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS courses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    course_code TEXT UNIQUE NOT NULL,
                    course_name TEXT NOT NULL,
                    department TEXT NOT NULL,
                    credits INTEGER DEFAULT 3,
                    instructor TEXT,
                    max_capacity INTEGER DEFAULT 60,
                    enrolled_count INTEGER DEFAULT 0,
                    description TEXT,
                    created_at TEXT DEFAULT (datetime('now', 'localtime'))
                );
            """);

            // Attendance Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS attendance (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id INTEGER NOT NULL,
                    course_id INTEGER NOT NULL,
                    date TEXT NOT NULL,
                    status TEXT DEFAULT 'PRESENT'
                        CHECK(status IN ('PRESENT', 'ABSENT', 'LATE', 'EXCUSED')),
                    remarks TEXT,
                    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
                );
            """);

            // Grades Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS grades (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id INTEGER NOT NULL,
                    course_id INTEGER NOT NULL,
                    exam_type TEXT NOT NULL CHECK(exam_type IN ('MIDTERM', 'FINAL', 'ASSIGNMENT', 'PROJECT', 'QUIZ')),
                    marks_obtained REAL NOT NULL,
                    total_marks REAL NOT NULL,
                    grade_letter TEXT,
                    date TEXT DEFAULT (date('now', 'localtime')),
                    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
                );
            """);

            // Fees Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS fees (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id INTEGER NOT NULL,
                    fee_type TEXT NOT NULL CHECK(fee_type IN ('TUITION', 'LIBRARY', 'LAB', 'HOSTEL', 'EXAM', 'OTHER')),
                    amount REAL NOT NULL,
                    paid_amount REAL DEFAULT 0.0,
                    due_date TEXT NOT NULL,
                    paid_date TEXT,
                    status TEXT DEFAULT 'PENDING'
                        CHECK(status IN ('PENDING', 'PAID', 'OVERDUE', 'PARTIAL')),
                    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
                );
            """);

            // Activity Log Table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS activity_log (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    action TEXT NOT NULL,
                    entity_type TEXT NOT NULL,
                    entity_id INTEGER,
                    description TEXT,
                    timestamp TEXT DEFAULT (datetime('now', 'localtime'))
                );
            """);

            // Indexes
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_students_email ON students(email);");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_students_dept ON students(department);");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_students_status ON students(enrollment_status);");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_attendance_student ON attendance(student_id);");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_grades_student ON grades(student_id);");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_fees_student ON fees(student_id);");

            System.out.println("Database tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Failed to initialize tables: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Log an activity to the activity_log table.
     */
    public void logActivity(String action, String entityType, int entityId, String description) {
        String sql = "INSERT INTO activity_log (action, entity_type, entity_id, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, action);
            pstmt.setString(2, entityType);
            pstmt.setInt(3, entityId);
            pstmt.setString(4, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to log activity: " + e.getMessage());
        }
    }

    /**
     * Get recent activity logs.
     */
    public java.util.List<String[]> getRecentActivity(int limit) {
        java.util.List<String[]> activities = new java.util.ArrayList<>();
        String sql = "SELECT action, entity_type, description, timestamp FROM activity_log ORDER BY id DESC LIMIT ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                activities.add(new String[]{
                    rs.getString("action"),
                    rs.getString("entity_type"),
                    rs.getString("description"),
                    rs.getString("timestamp")
                });
            }
        } catch (SQLException e) {
            System.err.println("Failed to get activity: " + e.getMessage());
        }
        return activities;
    }

    /**
     * Close the database connection.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}
