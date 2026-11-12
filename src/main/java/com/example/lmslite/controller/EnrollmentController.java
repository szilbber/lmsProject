package com.example.lmslite.controller;

import com.example.lmslite.dto.*;
import com.example.lmslite.services.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enrollments")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse enroll (@Valid @RequestBody EnrollmentRequest request) {
        Long studentId = request.studentId();
        Long courseId = request.courseId();
        return enrollmentService.enrollStudent(studentId, courseId);
    }

    @GetMapping("/students/{id}/enrollments")
    public List<EnrollmentResponse> getStudentEnrollments(@PathVariable Long id) {
        return enrollmentService.getStudentEnrollments(id);
    }

    @GetMapping("/enrollments/{id}")
    public EnrollmentResponseWithDetails getEnrollmentDetailsWithProgress(@PathVariable Long id) {
        return enrollmentService.getEnrollment(id);
    }

    @DeleteMapping("/enrollments/{id}")
    public EnrollmentResponse dropEnrollment(@PathVariable Long id) {
        return enrollmentService.dropEnrollment(id);
    }

    @PostMapping("/enrollments/{id}/lessons/{lessonId}/complete")
    public ProgressResponse completeLesson(@PathVariable Long id, @PathVariable Long lessonId) {
        return enrollmentService.completeLesson(id, lessonId);
    }

    @GetMapping("/enrollments/{id}/progress")
    public ProgressResponse getLessonProgress(@PathVariable Long id) {
        return enrollmentService.getProgress(id);
    }

}
