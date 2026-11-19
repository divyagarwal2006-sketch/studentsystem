package com.exam.studentsystem.service;

import com.exam.studentsystem.model.Student;
import com.exam.studentsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    // Rule 8: Prevent duplicate student IDs and validate scores (0-100)
    private void validateScores(double prog, double math) {
        if (prog < 0 || prog > 100 || math < 0 || math > 100) {
            throw new IllegalArgumentException("Scores must be between 0 and 100.");
        }
    }

    public Student addStudent(Student student) {
        if (repo.existsById(student.getStudentId())) {
            throw new IllegalArgumentException("Duplicate Student ID: " + student.getStudentId());
        }
        validateScores(student.getProgrammingScore(), student.getMathematicsScore());
        return repo.save(student);
    }

    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    // Rule 4: Search by ID, email, or course
    public Student getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + id));
    }

    public Student getByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Student not found with email: " + email));
    }

    public List<Student> getByCourse(String course) {
        return repo.findByCourseNameIgnoreCase(course);
    }

    // Update scores and skills (Q3.4)
    public Student updateScoresAndSkills(String id, double progScore, double mathScore, List<String> skills) {
        validateScores(progScore, mathScore);
        Student existing = getById(id);
        existing.setProgrammingScore(progScore);
        existing.setMathematicsScore(mathScore);
        if (skills != null) {
            existing.setTechnicalSkills(skills);
        }
        return repo.save(existing);
    }

    public void deleteStudent(String id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Student not found: " + id);
        }
        repo.deleteById(id);
    }

    // Rule 5: Merit list sorted by average score descending
    public List<Student> getMeritList() {
        return repo.findAll().stream()
                .sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
                .collect(Collectors.toList());
    }

    // Rule 6: Identify students who have the same technical skills (using HashMap)
    public Map<String, List<String>> getStudentsGroupedBySharedSkills() {
        Map<String, List<String>> skillMap = new HashMap<>();
        for (Student s : repo.findAll()) {
            for (String skill : s.getTechnicalSkills()) {
                skillMap.computeIfAbsent(skill.toLowerCase(), k -> new ArrayList<>())
                        .add(s.getStudentName() + " (" + s.getStudentId() + ")");
            }
        }
        return skillMap.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // Rule 7: Programming < 50 (Needs Improvement) AND Mathematics >= 75
    public List<Student> getWeakProgStrongMathStudents() {
        return repo.findAll().stream()
                .filter(s -> s.getProgrammingScore() < 50 && s.getMathematicsScore() >= 75)
                .collect(Collectors.toList());
    }
}