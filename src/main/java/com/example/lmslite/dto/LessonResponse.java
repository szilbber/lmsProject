package com.example.lmslite.dto;

import com.example.lmslite.enums.LessonType;
import com.example.lmslite.model.Lesson;

public record LessonResponse (
        Long id,
        String title,
        LessonType type,
        int orderIndex
){
    public static LessonResponse from(Lesson lesson){
        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getType(),
                lesson.getOrderIndex()
        );
    }
}