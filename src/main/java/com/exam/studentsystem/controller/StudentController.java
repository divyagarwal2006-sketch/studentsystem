package com.exam.studentsystem.controller;

import com.exam.studentsystem.model.Student;
import com.exam.studentsystem.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // 1. Add a student
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            return ResponseEntity.ok(service.addStudent(student));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Retrieve all students
    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    // 3. Search for a student by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. Update student scores and skills
    @PutMapping("/{id}")
    public ResponseEntity<?> updateScoresAndSkills(@PathVariable String id, @RequestBody Student updated) {
        try {
            Student saved = service.updateScoresAndSkills(
                    id,
                    updated.getProgrammingScore(),
                    updated.getMathematicsScore(),
                    updated.getTechnicalSkills()
            );
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Delete a student record
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        try {
            service.deleteStudent(id);
            return ResponseEntity.ok("Deleted student: " + id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. Display the merit list
    @GetMapping("/merit-list")
    public List<Student> getMeritList() {
        return service.getMeritList();
    }

    // Extra Q2 endpoints for Viva demo
    @GetMapping("/search/email")
    public Student searchByEmail(@RequestParam String email) {
        return service.getByEmail(email);
    }

    @GetMapping("/search/course")
    public List<Student> searchByCourse(@RequestParam String course) {
        return service.getByCourse(course);
    }

    @GetMapping("/shared-skills")
    public Map<String, List<String>> getSharedSkills() {
        return service.getStudentsGroupedBySharedSkills();
    }

    @GetMapping("/weak-prog-strong-math")
    public List<Student> getWeakProgStrongMath() {
        return service.getWeakProgStrongMathStudents();
    }
}