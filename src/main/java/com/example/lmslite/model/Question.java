package com.example.lmslite.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    @ElementCollection
    @OrderColumn
    private List<String> options = new ArrayList<>();

    private int correctOptionIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizLesson_id")
    private QuizLesson quiz;

    public boolean isCorrectAnswer(int index) {
        return correctOptionIndex == index;
    }

}
