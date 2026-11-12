package com.example.lmslite.dto;


import com.example.lmslite.enums.EnrollmentStatus;
import com.example.lmslite.model.Enrollment;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ProgressResponse(
        Long enrollmentId,
        String courseTitle,
        int progressPercent,
        int completedLessons,
        int totalLessons,
        EnrollmentStatus status,
        List<LessonStatusResponse> lessons
) {
    public static ProgressResponse from(Enrollment enrollment) {
        Set<Long> completedIds = enrollment.getCompletedLessons().stream()
                .map(p -> p.getLesson().getId())
                .collect(Collectors.toSet());

        List<LessonStatusResponse> lessons = enrollment.getCourse().getLessons().stream()
                .map(l -> new LessonStatusResponse(l.getId(), l.getTitle(), completedIds.contains(l.getId())))
                .toList();

        return new ProgressResponse(
                enrollment.getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getProgressPercent(),
                enrollment.getCompletedLessons().size(),
                enrollment.getCourse().getTotalLessons(),
                enrollment.getStatus(),
                lessons
        );
    }
}