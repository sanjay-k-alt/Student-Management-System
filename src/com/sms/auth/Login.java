package com.sms.auth;

import com.sms.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JButton forgotPasswordButton;
    private final AuthService authService;

    public Login() {
        authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel("Login to Student Management System", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        
        signupButton = new JButton("Sign Up");
        signupButton.setBackground(new Color(34, 139, 34));
        signupButton.setForeground(Color.WHITE);
        
        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setBackground(new Color(128, 128, 128));
        forgotPasswordButton.setForeground(Color.WHITE);

        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        buttonPanel.add(forgotPasswordButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        loginButton.addActionListener(this::performLogin);
        signupButton.addActionListener(e -> openSignup());
        forgotPasswordButton.addActionListener(e -> openForgotPassword());

        add(mainPanel);
    }

    private void performLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authService.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
            openMainApplication();
            dispose(); // Close login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSignup() {
        new Signup().setVisible(true);
        this.dispose();
    }

    private void openForgotPassword() {
        new ForgotPassword().setVisible(true);
    }

    private void openMainApplication() {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(authService);
            mainFrame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}