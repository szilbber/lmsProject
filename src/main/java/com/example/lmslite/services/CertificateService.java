package com.example.lmslite.services;

import com.example.lmslite.dto.CertificateResponse;
import com.example.lmslite.exception.CertificateNotFoundException;
import com.example.lmslite.exception.StudentNotFoundException;
import com.example.lmslite.model.Certificate;
import com.example.lmslite.model.Enrollment;
import com.example.lmslite.repositories.CertificateRepository;
import com.example.lmslite.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CertificateService {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ru"));

    private final CertificateRepository certificateRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void issueCertificate(Enrollment enrollment) {
        Long studentId = enrollment.getStudent().getId();
        Long courseId = enrollment.getCourse().getId();
        if (certificateRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            return;
        }
        Certificate certificate = new Certificate();
        certificate.setStudent(enrollment.getStudent());
        certificate.setCourse(enrollment.getCourse());
        certificate.setIssuedAt(LocalDate.now());
        certificate.setUniqueCode(UUID.randomUUID().toString());
        certificateRepository.save(certificate);
    }

    @Transactional(readOnly = true)
    public List<CertificateResponse> getStudentCertificates(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Студент не найден");
        }
        return certificateRepository.findByStudentId(studentId).stream()
                .map(CertificateResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CertificateResponse checkCertificate(String code) {
        Certificate certificate = certificateRepository.findByUniqueCode(code)
                .orElseThrow(() -> new CertificateNotFoundException("Сертификат не найден"));
        return CertificateResponse.from(certificate);
    }

    @Transactional(readOnly = true)
    public String buildCertificateText(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException("Сертификат не найден"));
        return """
                ═══════════════════════════════════════════
                           СЕРТИФИКАТ О ПРОХОЖДЕНИИ
                ═══════════════════════════════════════════

                Настоящим подтверждается, что

                            %s

                успешно завершил(а) курс

                         "%s"

                Дата выдачи: %s
                Код сертификата: %s

                ═══════════════════════════════════════════
                """.formatted(
                certificate.getStudent().getFullName(),
                certificate.getCourse().getTitle(),
                certificate.getIssuedAt().format(DATE_FORMAT),
                certificate.getUniqueCode());
    }
}