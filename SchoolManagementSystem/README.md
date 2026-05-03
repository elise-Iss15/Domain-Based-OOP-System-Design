# 🏫 SchoolManagementSystem

A JavaFX Maven application that integrates a fully designed backend school management system.  
**Renamed** from `SchoolSystemFX` → `SchoolManagementSystem`  
**Improved** with patterns from the `HealthSystem` reference implementation.

---

## ✨ What's New (Improvements from HealthSystem)

| Feature | Before (SchoolSystemFX) | After (SchoolManagementSystem) |
|---|---|---|
| **Authentication** | No login — opens dashboard directly | Login / Register screen (FXML-based) |
| **Role-Based Views** | Single view for all users | ADMIN / TEACHER / STUDENT roles with tailored UI |
| **Input Validation** | Basic null checks only | `InputValidator` — regex-validated names, IDs, ages, scores |
| **Password Security** | No password system | `PasswordUtil` — SHA-256 hashed passwords |
| **User Persistence** | No user accounts | `PersistenceService` — saves accounts to `~/.schoolmanagementsystem/users.properties` |
| **Auth Service** | N/A | `AuthService` — signup / login with duplicate detection |
| **Logout** | N/A | Logout button navigates back to auth screen |
| **User Session** | N/A | Session passed from Auth → Main via `SchoolUser` object |
| **CSS Theming** | Basic dark theme | Enhanced dark GitHub-inspired theme |
| **FXML** | Programmatic UI only | Auth screen uses `auth-view.fxml` |
| **pom.xml** | Java 17 | Upgraded to Java 21, JUnit 5 included |

---

## 📦 Project Structure

```
SchoolManagementSystem/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   ├── module-info.java
        │   └── com/school/
        │       ├── ui/
        │       │   └── MainApp.java            ← JavaFX entry point (now launches Auth)
        │       ├── controller/
        │       │   ├── AuthController.java     ← NEW: Login/Register UI logic
        │       │   └── MainController.java     ← Updated: role-aware dashboard
        │       ├── manager/
        │       │   ├── SchoolManager.java      ← Central backend manager
        │       │   ├── FileManager.java        ← File I/O layer
        │       │   ├── Repository.java         ← Generics layer
        │       │   ├── AuthService.java        ← NEW: signup/login
        │       │   └── PersistenceService.java ← NEW: user account persistence
        │       ├── model/
        │       │   ├── Person.java             ← Abstract OOP base
        │       │   ├── Student.java            ← OOP + Collections
        │       │   ├── Teacher.java            ← OOP + Collections
        │       │   ├── Course.java             ← OOP + Collections
        │       │   └── SchoolUser.java         ← NEW: auth entity with roles
        │       ├── exception/
        │       │   ├── SchoolSystemException.java
        │       │   ├── StudentNotFoundException.java
        │       │   ├── InvalidAgeException.java
        │       │   ├── InvalidGradeException.java
        │       │   └── DuplicateEnrollmentException.java
        │       ├── component/
        │       │   ├── StatusBar.java          ← Auto-clearing status messages
        │       │   └── FormField.java          ← Reusable labeled input
        │       └── util/
        │           ├── AppLogger.java          ← Logging to data/app.log
        │           ├── InputValidator.java     ← NEW: regex input validation
        │           └── PasswordUtil.java       ← NEW: SHA-256 hashing
        └── resources/
            └── com/school/
                ├── fxml/
                │   └── auth-view.fxml          ← NEW: Login/Register FXML
                └── css/
                    └── styles.css              ← Enhanced dark theme
```

---

## 🔑 Default Credentials

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | ADMIN |

_(Created automatically on first launch)_

---

## 👤 Role Permissions

| Feature | ADMIN | TEACHER | STUDENT |
|---|---|---|---|
| Add/Remove Students | ✅ | ❌ | ❌ |
| Add/Remove Teachers | ✅ | ❌ | ❌ |
| Manage Courses | ✅ | ❌ | ❌ |
| Enroll Students | ✅ | ❌ | ❌ |
| Grade Students | ✅ | ✅ (own records) | ❌ |
| View Own Report | ✅ | ❌ | ✅ |
| View My Courses | N/A | ✅ | ❌ |
| Dashboard | ✅ | ✅ | ✅ |

---

## ▶️ Running the Application

```bash
mvn javafx:run
```

Or via your IDE (IntelliJ / Eclipse) with JavaFX 21 configured.

---

## 📋 Assignment Requirements Met

1. ✅ **JavaFX Maven Setup** — valid pom.xml, JavaFX 21 dependencies, clean structure
2. ✅ **JavaFX UI Layer** — input fields, Add/Remove/Search/Sort buttons, TableView, ListView, Labels
3. ✅ **Backend Integration** — OOP, Generics, Collections, Exception Handling, File I/O
4. ✅ **Controller Integration** — AuthController + MainController bridge UI ↔ backend
5. ✅ **JavaFX Thread Rule** — `Platform.runLater()` used for all async UI updates
6. ✅ **Expected Behavior** — real-time updates, graceful error handling, role-based views
7. ✅ **Logging** — `AppLogger` writes to `data/app.log`
8. ✅ **Folder structure** — `/ui`, `/controller`, `/manager`, `/model`, `/component`, `/util`, `/exception`
9. ✅ **Bonus** — Auth system, role-based access, SHA-256 password hashing, InputValidator

---

## 🗂️ Data Files

All data is stored in the `data/` directory (created automatically):
- `data/teachers.txt` — teacher records
- `data/students.txt` — student records
- `data/courses.txt` — course and enrollment data
- `data/marks.txt` — formative and summative grades
- `data/attendance.txt` — attendance records
- `data/app.log` — application log

User accounts are stored in `~/.schoolmanagementsystem/users.properties`.

---

## 🌿 Branches

- `main` — stable release
- `dev` — development / feature work
