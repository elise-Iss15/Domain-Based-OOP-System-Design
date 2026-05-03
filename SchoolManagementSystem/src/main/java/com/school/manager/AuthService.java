package com.school.manager;

import com.school.model.SchoolUser;
import com.school.model.SchoolUser.Role;
import com.school.util.InputValidator;
import com.school.util.PasswordUtil;
import com.school.util.AppLogger;

import java.util.Map;


public class AuthService {

    private final PersistenceService persistence;
    private final Map<String, SchoolUser> users;

    public AuthService(PersistenceService persistence) {
        this.persistence = persistence;
        this.users = persistence.loadUsers();
    }

    /**
     * Register a new account.
     * Validates username & password, checks for duplicate usernames.
     */
    public SchoolUser signup(String username, String password, Role role, String fullName) {
        String validUsername = InputValidator.validateUsername(username);
        String validPassword = InputValidator.validatePassword(password);
        String validFullName = InputValidator.validateName(fullName, "Full name");

        if (users.containsKey(validUsername.toLowerCase()))
            throw new IllegalArgumentException("Username '" + validUsername + "' is already taken.");

        SchoolUser user = new SchoolUser(
                validUsername.toLowerCase(),
                PasswordUtil.hash(validPassword),
                role,
                validFullName
        );

        users.put(user.getUsername(), user);
        persistence.saveUsers(users);
        AppLogger.log("INFO", "New account registered: " + user.getUsername() + " [" + role + "]");
        return user;
    }

    /**
     * Authenticate an existing user.
     */
    public SchoolUser login(String username, String password) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty.");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty.");

        SchoolUser user = users.get(username.trim().toLowerCase());
        if (user == null || !PasswordUtil.matches(password, user.getHashedPassword()))
            throw new IllegalArgumentException("Invalid username or password.");

        AppLogger.log("INFO", "Login: " + user.getUsername() + " [" + user.getRole() + "]");
        return user;
    }

    public Map<String, SchoolUser> getUsers() {
        return users;
    }
}
