package com.example.lmslite.services;

import com.example.lmslite.dto.CourseDetailsWithLessonsResponse;
import com.example.lmslite.dto.CourseResponse;
import com.example.lmslite.dto.CreateCourseRequest;
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
    public CourseResponse createCourse(CreateCourseRequest request){

        Instructor author = instructorRepository.findById(request.instructorId())
                .orElseThrow(() -> new InstructorNotFoundException("Инструктор не найден")
        );
        Course newCourse = new Course();
        newCourse.setTitle(request.title());
        newCourse.setDescription(request.description());
        newCourse.setStatus(CourseStatus.DRAFT);
        newCourse.setAuthor(author);
        return CourseResponse.from(courseRepository.save(newCourse));
    }

    @Transactional(readOnly = true)
    public CourseDetailsWithLessonsResponse getCourseById(Long id){
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Курс не найден")
        );
        return CourseDetailsWithLessonsResponse.from(course);
    }

    @Transactional
    public void deleteCourse(Long id){
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new CourseNotFoundException("Курс не найден"));
        if(!course.getStatus().equals(CourseStatus.DRAFT)){
            throw new IllegalStateException("Удалить можно только курс в статусе DRAFT");
        }
        courseRepository.delete(course);
    }

    @Transactional
    public CourseResponse publishCourse(Long courseId){
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("Курс не найден")
        );
        if(!course.getStatus().equals(CourseStatus.DRAFT)){
            throw new IllegalStateException("Опубликовать можно только курс в статусе DRAFT");
        }
        if (course.getTotalLessons() == 0) {
            throw new IllegalStateException("Нельзя опубликовать курс без уроков");
        }
        course.publish();
        return CourseResponse.from(course);
    }

    @Transactional
    public CourseResponse archiveCourse(Long courseId){
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("Курс не найден")
        );
        course.setStatus(CourseStatus.ARCHIVED);
        return CourseResponse.from(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getPublishedCourses(){
        return courseRepository.findByStatus(CourseStatus.PUBLISHED).stream()
                .map(CourseResponse::from)
                .toList();
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CreateCourseRequest request){
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new CourseNotFoundException("Курс не найден")
        );
        Instructor instructor = instructorRepository.findById(request.instructorId()).orElseThrow(
                () -> new InstructorNotFoundException("Инструктор не найден")
        );
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setAuthor(instructor);

        return CourseResponse.from(course);
    }

}
