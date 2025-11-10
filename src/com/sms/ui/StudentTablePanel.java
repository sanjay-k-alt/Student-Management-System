package com.sms.ui;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentTablePanel extends JPanel {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    private StudentForm studentForm;
    private JLabel statsLabel;

    public StudentTablePanel() {
        this.studentDAO = new StudentDAO();
        initializeUI();
        refreshData();
    }

    public void setStudentForm(StudentForm studentForm) {
        this.studentForm = studentForm;
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsLabel = new JLabel("Total Students: 0");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statsLabel.setForeground(Color.BLUE);
        statsPanel.add(statsLabel);

        // Create table model
        String[] columns = {"ID", "Student ID", "Name", "Email", "Phone", "Course", "Semester", "Attendance", "Marks"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : String.class;
            }
        };

        // Create table
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentTable.setRowHeight(25);
        
        // Set column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(70);
        studentTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        studentTable.getColumnModel().getColumn(8).setPreferredWidth(80);

        // Add double-click listener
        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    loadSelectedStudent();
                }
            }
        });

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(studentTable);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();

        add(statsPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete Selected");
        JButton viewButton = new JButton("View/Edit Selected");
        JButton exportButton = new JButton("Export Data");
        
        refreshButton.addActionListener(e -> refreshData());
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        viewButton.addActionListener(e -> loadSelectedStudent());
        exportButton.addActionListener(e -> exportData());
        
        // Set colors
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        
        viewButton.setBackground(new Color(34, 139, 34));
        viewButton.setForeground(Color.WHITE);
        
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        
        exportButton.setBackground(new Color(128, 0, 128));
        exportButton.setForeground(Color.WHITE);
        
        panel.add(refreshButton);
        panel.add(viewButton);
        panel.add(deleteButton);
        panel.add(exportButton);
        
        return panel;
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        
        for (Student student : students) {
            Object[] row = {
                student.getId(),
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getPhone() != null ? student.getPhone() : "",
                student.getCourse(),
                student.getSemester(),
                String.format("%.1f%%", student.getAttendance()),
                String.format("%.1f%%", student.getMarks())
            };
            tableModel.addRow(row);
        }
        
        updateStats();
    }

    public void searchData(String keyword) {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.searchStudents(keyword);
        
        for (Student student : students) {
            Object[] row = {
                student.getId(),
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getPhone() != null ? student.getPhone() : "",
                student.getCourse(),
                student.getSemester(),
                String.format("%.1f%%", student.getAttendance()),
                String.format("%.1f%%", student.getMarks())
            };
            tableModel.addRow(row);
        }
        
        updateStats();
        
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students found matching: " + keyword, "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateStats() {
        int totalStudents = studentDAO.getTotalStudents();
        int displayedStudents = tableModel.getRowCount();
        
        if (totalStudents == displayedStudents) {
            statsLabel.setText("Total Students: " + totalStudents);
        } else {
            statsLabel.setText("Displaying: " + displayedStudents + " of " + totalStudents + " students");
        }
    }

    private void loadSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String studentId = tableModel.getValueAt(selectedRow, 1).toString();
            Student student = studentDAO.getStudentById(studentId);
            
            if (student != null && studentForm != null) {
                studentForm.loadStudentData(student);
                
                // Switch to form tab
                JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
                if (tabbedPane != null) {
                    tabbedPane.setSelectedIndex(0);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student first.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String studentId = tableModel.getValueAt(selectedRow, 1).toString();
            String studentName = tableModel.getValueAt(selectedRow, 2).toString();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete student:\n" + studentName + " (" + studentId + ")?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                if (studentDAO.deleteStudent(studentId)) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting student.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Student Data");
        fileChooser.setSelectedFile(new java.io.File("students_data.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                // Write header
                writer.println("ID,Student ID,Name,Email,Phone,Course,Semester,Attendance,Marks");
                
                // Write data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        if (j > 0) sb.append(",");
                        sb.append(tableModel.getValueAt(i, j));
                    }
                    writer.println(sb.toString());
                }
                
                JOptionPane.showMessageDialog(this, "Data exported successfully to: " + file.getName(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error exporting data: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}