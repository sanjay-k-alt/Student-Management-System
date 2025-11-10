package com.sms.auth;

import com.sms.dao.UserDAO;
import com.sms.model.User;

public class AuthService {
    private UserDAO userDAO;
    private User currentUser;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean login(String username, String password) {
        User user = userDAO.authenticateUser(username, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public boolean register(String username, String email, String password, String role) {
        if (userDAO.userExists(username, email)) {
            return false; // User already exists
        }
        
        User newUser = new User(username, email, password, role);
        return userDAO.registerUser(newUser);
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public boolean requestPasswordReset(String email) {
        String token = userDAO.createPasswordResetToken(email);
        return token != null;
    }

    public boolean resetPassword(String token, String newPassword) {
        return userDAO.resetPasswordWithToken(token, newPassword);
    }

    public boolean validateResetToken(String token) {
        return userDAO.validateResetToken(token);
    }
}