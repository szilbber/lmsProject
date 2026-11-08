package com.example.lmslite.model;

import com.example.lmslite.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private Instructor author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status = CourseStatus.DRAFT;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public int getTotalLessons() {
        return lessons.size();
    }

    public boolean isAvailableForEnrollment() {
        return status == CourseStatus.PUBLISHED;
    }

    public void publish() {
        this.status = CourseStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    public int getTotalEstimatedMinutes() {
        int total = 0;
        for (Lesson lesson : lessons) {
            total += lesson.getEstimatedMinutes();
        }
        return total;
    }
}