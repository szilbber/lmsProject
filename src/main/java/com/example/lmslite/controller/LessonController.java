package com.example.lmslite.controller;

import com.example.lmslite.dto.*;
import com.example.lmslite.services.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping("/courses/{courseId}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse createLesson(@PathVariable Long courseId, @Valid @RequestBody CreateLessonRequest request) {
        return lessonService.createLesson(courseId, request);
    }

    @GetMapping("/courses/{courseId}/lessons")
    public List<LessonResponse> getCourseLessons(@PathVariable Long courseId) {
        return lessonService.getCourseLessons(courseId);
    }

    @GetMapping("/lessons/{id}")
    public LessonDetailsResponse getLessonDetails(@PathVariable Long id) {
        return lessonService.getLessonById(id);
    }

    @PutMapping("/lessons/{id}")
    public LessonResponse updateLesson(@PathVariable Long id, @Valid @RequestBody CreateLessonRequest request) {
        return lessonService.updateLesson(id, request);
    }

    @DeleteMapping("/lessons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
    }
}