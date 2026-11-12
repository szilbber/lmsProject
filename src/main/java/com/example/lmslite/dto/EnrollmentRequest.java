package com.example.lmslite.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
        @NotNull(message = "Не указан студент") Long studentId,
        @NotNull(message = "Не указан курс") Long courseId
) {}