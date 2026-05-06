package com.student.util;

import com.student.model.Student;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Export utility for generating CSV and text reports from student data.
 */
public class ExportUtil {

    /**
     * Export students to CSV file with file chooser dialog.
     */
    public static boolean exportToCSV(ObservableList<Student> students, Stage ownerStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Students to CSV");
        fileChooser.setInitialFileName("students_export_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(ownerStage);
        if (file == null) return false;

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            // Header
            writer.println("ID,First Name,Last Name,Email,Phone,Course,Department,Semester,Date of Birth,Gender,Address,Status,GPA,Created At");

            // Data rows
            for (Student s : students) {
                writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\"%n",
                        s.getId(),
                        escapeCsv(s.getFirstName()),
                        escapeCsv(s.getLastName()),
                        escapeCsv(s.getEmail()),
                        escapeCsv(s.getPhone()),
                        escapeCsv(s.getCourse()),
                        escapeCsv(s.getDepartment()),
                        s.getSemester(),
                        s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : "",
                        escapeCsv(s.getGender()),
                        escapeCsv(s.getAddress()),
                        s.getEnrollmentStatus(),
                        s.getGpa(),
                        s.getFormattedCreatedAt());
            }

            System.out.println("✅ Exported " + students.size() + " students to: " + file.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.err.println("❌ Export failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Export a summary report to text file.
     */
    public static boolean exportSummaryReport(ObservableList<Student> students, int totalCourses,
                                               double avgGpa, Stage ownerStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Summary Report");
        fileChooser.setInitialFileName("summary_report_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showSaveDialog(ownerStage);
        if (file == null) return false;

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            writer.println("╔══════════════════════════════════════════════════════╗");
            writer.println("║        STUDENT MANAGEMENT SYSTEM - SUMMARY REPORT   ║");
            writer.println("╚══════════════════════════════════════════════════════╝");
            writer.println();
            writer.println("Generated: " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a")));
            writer.println("─".repeat(55));
            writer.println();

            // Statistics
            long active = students.stream().filter(s -> "ACTIVE".equals(s.getEnrollmentStatus())).count();
            long graduated = students.stream().filter(s -> "GRADUATED".equals(s.getEnrollmentStatus())).count();
            long suspended = students.stream().filter(s -> "SUSPENDED".equals(s.getEnrollmentStatus())).count();

            writer.println("📊 STATISTICS");
            writer.println("  Total Students     : " + students.size());
            writer.println("  Active Students    : " + active);
            writer.println("  Graduated Students : " + graduated);
            writer.println("  Suspended Students : " + suspended);
            writer.println("  Total Courses      : " + totalCourses);
            writer.println("  Average GPA        : " + String.format("%.2f", avgGpa));
            writer.println();

            // Student List
            writer.println("📋 STUDENT LIST");
            writer.println("─".repeat(55));
            writer.printf("%-5s %-20s %-25s %-10s %-5s%n", "ID", "Name", "Email", "Status", "GPA");
            writer.println("─".repeat(55));

            for (Student s : students) {
                writer.printf("%-5d %-20s %-25s %-10s %-5.2f%n",
                        s.getId(),
                        truncate(s.getFullName(), 20),
                        truncate(s.getEmail(), 25),
                        s.getEnrollmentStatus(),
                        s.getGpa());
            }

            writer.println("─".repeat(55));
            writer.println("\n© Student Management System v2.0");

            return true;

        } catch (IOException e) {
            System.err.println("❌ Report export failed: " + e.getMessage());
            return false;
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }

    private static String truncate(String value, int maxLen) {
        if (value == null) return "";
        return value.length() > maxLen ? value.substring(0, maxLen - 2) + ".." : value;
    }
}
