package com.example.lmslite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCourseRequest(
        @NotBlank(message = "Название обязательно") @Size(max = 255) String title,
        @Size(max = 2000, message = "Не больше 2000 символов") String description,
        @NotNull(message = "Не указан инструктор") Long instructorId
) {}