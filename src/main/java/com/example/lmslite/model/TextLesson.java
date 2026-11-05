package com.example.lmslite.model;

import com.example.lmslite.enums.LessonType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TextLesson extends Lesson {

    private String content;

    private int readingTimeMinutes;

    @Override
    public LessonType getType() {
        return LessonType.TEXT;
    }

    @Override
    public int getEstimatedMinutes() {
        return readingTimeMinutes;
    }
}
