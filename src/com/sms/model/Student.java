package com.sms.model;

public class Student {
    private int id;
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String course;
    private int semester;
    private double attendance;
    private double marks;

    // Constructors
    public Student() {}

    public Student(String studentId, String name, String email, String phone, 
                  String course, int semester, double attendance, double marks) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.semester = semester;
        this.attendance = attendance;
        this.marks = marks;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public double getAttendance() { return attendance; }
    public void setAttendance(double attendance) { this.attendance = attendance; }

    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    @Override
    public String toString() {
        return String.format("Student[ID=%s, Name=%s, Course=%s, Semester=%d]", 
                           studentId, name, course, semester);
    }
}