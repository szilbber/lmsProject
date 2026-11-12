package com.example.lmslite.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubmitQuizRequest(
        @NotNull(message = "Не указана запись на курс") Long enrollmentId,
        @NotNull(message = "Нет ответов") @Valid List<AnswerRequest> answers
) {}