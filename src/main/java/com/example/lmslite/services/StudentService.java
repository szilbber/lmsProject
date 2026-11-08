package com.example.lmslite.services;

import com.example.lmslite.dto.CreateStudentRequest;
import com.example.lmslite.dto.StudentResponse;
import com.example.lmslite.exception.EmailAlreadyUsedException;
import com.example.lmslite.exception.StudentNotFoundException;
import com.example.lmslite.model.Student;
import com.example.lmslite.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public StudentResponse createStudent(CreateStudentRequest request) {
        if(studentRepository.existsByEmail(request.email())){
            throw new EmailAlreadyUsedException("Этот имейл уже используется");
        }
        Student student = new Student();
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        return StudentResponse.from(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден"));
        return StudentResponse.from(student);
    }

}