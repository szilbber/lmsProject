package com.example.lmslite.model;

import com.example.lmslite.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @OneToMany(mappedBy = "enrollment")
    private Set<LessonProgress> completedLessons = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;

    @PrePersist
    void onCreate() {
        enrolledAt = LocalDateTime.now();
    }

    public int getProgressPercent(){
        int total = course.getTotalLessons();
        if(total==0){
            return 0;
        }
        return completedLessons.size() * 100 / total;
    }
}
