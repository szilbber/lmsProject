package com.example.lmslite.services;

import com.example.lmslite.dto.CreateLessonRequest;
import com.example.lmslite.dto.LessonDetailsResponse;
import com.example.lmslite.dto.LessonResponse;
import com.example.lmslite.dto.QuestionRequest;
import com.example.lmslite.exception.CourseNotFoundException;
import com.example.lmslite.exception.LessonNotFoundException;
import com.example.lmslite.model.*;
import com.example.lmslite.repositories.CourseRepository;
import com.example.lmslite.repositories.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public LessonDetailsResponse getLessonById(Long id) {
        return LessonDetailsResponse.from(findLesson(id));
    }

    @Transactional
    public LessonResponse updateLesson(Long id, CreateLessonRequest request) {
        Lesson lesson =  findLesson(id);
        if (lesson.getType() != request.type()) {
            throw new IllegalStateException("Нельзя изменить тип урока");
        }
        fillLesson(lesson, request);
        return LessonResponse.from(lesson);
    }

    @Transactional
    public void deleteLesson(Long id) {
        lessonRepository.delete(findLesson(id));
    }

    @Transactional
    public List<LessonResponse> getCourseLessons(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException("Курс не найден");
        }
        return lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId)
                .stream()
                .map(LessonResponse::from)
                .toList();
    }

    @Transactional
    public LessonResponse createLesson(Long courseId, CreateLessonRequest request) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("Курс не найден")
        );
        Lesson lesson = switch (request.type()) {
            case VIDEO -> new VideoLesson();
            case TEXT -> new TextLesson();
            case QUIZ -> new QuizLesson();
        };
        fillLesson(lesson, request);

        int nextIndex = course.getLessons().stream()
                .mapToInt(Lesson::getOrderIndex)
                .max()
                .orElse(0) + 1;
        lesson.setOrderIndex(nextIndex);
        lesson.setCourse(course);

        return LessonResponse.from(lessonRepository.save(lesson));
    }

    private Lesson findLesson(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new LessonNotFoundException("Урок не найден"));
    }

    private int required(Integer value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException("Поле " + fieldName + " обязательно для этого типа урока");
        }
        return value;
    }

    private void fillLesson(Lesson lesson, CreateLessonRequest request) {
        lesson.setTitle(request.title());

        if (lesson instanceof VideoLesson video) {
            video.setVideoUrl(request.videoUrl());
            video.setDurationMinutes(required(request.durationMinutes(), "durationMinutes"));
        } else if (lesson instanceof TextLesson text) {
            text.setContent(request.content());
            text.setReadingTimeMinutes(required(request.readingTimeMinutes(), "readingTimeMinutes"));
        } else if (lesson instanceof QuizLesson quiz) {
            quiz.setPassingScore(required(request.passingScore(), "passingScore"));
            quiz.setTimeLimitMinutes(request.timeLimitMinutes());
            quiz.getQuestions().clear();
            if (request.questions() != null) {
                for (QuestionRequest item : request.questions()) {
                    Question question = new Question();
                    question.setText(item.text());
                    question.setOptions(new ArrayList<>(item.options()));
                    question.setCorrectOptionIndex(item.correctOptionIndex());
                    question.setQuiz(quiz);
                    quiz.getQuestions().add(question);
                }
            }
        }
    }
}
