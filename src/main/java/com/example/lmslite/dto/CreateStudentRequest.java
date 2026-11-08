package com.example.lmslite.dto;


public record CreateStudentRequest (
        String firstName,
        String lastName,
        String email
) {}

