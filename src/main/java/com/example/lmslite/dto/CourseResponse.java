package com.example.lmslite.dto;

import com.example.lmslite.enums.CourseStatus;
import com.example.lmslite.model.Course;

public record CourseResponse (
        Long id,
        String title,
        String description,
        String authorName,
        CourseStatus status,
        int totalLessons,
        int duration
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getAuthor().getFullName(),
                course.getStatus(),
                course.getTotalLessons(),
                course.getTotalEstimatedMinutes()
        );
    }
}
