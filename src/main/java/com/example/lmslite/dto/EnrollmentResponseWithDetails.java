package com.example.lmslite.dto;

import com.example.lmslite.enums.EnrollmentStatus;
import com.example.lmslite.model.Enrollment;

import java.util.List;

public record EnrollmentResponseWithDetails(
        Long id,
        Long studentId,
        Long courseId,
        EnrollmentStatus status,
        List<LessonProgressResponse> completedLessons,
        int progressPercent
) {
    public static EnrollmentResponseWithDetails from(Enrollment enrollment){
        return new EnrollmentResponseWithDetails(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                enrollment.getStatus(),
                enrollment.getCompletedLessons().stream().map(LessonProgressResponse::from).toList(),
                enrollment.getProgressPercent()
        );
    }

}