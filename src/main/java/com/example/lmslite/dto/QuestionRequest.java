package com.example.lmslite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuestionRequest(
        @NotBlank(message = "Текст вопроса обязателен") String text,
        @NotNull @Size(min = 2, message = "Нужно минимум 2 варианта ответа")
        List<@NotBlank(message = "Вариант ответа не может быть пустым") String> options,
        @NotNull(message = "Не указан правильный ответ") @Min(0) Integer correctOptionIndex
) {}