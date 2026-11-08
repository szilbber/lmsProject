package com.example.lmslite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CourseNotFoundException.class,
            StudentNotFoundException.class,
            InstructorNotFoundException.class,
            EnrollmentNotFoundException.class,
            LessonNotFoundException.class
    })
    public ProblemDetail handleNotFound(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({
            AlreadyEnrolledException.class,
            EmailAlreadyUsedException.class,
            CourseNotPublishedException.class
    })
    public ProblemDetail handleConflict(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({
            IllegalStateException.class,
            IllegalArgumentException.class
    })
    public ProblemDetail handleBadRequest(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}