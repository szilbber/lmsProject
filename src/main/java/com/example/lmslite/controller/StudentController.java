package com.example.lmslite.controller;

import com.example.lmslite.dto.CreateStudentRequest;
import com.example.lmslite.dto.StudentResponse;
import com.example.lmslite.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse createStudent(@Valid @RequestBody CreateStudentRequest request){
        return studentService.createStudent(request);
    }

    @GetMapping("/{id}")
    public StudentResponse getStudent(@PathVariable Long id){
        return studentService.findById(id);
    }

}