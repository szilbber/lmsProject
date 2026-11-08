package com.example.lmslite.dto;

import com.example.lmslite.enums.LessonType;
import com.example.lmslite.model.Question;

import java.util.List;

public record CreateLessonRequest(
        String title,
        LessonType type,
        String videoUrl,
        Integer durationMinutes,
        String content,
        Integer readingTimeMinutes,
        Integer passingScore,
        Integer timeLimitMinutes,
        List<QuestionRequest> questions
) {}