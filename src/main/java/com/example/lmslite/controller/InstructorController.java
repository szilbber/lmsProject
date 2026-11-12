package com.example.lmslite.controller;

import com.example.lmslite.dto.CourseResponse;
import com.example.lmslite.dto.CreateInstructorRequest;
import com.example.lmslite.dto.InstructorResponse;
import com.example.lmslite.services.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorResponse createInstructor(@Valid @RequestBody CreateInstructorRequest request){
        return instructorService.createInstructor(request);
    }

    @GetMapping("/{id}")
    public InstructorResponse getInstructor(@PathVariable Long id) {
        return instructorService.findById(id);
    }

    @GetMapping("/{id}/courses")
    public List<CourseResponse> getCourses(@PathVariable Long id) {
        return instructorService.getInstructorCourses(id);
    }

}
