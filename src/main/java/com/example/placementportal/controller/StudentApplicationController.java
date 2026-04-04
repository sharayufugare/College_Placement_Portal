package com.example.placementportal.controller;

import com.example.placementportal.model.*;
import com.example.placementportal.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*")
public class StudentApplicationController {

    private final StudentRepository studentRepo;
    private final CompanyRepository companyRepo;
    private final ApplicationRepository applicationRepo;

    public StudentApplicationController(
            StudentRepository studentRepo,
            CompanyRepository companyRepo,
            ApplicationRepository applicationRepo
    ) {
        this.studentRepo = studentRepo;
        this.companyRepo = companyRepo;
        this.applicationRepo = applicationRepo;
    }

    @PostMapping("/apply/{companyId}")
    public ResponseEntity<?> applyToCompany(
            @PathVariable Long companyId,
            @RequestParam Long studentId
    ) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (applicationRepo.findByStudentIdAndCompanyId(studentId, companyId).isPresent()) {
            return ResponseEntity.badRequest().body("Already applied");
        }

        Application application = new Application(student, company);
        applicationRepo.save(application);

        return ResponseEntity.ok("Applied successfully");
    }
}

