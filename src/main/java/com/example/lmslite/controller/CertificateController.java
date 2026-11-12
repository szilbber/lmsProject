package com.example.lmslite.controller;

import com.example.lmslite.dto.CertificateResponse;
import com.example.lmslite.services.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping("/students/{id}/certificates")
    public List<CertificateResponse> getStudentCertificates(@PathVariable Long id){
        return certificateService.getStudentCertificates(id);
    }

    @GetMapping("/certificates/{code}")
    public CertificateResponse checkCertificate(@PathVariable String code){
        return certificateService.checkCertificate(code);
    }

    @GetMapping("/certificates/{id}/download")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long id){
        String text = certificateService.buildCertificateText(id);
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "plain", StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("certificate-" + id + ".txt")
                                .build()
                                .toString())
                .body(text.getBytes(StandardCharsets.UTF_8));
    }
}