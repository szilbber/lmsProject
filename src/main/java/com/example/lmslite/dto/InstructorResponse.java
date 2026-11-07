package com.example.lmslite.dto;

import com.example.lmslite.model.Instructor;

public record InstructorResponse (
        String firstName,
        String lastName,
        String email,
        String bio
){
    public static InstructorResponse from(Instructor instructor) {
        return new InstructorResponse(
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getEmail(),
                instructor.getBio()
        );
    }
}