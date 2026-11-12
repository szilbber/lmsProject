package com.example.lmslite.dto;

public record LessonStatusResponse(
        Long id,
        String title,
        boolean completed
) {}