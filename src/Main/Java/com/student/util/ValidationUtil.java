package com.student.util;

import java.util.regex.Pattern;

/**
 * Input validation utility class.
 * Provides enterprise-grade validation for all form inputs.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[+]?[0-9\\-\\s]{7,15}$");

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z\\s'-]{2,50}$");

    /**
     * Validate that a string is not null or empty (after trimming).
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validate email format.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number format.
     */
    public static boolean isValidPhone(String phone) {
        return phone == null || phone.trim().isEmpty() || PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validate name (only letters, spaces, hyphens, apostrophes).
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validate GPA range (0.0 to 4.0).
     */
    public static boolean isValidGpa(String gpaStr) {
        try {
            double gpa = Double.parseDouble(gpaStr);
            return gpa >= 0.0 && gpa <= 4.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate semester range (1 to 8).
     */
    public static boolean isValidSemester(String semStr) {
        try {
            int sem = Integer.parseInt(semStr);
            return sem >= 1 && sem <= 8;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Build a validation error message for student form.
     */
    public static String validateStudentForm(String firstName, String lastName, String email,
                                              String phone, String course, String department,
                                              String semester, String gpa) {
        StringBuilder errors = new StringBuilder();

        if (!isNotEmpty(firstName) || !isValidName(firstName)) {
            errors.append("• First name is required (letters only, 2-50 characters)\n");
        }
        if (!isNotEmpty(lastName) || !isValidName(lastName)) {
            errors.append("• Last name is required (letters only, 2-50 characters)\n");
        }
        if (!isNotEmpty(email) || !isValidEmail(email)) {
            errors.append("• Valid email address is required\n");
        }
        if (!isValidPhone(phone)) {
            errors.append("• Phone number format is invalid\n");
        }
        if (!isNotEmpty(course)) {
            errors.append("• Course is required\n");
        }
        if (!isNotEmpty(department)) {
            errors.append("• Department is required\n");
        }
        if (isNotEmpty(semester) && !isValidSemester(semester)) {
            errors.append("• Semester must be between 1 and 8\n");
        }
        if (isNotEmpty(gpa) && !isValidGpa(gpa)) {
            errors.append("• GPA must be between 0.0 and 4.0\n");
        }

        return errors.toString();
    }
}
