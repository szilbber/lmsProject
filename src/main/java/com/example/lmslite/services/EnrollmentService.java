package com.example.lmslite.services;

import com.example.lmslite.dto.EnrollmentResponse;
import com.example.lmslite.dto.EnrollmentResponseWithDetails;
import com.example.lmslite.dto.ProgressResponse;
import com.example.lmslite.enums.CourseStatus;
import com.example.lmslite.enums.EnrollmentStatus;
import com.example.lmslite.exception.*;
import com.example.lmslite.model.*;
import com.example.lmslite.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository  lessonProgressRepository;

    @Transactional
    public EnrollmentResponse enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Курс не найден"));
        if(course.getStatus() != CourseStatus.PUBLISHED) {
            throw new CourseNotPublishedException("Запись на неопубликованный курс");
        }
        if(enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new AlreadyEnrolledException("Студент уже записан");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return EnrollmentResponse.from(enrollmentRepository.save(enrollment));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getStudentEnrollments(Long studentId){
       if (!studentRepository.existsById(studentId)) {
                throw new StudentNotFoundException("Студент не найден");
       }
       return enrollmentRepository.findByStudentId(studentId).stream()
               .map(EnrollmentResponse::from)
               .toList();
    }

    @Transactional
    public ProgressResponse completeLesson(Long enrollmentId, Long lessonId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Запись на курс не найдена"));
        if(enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new IllegalStateException("Запись на курс не активна");
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Урок не найден"));
        if (!lesson.getCourse().getId().equals(enrollment.getCourse().getId())) {
            throw new IllegalStateException("Урок не принадлежит курсу этой записи");
        }

        boolean alreadyCompleted = enrollment.getCompletedLessons().stream()
                .anyMatch(p -> p.getLesson().getId().equals(lessonId));
        if(alreadyCompleted) {
            return ProgressResponse.from(enrollment);
        }

        LessonProgress progress = new LessonProgress();
        progress.setEnrollment(enrollment);
        progress.setLesson(lesson);
        progress.setCompletedAt(LocalDateTime.now());
        lessonProgressRepository.save(progress);

        enrollment.getCompletedLessons().add(progress);

        if (enrollment.getCompletedLessons().size() >= enrollment.getCourse().getTotalLessons()) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollment.setCompletedAt(LocalDateTime.now());
        }

        return ProgressResponse.from(enrollment);
    }

    @Transactional(readOnly = true)
    public ProgressResponse getProgress(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Запись не найдена"));
        return ProgressResponse.from(enrollment);
    }

    @Transactional(readOnly = true)
    public EnrollmentResponseWithDetails getEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(
                () -> new EnrollmentNotFoundException("Запись не найдена")
        );
        return EnrollmentResponseWithDetails.from(enrollment);
    }

    @Transactional
    public EnrollmentResponse dropEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(
                () -> new EnrollmentNotFoundException("Запись не найдена")
        );
        if(enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new IllegalStateException("Отписаться можно только от активной записи");
        }
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        return EnrollmentResponse.from(enrollment);
    }
}