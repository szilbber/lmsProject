package com.example.lmslite.dto;

import com.example.lmslite.enums.LessonType;
import com.example.lmslite.model.*;
import com.example.lmslite.repositories.LessonRepository;

import java.time.LocalDateTime;
import java.util.List;

public record LessonDetailsResponse(
        Long id,
        String title,
        LessonType type,
        int orderIndex,
        String videoUrl,
        Integer durationMinutes,
        String content,
        Integer readingTimeMinutes,
        Integer passingScore,
        Integer timeLimitMinutes,
        List<QuestionResponse> questions
) {
    public static LessonDetailsResponse from(Lesson lesson) {
        String videoUrl = null;
        Integer durationMinutes = null;
        String content = null;
        Integer readingTimeMinutes = null;
        Integer passingScore = null;
        Integer timeLimitMinutes = null;
        List<QuestionResponse> questions = null;

        if (lesson instanceof VideoLesson video) {
            videoUrl = video.getVideoUrl();
            durationMinutes = video.getDurationMinutes();
        } else if (lesson instanceof TextLesson text) {
            content = text.getContent();
            readingTimeMinutes = text.getReadingTimeMinutes();
        } else if (lesson instanceof QuizLesson quiz) {
            passingScore = quiz.getPassingScore();
            timeLimitMinutes = quiz.getTimeLimitMinutes();
            questions = quiz.getQuestions().stream()
                    .map(QuestionResponse::from)
                    .toList();
        }

        return new LessonDetailsResponse(
                lesson.getId(), lesson.getTitle(), lesson.getType(), lesson.getOrderIndex(),
                videoUrl, durationMinutes, content, readingTimeMinutes,
                passingScore, timeLimitMinutes, questions);
    }
}