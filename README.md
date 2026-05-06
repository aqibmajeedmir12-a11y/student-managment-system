<div align="center">

# 🎓 Student Management System

### Enterprise-Grade Desktop Application

[![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17-blue?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![SQLite](https://img.shields.io/badge/SQLite-3.42-green?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

A production-ready, enterprise-grade desktop application for academic institution management — featuring a **modern Glassmorphism UI**, **SQLite persistence**, and a **layered MVC architecture**.

**Developed by: Aqib Majeed Mir**

</div>

---

## 👨‍💻 Developer Profile

| | |
|---|---|
| **Name** | Aqib Majeed Mir |
| **GitHub** | [@aqibmajeedmir12-a11y](https://github.com/aqibmajeedmir12-a11y) |
| **Project Type** | Individual Full-Stack Desktop Application |
| **Duration** | Designed & developed as a showcase project |
| **Role** | Sole Developer — Architecture, Backend, Frontend, Database, UI/UX |

---

## 📌 Project Summary

This project demonstrates my ability to design and build a **complete, production-grade software application** from scratch. It showcases:

- **Software Architecture** — Clean layered MVC pattern with DAO, Model, UI, and Utility layers
- **Database Design** — Normalized relational schema with 6 tables, foreign keys, indexes, and audit logging
- **Modern UI/UX** — Premium dark glassmorphism design with animations, hover effects, and responsive layouts
- **Full-Stack Development** — End-to-end implementation from database to polished user interface
- **Software Engineering Practices** — Input validation, error handling, data export, separation of concerns

---

## ✨ Key Features Implemented

### 📊 Interactive Dashboard
- Real-time statistics cards (total students, active count, courses, GPA)
- Pie chart for student distribution by department
- Bar chart for enrollment status visualization
- Live activity feed from database audit log
- System information panel

### 🎓 Student Management (Full CRUD)
- **Create** — Add students with 12+ data fields and form validation
- **Read** — Searchable, filterable data table with color-coded GPA and status badges
- **Update** — Edit student information with duplicate email detection
- **Delete** — Safe deletion with confirmation dialogs
- Real-time search across name, email, phone, course, and department
- Filter by department and enrollment status simultaneously

### 📚 Course Management (Full CRUD)
- Add/Edit/Delete courses with code, name, credits, instructor, capacity
- Search courses by code, name, department, or instructor

### 📤 Data Export & Reporting
- **CSV Export** — Export all student data to CSV format with file chooser
- **Summary Report** — Generate formatted text reports with statistics

### 🎨 Premium UI/UX Design
- **Glassmorphism** — Frosted glass cards, translucent backgrounds, subtle borders
- **Dark Theme** — Full dark mode with carefully curated color palette
- **Animations** — Fade-in, slide-up, stagger effects on page transitions
- **Hover Effects** — Scale animations, glow effects, gradient transitions on buttons
- **Status Badges** — Color-coded enrollment status (Active/Inactive/Graduated/Suspended)
- **GPA Color Coding** — Green (≥3.5), Yellow (≥2.5), Red (<2.5)

### 🔒 Enterprise Backend
- SQLite database with WAL (Write-Ahead Logging) for performance
- Foreign key constraints for referential integrity
- Activity audit logging for all CRUD operations
- Duplicate email detection
- Graceful database shutdown on app close

---

## 🏗 Architecture & Design

### Layered Architecture

```
┌──────────────────────────────────────────────────┐
│                  UI Layer                         │
│  Main.java, DashboardView, StudentView,          │
│  CourseView, SettingsView, AppTheme              │
├──────────────────────────────────────────────────┤
│               DAO Layer                           │
│  StudentDAO (17 methods), CourseDAO (6 methods)  │
├──────────────────────────────────────────────────┤
│              Model Layer                          │
│  Student (15+ fields), Course (9 fields)         │
├──────────────────────────────────────────────────┤
│            Database Layer                         │
│  DatabaseManager — Singleton, WAL, Auto-Schema   │
├──────────────────────────────────────────────────┤
│            Utility Layer                          │
│  ValidationUtil, ExportUtil                      │
└──────────────────────────────────────────────────┘
```

### Design Patterns Used

| Pattern | Where | Why |
|---------|-------|-----|
| **Singleton** | `DatabaseManager` | Single connection instance across the app |
| **DAO Pattern** | `StudentDAO`, `CourseDAO` | Separation of data access from business logic |
| **MVC** | Model / View / Controller separation | Clean architecture, maintainability |
| **Factory** | `AppTheme` component methods | Consistent, reusable UI components |
| **Observer** | JavaFX property bindings | Reactive UI updates on data changes |

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 17+ | Core programming language |
| **JavaFX** | 17.0.2 | Desktop UI framework |
| **SQLite JDBC** | 3.42.0.0 | Embedded relational database |
| **Apache Maven** | 3.8+ | Build automation & dependency management |
| **JUnit 5** | 5.9.3 | Unit testing framework |
| **CSS** | Custom | Dark glassmorphism theme system |

---

## 📁 Project Structure

```
student-managment-system/
├── pom.xml                                    # Maven build configuration
└── src/
    ├── Main/
    │   ├── Java/
    │   │   ├── Main.java                      # JAR launcher
    │   │   └── com/student/
    │   │       ├── Main.java                  # Application entry point
    │   │       ├── database/
    │   │       │   └── DatabaseManager.java   # SQLite connection & schema
    │   │       ├── model/
    │   │       │   ├── Student.java           # Student entity (15+ fields)
    │   │       │   └── Course.java            # Course entity (9 fields)
    │   │       ├── dao/
    │   │       │   ├── StudentDAO.java        # 17 data access methods
    │   │       │   └── CourseDAO.java         # 6 data access methods
    │   │       ├── ui/
    │   │       │   ├── AppTheme.java          # Glassmorphism design system
    │   │       │   ├── DashboardView.java     # Dashboard with charts
    │   │       │   ├── StudentView.java       # Student CRUD interface
    │   │       │   ├── CourseView.java        # Course CRUD interface
    │   │       │   └── SettingsView.java      # System info & settings
    │   │       └── util/
    │   │           ├── ValidationUtil.java    # Input validation engine
    │   │           └── ExportUtil.java        # CSV & report generation
    │   └── resources/
    │       └── styles/
    │           └── dark-theme.css             # Complete dark theme stylesheet
```

---

## 🗄 Database Design

The application uses a **normalized relational schema** with 6 tables:

### Entity Relationship

| Table | Purpose | Key Relationships |
|-------|---------|-------------------|
| `students` | Student records (15 columns) | Referenced by attendance, grades, fees |
| `courses` | Academic courses (10 columns) | Referenced by attendance, grades |
| `attendance` | Daily attendance tracking | FK → students, FK → courses |
| `grades` | Exam/assignment scores | FK → students, FK → courses |
| `fees` | Financial records | FK → students |
| `activity_log` | Audit trail | System-generated logs |

### Key Database Features
- **Indexes** on email, department, status for optimized queries
- **Foreign keys** with CASCADE delete for referential integrity
- **CHECK constraints** for enum-type fields (gender, status, fee_type)
- **WAL mode** for concurrent read/write performance
- **Auto-schema generation** — tables created on first run

---

## 🚀 How to Run

### Prerequisites
- **Java JDK 17+** — [Download](https://adoptium.net/)
- **Apache Maven 3.8+** — [Download](https://maven.apache.org/download.cgi)

### Quick Start

```bash
# 1. Clone the repository
git clone https://github.com/aqibmajeedmir12-a11y/student-managment-system.git

# 2. Navigate to the project
cd student-managment-system

# 3. Build and run
mvn clean javafx:run
```

### Alternative: Build Standalone JAR

```bash
mvn clean package
java -jar target/StudentManagement-2.0.0.jar
```

> **Note:** The database is automatically created at `~/.student-management/student_management.db` on first launch. No additional configuration required.

---

## 📊 Code Metrics

| Metric | Value |
|--------|-------|
| Total Source Files | 14 Java files + 1 CSS |
| Lines of Code | ~3,500+ (Java) + ~350 (CSS) |
| Database Tables | 6 |
| DAO Methods | 23 |
| UI Views | 5 (Dashboard, Students, Courses, Settings, Theme) |
| Validation Rules | 8 (email, phone, name, GPA, semester, etc.) |
| Design Patterns | 5 (Singleton, DAO, MVC, Factory, Observer) |

---

## 🧪 Skills Demonstrated

| Skill Area | Implementation |
|-----------|---------------|
| **Object-Oriented Programming** | Clean class hierarchy, encapsulation, polymorphism |
| **Database Management** | Schema design, CRUD operations, prepared statements, transactions |
| **UI/UX Design** | Modern glassmorphism, responsive layouts, micro-animations |
| **Software Architecture** | Layered MVC, separation of concerns, design patterns |
| **Build & Tooling** | Maven build system, dependency management, fat JAR packaging |
| **Error Handling** | Input validation, exception handling, user-friendly error messages |
| **Data Structures** | ObservableList, HashMap, Collections, Property bindings |
| **CSS Styling** | 350+ lines of custom JavaFX CSS for complete dark theme |
| **Version Control** | Git, GitHub, proper project documentation |

---

## 🗺 Future Enhancements

These features are designed and partially implemented (database tables exist):

- [ ] 📅 **Attendance Module** — Mark daily attendance, track percentages
- [ ] 📝 **Grade Management** — Record exams, calculate CGPA
- [ ] 💰 **Fee Management** — Track payments, generate receipts
- [ ] 🔐 **Authentication** — Login/Logout with role-based access
- [ ] 📊 **PDF Reports** — Generate professional PDF documents
- [ ] 🎨 **Light/Dark Toggle** — Theme switching capability

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

<div align="center">

**Built with ❤️ by Aqib Majeed Mir**

*Java • JavaFX • SQLite • Maven*

⭐ If you found this project impressive, please star the repository!

</div>
