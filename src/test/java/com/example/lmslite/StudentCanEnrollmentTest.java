package com.example.lmslite;

import com.example.lmslite.model.Course;
import com.example.lmslite.model.Enrollment;
import com.example.lmslite.model.Student;
import com.example.lmslite.repositories.CourseRepository;
import com.example.lmslite.repositories.EnrollmentRepository;
import com.example.lmslite.repositories.StudentRepository;
import com.example.lmslite.services.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentCanEnrollmentTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    void enrollStudent_success() {
         Student student = new Student();
         Course course = new Course();
         course.publish();

         when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
         when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
         when(enrollmentRepository.save(any(Enrollment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Enrollment result = enrollmentService.enrollStudent(1L, 2L);

        assertSame(student, result.getStudent());
        assertSame(course, result.getCourse());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }
}
