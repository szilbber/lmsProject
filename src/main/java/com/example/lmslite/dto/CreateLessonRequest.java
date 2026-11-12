package com.example.lmslite.dto;

import com.example.lmslite.enums.LessonType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateLessonRequest(
        @NotBlank(message = "Название обязательно") @Size(max = 255) String title,
        @NotNull(message = "Не указан тип урока") LessonType type,
        String videoUrl,
        @Min(value = 1, message = "Минимум 1 минута") Integer durationMinutes,
        String content,
        @Min(value = 1, message = "Минимум 1 минута") Integer readingTimeMinutes,
        @Min(0) @Max(value = 100, message = "Проходной балл от 0 до 100") Integer passingScore,
        @Min(value = 1, message = "Минимум 1 минута") Integer timeLimitMinutes,
        @Valid List<QuestionRequest> questions
) {}