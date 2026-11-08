package com.example.lmslite.dto;

import com.example.lmslite.model.Question;

import java.util.List;

public record QuestionResponse(
        Long id,
        String text,
        List<String> options
) {
    public static QuestionResponse from(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getText(),
                List.copyOf(question.getOptions())
        );
    }
}