package com.example.lmslite.services;

import com.example.lmslite.enums.CourseStatus;
import com.example.lmslite.exception.CourseNotFoundException;
import com.example.lmslite.exception.InstructorNotFoundException;
import com.example.lmslite.model.Course;
import com.example.lmslite.model.Instructor;
import com.example.lmslite.repositories.CourseRepository;
import com.example.lmslite.repositories.InstructorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Transactional
    public Course createCourse(String title, String description, Long instructorId){

        Instructor author = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new InstructorNotFoundException("Инструктор не найден")
        );
        Course newCourse = new Course();
        newCourse.setTitle(title);
        newCourse.setDescription(description);
        newCourse.setStatus(CourseStatus.DRAFT);
        newCourse.setAuthor(author);
        return courseRepository.save(newCourse);
    }

    @Transactional
    public void publishCourse(Long courseId){
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("Курс не найден")
        );
        if (course.getTotalLessons() == 0) {
            throw new IllegalStateException("Нельзя опубликовать курс без уроков");
        }
        course.publish();
    }

    @Transactional(readOnly = true)
    public List<Course> getPublishedCourses(){

        return courseRepository.findByStatus(CourseStatus.PUBLISHED);
    }

    @Transactional(readOnly = true)
    public Course getCourseById(Long id){
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Курс не найден")
        );
    }

}
