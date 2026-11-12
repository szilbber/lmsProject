package com.example.lmslite.controller;

import com.example.lmslite.dto.CourseDetailsWithLessonsResponse;
import com.example.lmslite.dto.CourseResponse;
import com.example.lmslite.dto.CreateCourseRequest;
import com.example.lmslite.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(@Valid @RequestBody CreateCourseRequest request){
        return courseService.createCourse(request);
    }

    @GetMapping
    public List<CourseResponse> getCourses() {
        return courseService.getPublishedCourses();
    }

    @GetMapping("/{id}")
    public CourseDetailsWithLessonsResponse getCourseDetailWithLessons(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PutMapping("/{id}")
    public CourseResponse updateCourse(@PathVariable Long id, @Valid @RequestBody CreateCourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @PatchMapping("/{id}/publish")
    public CourseResponse publishCourse(@PathVariable Long id) {
        return courseService.publishCourse(id);
    }

    @PatchMapping("/{id}/archive")
    public CourseResponse archiveCourse(@PathVariable Long id) {
        return courseService.archiveCourse(id);
    }

}