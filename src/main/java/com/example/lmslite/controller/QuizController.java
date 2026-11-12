package com.example.lmslite.controller;

import com.example.lmslite.dto.QuestionResponse;
import com.example.lmslite.dto.QuizResultResponse;
import com.example.lmslite.dto.SubmitQuizRequest;
import com.example.lmslite.services.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/lessons/{quizId}/questions")
    public List<QuestionResponse> getQuestions(@PathVariable Long quizId) {
        return quizService.getQuestions(quizId);
    }

    @PostMapping("/lessons/{quizId}/submit")
    public QuizResultResponse submitQuiz(@PathVariable Long quizId, @Valid @RequestBody SubmitQuizRequest request){
        return quizService.submitQuiz(quizId, request);
    }
}