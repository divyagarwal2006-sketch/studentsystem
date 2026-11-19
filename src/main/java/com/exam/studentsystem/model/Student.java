package com.exam.studentsystem.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "programming_score")
    private double programmingScore;

    @Column(name = "mathematics_score")
    private double mathematicsScore;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private List<String> technicalSkills = new ArrayList<>();

    // Default constructor (required by JPA)
    public Student() {}

    // Parameterized constructor
    public Student(String studentId, String studentName, String email, String courseName,
                   double programmingScore, double mathematicsScore, List<String> technicalSkills) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        this.technicalSkills = technicalSkills != null ? technicalSkills : new ArrayList<>();
    }

    // Computed business logic methods (Q2)
    public double getAverageScore() {
        return (programmingScore + mathematicsScore) / 2.0;
    }

    public String getPerformanceCategory() {
        double avg = getAverageScore();
        if (avg >= 85) return "Excellent";
        if (avg >= 70) return "Good";
        if (avg >= 50) return "Satisfactory";
        return "Needs Improvement";
    }

    public String getStrongerSubject() {
        if (programmingScore > mathematicsScore) return "Programming";
        if (mathematicsScore > programmingScore) return "Mathematics";
        return "Equal";
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(double programmingScore) { this.programmingScore = programmingScore; }

    public double getMathematicsScore() { return mathematicsScore; }
    public void setMathematicsScore(double mathematicsScore) { this.mathematicsScore = mathematicsScore; }

    public List<String> getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(List<String> technicalSkills) { this.technicalSkills = technicalSkills; }

    // Object methods
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", avg=" + getAverageScore() +
                ", category='" + getPerformanceCategory() + '\'' +
                ", skills=" + technicalSkills +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}