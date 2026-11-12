package com.example.lmslite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AnswerRequest(
        @NotNull(message = "Не указан вопрос") Long questionId,
        @NotNull(message = "Не выбран вариант") @Min(0) Integer selectedOption
) {}