package com.example.lmslite.dto;

import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest (
        String title,
        String description,
        @NotNull Long instructorId
) {}