package com.example.lmslite.repositories;

import com.example.lmslite.enums.CourseStatus;
import com.example.lmslite.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStatus(CourseStatus courseStatus);
    List<Course>  findByAuthorId(Long authorId);
    List<Course>  findByTitleContainingIgnoreCase(String title);

}
