package com.example.lmslite.dto;

import com.example.lmslite.enums.EnrollmentStatus;
import com.example.lmslite.model.Enrollment;

public record EnrollmentResponse (
        Long id,
        Long studentId,
        Long courseId,
        EnrollmentStatus status
) {
    public static EnrollmentResponse from(Enrollment enrollment){
            return new EnrollmentResponse(
                    enrollment.getId(),
                    enrollment.getStudent().getId(),
                    enrollment.getCourse().getId(),
                    enrollment.getStatus()
            );
    }
}