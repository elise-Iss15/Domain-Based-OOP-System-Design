package com.school.manager;

import com.school.model.SchoolUser;
import com.school.model.SchoolUser.Role;
import com.school.util.AppLogger;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PersistenceService {

    private static final Path DATA_DIR   = Paths.get(System.getProperty("user.home"), ".schoolmanagementsystem");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.properties");

    static {
        try { Files.createDirectories(DATA_DIR); }
        catch (IOException e) { throw new RuntimeException("Could not create data directory.", e); }
    }

    public Map<String, SchoolUser> loadUsers() {
        Map<String, SchoolUser> map = new LinkedHashMap<>();
        if (!Files.exists(USERS_FILE)) {
            seedDefaultAdmin(map);
            return map;
        }

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(USERS_FILE)) {
            props.load(in);
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to load users: " + e.getMessage());
            return map;
        }

        for (String username : props.stringPropertyNames()) {
            String[] parts = props.getProperty(username).split(":", -1);
            if (parts.length >= 3) {
                try {
                    SchoolUser user = new SchoolUser(
                            username,
                            parts[0],
                            Role.valueOf(parts[1]),
                            parts[2]
                    );
                    if (parts.length >= 4 && !parts[3].equals("null")) {
                        if (user.getRole() == Role.STUDENT)  user.setLinkedStudentId(parts[3]);
                        if (user.getRole() == Role.TEACHER)  user.setLinkedTeacherName(parts[3]);
                    }
                    map.put(username, user);
                } catch (Exception ignored) {}
            }
        }

        if (map.isEmpty()) seedDefaultAdmin(map);
        return map;
    }


    public void saveUsers(Map<String, SchoolUser> users) {
        Properties props = new Properties();
        users.forEach((u, user) -> {
            String linked = switch (user.getRole()) {
                case STUDENT -> (user.getLinkedStudentId() != null ? user.getLinkedStudentId() : "null");
                case TEACHER -> (user.getLinkedTeacherName() != null ? user.getLinkedTeacherName() : "null");
                default -> "null";
            };
            String value = String.join(":",
                    user.getHashedPassword(),
                    user.getRole().name(),
                    user.getFullName(),
                    linked
            );
            props.setProperty(u, value);
        });
        try (OutputStream out = Files.newOutputStream(USERS_FILE)) {
            props.store(out, "SchoolManagementSystem user accounts");
        } catch (IOException e) {
            AppLogger.log("ERROR", "Failed to save users: " + e.getMessage());
        }
    }

    private void seedDefaultAdmin(Map<String, SchoolUser> map) {
        // Default: admin / admin123
        SchoolUser admin = new SchoolUser(
                "admin",
                com.school.util.PasswordUtil.hash("admin123"),
                Role.ADMIN,
                "System Administrator"
        );
        map.put("admin", admin);
        saveUsers(map);
        AppLogger.log("INFO", "Default admin account created (admin / admin123).");
    }
}
