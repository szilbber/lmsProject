package com.example.lmslite.dto;

import com.example.lmslite.model.Certificate;

import java.time.LocalDate;

public record CertificateResponse(
        Long id,
        String studentName,
        String courseTitle,
        LocalDate issuedAt,
        String uniqueCode
) {
    public static CertificateResponse from(Certificate certificate) {
        return new CertificateResponse(
                certificate.getId(),
                certificate.getStudent().getFullName(),
                certificate.getCourse().getTitle(),
                certificate.getIssuedAt(),
                certificate.getUniqueCode()
        );
    }
}