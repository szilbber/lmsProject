package com.example.lmslite.dto;

public record CreateCourseRequest (
        String title,
        String description,
        Long instructorId
) {}