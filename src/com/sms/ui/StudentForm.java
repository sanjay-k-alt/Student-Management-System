package com.sms.ui;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.List; // Import List

public class StudentForm extends JPanel {
    private JTextField studentIdField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> courseComboBox;
    private JSpinner semesterSpinner;
    private JSpinner attendanceSpinner;
    private JSpinner marksSpinner;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;
    private JTextField searchField;

    private StudentTablePanel studentTablePanel;
    private StudentDAO studentDAO;

    public StudentForm(StudentTablePanel studentTablePanel) {
        this.studentTablePanel = studentTablePanel;
        this.studentDAO = new StudentDAO();
        initializeForm();
    }

    private void initializeForm() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Initialize components
        studentIdField = new JTextField(20);
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        String[] courses = {"Computer Science", "Electrical Engineering",
                "Mechanical Engineering", "Civil Engineering",
                "Business Administration", "Mathematics"};
        courseComboBox = new JComboBox<>(courses);

        semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        attendanceSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5));
        marksSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5));

        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        // Add components to form
        int row = 0;

        // Search row
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Search (ID/Name/Email/Course):"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(searchField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(searchButton, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        row++;

        // Separator
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 4;
        panel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        row++;

        // Student ID
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Student ID*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(studentIdField, gbc);
        gbc.gridwidth = 1;

        row++;

        // Name
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Full Name*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(nameField, gbc);
        gbc.gridwidth = 1;

        row++;

        // Email
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Email*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(emailField, gbc);
        gbc.gridwidth = 1;

        row++;

        // Phone
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(phoneField, gbc);
        gbc.gridwidth = 1;

        row++;

        // Course and Semester
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Course*:"), gbc);

        gbc.gridx = 1;
        panel.add(courseComboBox, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Semester*:"), gbc);

        gbc.gridx = 3;
        panel.add(semesterSpinner, gbc);

        row++;

        // Attendance and Marks
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Attendance (%):"), gbc);

        gbc.gridx = 1;
        panel.add(attendanceSpinner, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Marks (%):"), gbc);

        gbc.gridx = 3;
        panel.add(marksSpinner, gbc);

        // Attach search listener here (searchButton is initialized in this method)
        searchButton.addActionListener(this::searchStudents);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Initialize the buttons
        addButton = new JButton("Add Student");
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");
        clearButton = new JButton("Clear Form");

        // Set colors
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);

        updateButton.setBackground(new Color(34, 139, 34));
        updateButton.setForeground(Color.WHITE);

        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);

        clearButton.setBackground(new Color(128, 128, 128));
        clearButton.setForeground(Color.WHITE);

        // Add action listeners
        addButton.addActionListener(this::addStudent);
        updateButton.addActionListener(this::updateStudent);
        deleteButton.addActionListener(this::deleteStudent);
        clearButton.addActionListener(e -> clearForm());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);

        return panel;
    }

    private void addStudent(ActionEvent e) {
        if (!validateForm()) return;

        try {
            Student student = createStudentFromForm();

            if (studentDAO.addStudent(student)) {
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                studentTablePanel.refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding student. Student ID might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent(ActionEvent e) {
        if (!validateForm()) return;

        try {
            Student student = createStudentFromForm();

            if (studentDAO.updateStudent(student)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                studentTablePanel.refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating student. Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent(ActionEvent e) {
        String studentId = studentIdField.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Student ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student " + studentId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (studentDAO.deleteStudent(studentId)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                studentTablePanel.refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting student. Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * NEW/MODIFIED METHOD: Searches for a student by ID or other fields and loads the result into the form.
     */
    private void searchStudents(ActionEvent e) {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            // If the search field is empty, show all records in the table and clear the form
            studentTablePanel.refreshData();
            clearForm(); 
            JOptionPane.showMessageDialog(this, "Search field is empty. Showing all records.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Student foundStudent = null;

        // 1. Try searching by exact Student ID first
        foundStudent = studentDAO.getStudentById(keyword);

        // 2. If not found by exact ID, use the general search (searches Name, Email, Course, etc.)
        if (foundStudent == null) {
            List<Student> results = studentDAO.searchStudents(keyword);
            
            if (!results.isEmpty()) {
                // If multiple results, load the first one into the form fields
                foundStudent = results.get(0);
            }
        }

        // 3. Update the UI
        if (foundStudent != null) {
            // Load the found student data into the form fields
            loadStudentData(foundStudent);
            JOptionPane.showMessageDialog(this, "Student record found and loaded!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // If no student is found
            clearForm(); // Clear the form fields
            JOptionPane.showMessageDialog(this, "No student record found for keyword: " + keyword, "Not Found", JOptionPane.WARNING_MESSAGE);
        }
        
        // 4. Update the table panel to show the filtered results
        // This is usually done to update the 'Student Records' tab
        studentTablePanel.searchData(keyword); 
    }

    private Student createStudentFromForm() {
        Student student = new Student();
        student.setStudentId(studentIdField.getText().trim());
        student.setName(nameField.getText().trim());
        student.setEmail(emailField.getText().trim());
        student.setPhone(phoneField.getText().trim());
        student.setCourse(courseComboBox.getSelectedItem().toString());
        student.setSemester((Integer) semesterSpinner.getValue());
        student.setAttendance((Double) attendanceSpinner.getValue());
        student.setMarks((Double) marksSpinner.getValue());
        return student;
    }

    private boolean validateForm() {
        if (studentIdField.getText().trim().isEmpty()) {
            showError("Please enter Student ID");
            studentIdField.requestFocus();
            return false;
        }
        if (nameField.getText().trim().isEmpty()) {
            showError("Please enter Student Name");
            nameField.requestFocus();
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            showError("Please enter Email");
            emailField.requestFocus();
            return false;
        }
        if (!isValidEmail(emailField.getText().trim())) {
            showError("Please enter a valid email address");
            emailField.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public void clearForm() {
        studentIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        courseComboBox.setSelectedIndex(0);
        semesterSpinner.setValue(1);
        attendanceSpinner.setValue(0.0);
        marksSpinner.setValue(0.0);
        studentIdField.requestFocus();
    }

    public void loadStudentData(Student student) {
        studentIdField.setText(student.getStudentId());
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhone());
        courseComboBox.setSelectedItem(student.getCourse());
        semesterSpinner.setValue(student.getSemester());
        attendanceSpinner.setValue(student.getAttendance());
        marksSpinner.setValue(student.getMarks());
    }
}