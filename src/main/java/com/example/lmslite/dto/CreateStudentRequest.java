package com.example.lmslite.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateStudentRequest(
        @NotBlank(message = "Имя обязательно") @Size(max = 100) String firstName,
        @NotBlank(message = "Фамилия обязательна") @Size(max = 100) String lastName,
        @NotBlank(message = "Email обязателен") @Email(message = "Некорректный email") String email
) {}

