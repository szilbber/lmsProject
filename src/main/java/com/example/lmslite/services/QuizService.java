package com.example.lmslite.services;

import com.example.lmslite.dto.*;
import com.example.lmslite.enums.EnrollmentStatus;
import com.example.lmslite.exception.EnrollmentNotFoundException;
import com.example.lmslite.exception.LessonNotFoundException;
import com.example.lmslite.model.Enrollment;
import com.example.lmslite.model.Lesson;
import com.example.lmslite.model.Question;
import com.example.lmslite.model.QuizLesson;
import com.example.lmslite.repositories.EnrollmentRepository;
import com.example.lmslite.repositories.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestions(Long quizId) {
        QuizLesson quiz = findQuiz(quizId);
        return quiz.getQuestions().stream()
                .map(QuestionResponse::from)
                .toList();
    }

    @Transactional
    public QuizResultResponse submitQuiz(Long quizId, SubmitQuizRequest request) {
        // 1. запись существует и активна
        Enrollment enrollment = enrollmentRepository.findById(request.enrollmentId())
                .orElseThrow(() -> new EnrollmentNotFoundException("Запись на курс не найдена"));
        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new IllegalStateException("Запись на курс не активна");
        }

        // 2. это тест, и он из курса этой записи
        QuizLesson quiz = findQuiz(quizId);
        if (!quiz.getCourse().getId().equals(enrollment.getCourse().getId())) {
            throw new IllegalStateException("Тест не принадлежит курсу этой записи");
        }

        // 3. ответы студента: номер вопроса -> выбранный вариант
        Map<Long, Integer> selected = new HashMap<>();
        for (AnswerRequest answer : request.answers()) {
            selected.put(answer.questionId(), answer.selectedOption());
        }

        // 4. считаем верные
        int total = quiz.getQuestions().size();
        if (total == 0) {
            throw new IllegalStateException("В тесте нет вопросов");
        }
        int correct = 0;
        for (Question question : quiz.getQuestions()) {
            Integer option = selected.get(question.getId());
            if (option != null && question.isCorrectAnswer(option)) {
                correct++;
            }
        }

        // 5-6. процент и результат
        int scorePercent = correct * 100 / total;
        boolean passed = scorePercent >= quiz.getPassingScore();

        // 7-8. если сдал, отмечаем урок пройденным и сохраняем балл
        if (passed) {
            enrollmentService.completeLesson(enrollment.getId(), quizId);
            enrollment.getCompletedLessons().stream()
                    .filter(p -> p.getLesson().getId().equals(quizId))
                    .findFirst()
                    .ifPresent(p -> {
                        if (p.getQuizScore() == null || p.getQuizScore() < scorePercent) {
                            p.setQuizScore(scorePercent);
                        }
                    });
        }

        return new QuizResultResponse(quizId, quiz.getTitle(), total, correct,
                scorePercent, passed, quiz.getPassingScore());
    }

    private QuizLesson findQuiz(Long quizId) {
        Lesson lesson = lessonRepository.findById(quizId)
                .orElseThrow(() -> new LessonNotFoundException("Урок не найден"));
        if (!(lesson instanceof QuizLesson quiz)) {
            throw new IllegalStateException("Этот урок не является тестом");
        }
        return quiz;
    }
}