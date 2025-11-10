package com.sms.auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ForgotPassword extends JDialog {
    private JTextField emailField;
    private JTextField tokenField;
    private JPasswordField newPasswordField;
    private JButton requestTokenButton;
    private JButton resetPasswordButton;
    private JButton cancelButton;
    private AuthService authService;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public ForgotPassword() {
        authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Reset Password");
        setModal(true);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Request token panel
        cardPanel.add(createRequestTokenPanel(), "REQUEST");
        
        // Reset password panel
        cardPanel.add(createResetPasswordPanel(), "RESET");

        add(cardPanel);
    }

    private JPanel createRequestTokenPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel instructionLabel = new JLabel("Enter your email to receive reset instructions:");
        panel.add(instructionLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        requestTokenButton = new JButton("Send Reset Link");
        requestTokenButton.setBackground(new Color(70, 130, 180));
        requestTokenButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(128, 128, 128));
        cancelButton.setForeground(Color.WHITE);

        buttonPanel.add(requestTokenButton);
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        requestTokenButton.addActionListener(this::requestToken);
        cancelButton.addActionListener(e -> dispose());

        return panel;
    }

    private JPanel createResetPasswordPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel instructionLabel = new JLabel("Enter reset token and new password:");
        panel.add(instructionLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("Reset Token:"));
        tokenField = new JTextField();
        formPanel.add(tokenField);

        formPanel.add(new JLabel("New Password:"));
        newPasswordField = new JPasswordField();
        formPanel.add(newPasswordField);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.setBackground(new Color(34, 139, 34));
        resetPasswordButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(128, 128, 128));
        cancelButton.setForeground(Color.WHITE);

        buttonPanel.add(resetPasswordButton);
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        resetPasswordButton.addActionListener(this::resetPassword);
        cancelButton.addActionListener(e -> dispose());

        return panel;
    }

    private void requestToken(ActionEvent e) {
        String email = emailField.getText().trim();
        
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your email", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authService.requestPasswordReset(email)) {
            // In real application, you would send email with token
            // For demo, we'll just show the token
            String token = "DEMO_TOKEN_123"; // This would come from database
            JOptionPane.showMessageDialog(this, 
                "Reset token: " + token + "\nUse this token to reset your password.", 
                "Reset Token", 
                JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(cardPanel, "RESET");
        } else {
            JOptionPane.showMessageDialog(this, "Email not found in our system", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPassword(ActionEvent e) {
        String token = tokenField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());

        if (token.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both token and new password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authService.resetPassword(token, newPassword)) {
            JOptionPane.showMessageDialog(this, "Password reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid or expired token", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}