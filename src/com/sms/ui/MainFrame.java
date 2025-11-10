package com.sms.ui;

import com.sms.auth.AuthService;
import com.sms.auth.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    private StudentTablePanel studentTablePanel;
    private StudentForm studentForm;
    private AuthService authService;
    private JLabel userInfoLabel;
    
    // 1. New Class Field: JTabbedPane
    private JTabbedPane tabbedPane; 

    public MainFrame(AuthService authService) {
        this.authService = authService;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Management System - Welcome " + authService.getCurrentUser().getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Create components
        studentTablePanel = new StudentTablePanel();
        studentForm = new StudentForm(studentTablePanel);
        studentTablePanel.setStudentForm(studentForm);

        // Create tabbed pane (now using the class field)
        tabbedPane = new JTabbedPane(); 
        tabbedPane.addTab("Student Form", studentForm); // Index 0
        tabbedPane.addTab("Student Records", studentTablePanel); // Index 1

        // Create menu bar with logout
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // Add user info panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoLabel = new JLabel("Welcome: " + authService.getCurrentUser().getUsername() + 
                                 " (" + authService.getCurrentUserRole() + ")");
        userPanel.add(userInfoLabel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(userPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        logoutItem.addActionListener(this::performLogout);
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Student Menu
        JMenu studentMenu = new JMenu("Student");
        JMenuItem addStudentItem = new JMenuItem("Add New Student");
        JMenuItem refreshItem = new JMenuItem("Refresh Data");
        
        // --- START OF CHANGE ---
        addStudentItem.addActionListener(e -> {
            // 2. Switch to the "Student Form" tab (Index 0)
            tabbedPane.setSelectedIndex(0); 
            // Clear the form fields
            studentForm.clearForm();
        });
        // --- END OF CHANGE ---
        
        refreshItem.addActionListener(e -> studentTablePanel.refreshData());
        
        studentMenu.add(addStudentItem);
        studentMenu.addSeparator();
        studentMenu.add(refreshItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(studentMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void performLogout(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            authService.logout();
            new Login().setVisible(true);
            this.dispose();
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Student Management System\nVersion 2.0\nLogged in as: " + 
            authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUserRole() + ")",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }
}