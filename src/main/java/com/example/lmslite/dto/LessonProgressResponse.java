package com.example.lmslite.dto;

import com.example.lmslite.model.LessonProgress;

import java.time.LocalDateTime;

public record LessonProgressResponse (
        Long lessonId,
        String lessonTitle,
        LocalDateTime completedAt,
        Integer quizScore
) {
    public static LessonProgressResponse from(LessonProgress lessonProgress) {
        return new LessonProgressResponse(
                lessonProgress.getLesson().getId(),
                lessonProgress.getLesson().getTitle(),
                lessonProgress.getCompletedAt(),
                lessonProgress.getQuizScore()
        );
    }
}