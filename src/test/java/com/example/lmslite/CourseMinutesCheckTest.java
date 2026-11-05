package com.example.lmslite;

import com.example.lmslite.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseMinutesCheckTest {

    @Test
    void totalEstimatedMinutes_mixedLessons() {
        Course course = new Course();

        VideoLesson video = new VideoLesson();
        video.setDurationMinutes(15);

        TextLesson text = new TextLesson();
        text.setReadingTimeMinutes(10);

        QuizLesson quiz = new QuizLesson();
        quiz.setTimeLimitMinutes(20);

        course.getLessons().add(video);
        course.getLessons().add(text);
        course.getLessons().add(quiz);

        assertEquals(45, course.getTotalEstimatedMinutes());
    }
}