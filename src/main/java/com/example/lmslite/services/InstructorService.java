package com.example.lmslite.services;

import com.example.lmslite.dto.CourseResponse;
import com.example.lmslite.dto.CreateInstructorRequest;
import com.example.lmslite.dto.InstructorResponse;
import com.example.lmslite.exception.EmailAlreadyUsedException;
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
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public InstructorResponse createInstructor(CreateInstructorRequest request)
    {
        if(instructorRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyUsedException("Этот имейл уже используется");
        }
        Instructor instructor = new Instructor();
        instructor.setFirstName(request.firstName());
        instructor.setLastName(request.lastName());
        instructor.setEmail(request.email());
        instructor.setBio(request.bio());
        return InstructorResponse.from(instructorRepository.save(instructor));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getInstructorCourses(Long id) {
        if(!instructorRepository.existsById(id)) {
            throw new InstructorNotFoundException("Инструктор не найден");
        }
        List<Course> courses = courseRepository.findByAuthorId(id);
        return courses.stream().map(CourseResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public InstructorResponse findById(Long id){
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new InstructorNotFoundException("Инструктор не найден"));
        return InstructorResponse.from(instructor);
    }
}