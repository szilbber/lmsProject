package com.example.lmslite.model;

import com.example.lmslite.enums.LessonType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class QuizLesson extends Lesson {

    @OneToMany(mappedBy = "quiz")
    private List<Question> questions = new ArrayList<>();
    private int passingScore;
    private Integer timeLimitMinutes;

    @Override
    public LessonType getType() {
        return LessonType.QUIZ;
    }

    @Override
    public int getEstimatedMinutes() {
        return timeLimitMinutes != null? timeLimitMinutes : questions.size();
    }
}
