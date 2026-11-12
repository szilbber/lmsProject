package com.example.lmslite.repositories;

import com.example.lmslite.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByStudentId(Long studentId);
    Optional<Certificate> findByUniqueCode(String uniqueCode);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

}