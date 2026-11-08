package com.example.lmslite.dto;

import java.util.List;

public record QuestionRequest(
        String text,
        List<String> options,
        int correctOptionIndex
) {}