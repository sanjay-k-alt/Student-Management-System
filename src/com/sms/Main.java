package com.sms;

import com.sms.auth.Login;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Start with Login screen instead of MainFrame directly
            new Login().setVisible(true);
        });
    }
}