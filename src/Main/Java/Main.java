/**
 * Launcher class for the Student Management System.
 * This is required for running JavaFX from a shaded/fat JAR.
 * It simply delegates to the actual Application class.
 */
public class Main {
    public static void main(String[] args) {
        com.student.Main.main(args);
    }
}