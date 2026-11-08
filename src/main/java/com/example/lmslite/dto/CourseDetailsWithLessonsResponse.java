package com.example.lmslite.dto;

import com.example.lmslite.enums.CourseStatus;
import com.example.lmslite.model.Course;

import java.util.List;

public record CourseDetailsWithLessonsResponse (
        Long id,
        String title,
        String description,
        String authorName,
        CourseStatus status,
        int totalLessons,
        int duration,
        List<LessonResponse> lessons
){
    public static CourseDetailsWithLessonsResponse from(Course course) {
        return new CourseDetailsWithLessonsResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getAuthor().getFullName(),
                course.getStatus(),
                course.getTotalLessons(),
                course.getTotalEstimatedMinutes(),
                course.getLessons().stream().map(LessonResponse::from).toList()
        );
    }
}
