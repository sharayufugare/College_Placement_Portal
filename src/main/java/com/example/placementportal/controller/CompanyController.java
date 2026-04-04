package com.example.placementportal.controller;

import com.example.placementportal.model.Company;
import com.example.placementportal.model.Student;
import com.example.placementportal.repository.ApplicationRepository;
import com.example.placementportal.repository.CompanyRepository;
import com.example.placementportal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    // ✅ 1. Add company
    @PostMapping("/add")
    public Company addCompany(@RequestParam String companyName,
                              @RequestParam(required = false, defaultValue = "") String skills,
                              @RequestParam(required = false, defaultValue = "") String criteria,
                              @RequestParam(required = false, defaultValue = "Open") String status,
                              @RequestParam(required = false) String registrationLink,
                              @RequestParam(required = false) String branch,
                              @RequestParam(required = false) String eligibleBatches,
                              @RequestParam(required = false) String degree,
                              @RequestParam(required = false) String stipend,
                              @RequestParam(required = false) String location,
                              @RequestParam(required = false) String workMode,
                              @RequestParam(required = false) String specification,
                              @RequestParam(required = false) String driveDate,
                              @RequestParam(required = false) String deadline,
                              @RequestParam(required = false) String internPeriod) {
        Company company = new Company(companyName, skills, criteria, status);
        company.setRegistrationLink(registrationLink);
        company.setBranch(branch);
        company.setEligibleBatches(eligibleBatches);
        company.setDegree(degree);
        company.setStipend(stipend);
        company.setLocation(location);
        company.setWorkMode(workMode);
        company.setSpecification(specification);
        company.setDriveDate(driveDate);
        company.setDeadline(deadline);
        company.setInternPeriod(internPeriod);
        return companyRepository.save(company);
    }


    // ✅ 2. Get all companies
    @GetMapping("/all")
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // ✅ 3. Search by skill or criteria
    @GetMapping("/search")
    public List<Company> searchCompany(@RequestParam(required = false) String skill,
                                       @RequestParam(required = false) String criteria) {
        if (skill != null && criteria != null)
            return companyRepository.findBySkillsContainingAndCriteriaContaining(skill, criteria);
        else if (skill != null)
            return companyRepository.findBySkillsContaining(skill);
        else if (criteria != null)
            return companyRepository.findByCriteriaContaining(criteria);
        else
            return companyRepository.findAll();
    }

    // ✅ 4. Update company by ID
    @PutMapping("/update/{id}")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany) {
        Optional<Company> existing = companyRepository.findById(id);
        if (existing.isPresent()) {
            Company company = existing.get();
            company.setCompanyName(updatedCompany.getCompanyName());
            company.setSkills(updatedCompany.getSkills());
            company.setCriteria(updatedCompany.getCriteria());
            company.setStatus(updatedCompany.getStatus());
            company.setRegistrationLink(updatedCompany.getRegistrationLink());
            company.setBranch(updatedCompany.getBranch());
            company.setEligibleBatches(updatedCompany.getEligibleBatches());
            company.setDegree(updatedCompany.getDegree());
            company.setStipend(updatedCompany.getStipend());
            company.setLocation(updatedCompany.getLocation());
            company.setWorkMode(updatedCompany.getWorkMode());
            company.setSpecification(updatedCompany.getSpecification());
            company.setDriveDate(updatedCompany.getDriveDate());
            company.setDeadline(updatedCompany.getDeadline());
            company.setInternPeriod(updatedCompany.getInternPeriod());
            return companyRepository.save(company);
        }
        return null;
    }

    // ✅ 5. Delete company by ID
    @DeleteMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id) {
        companyRepository.deleteById(id);
        return "Company deleted with ID: " + id;
    }

    @GetMapping("/{companyId}/eligible-students")
    public List<Student> getEligibleStudents(@PathVariable Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        double minCgpa = parseCgpa(company.getCriteria());
        return studentRepository.findAll().stream()
                .filter(student -> parseCgpa(student.getCgpa()) >= minCgpa)
                .toList();
    }

    @GetMapping("/{companyId}/applications")
    public ResponseEntity<?> getCompanyApplications(@PathVariable Long companyId) {
        return ResponseEntity.ok(applicationRepository.findByCompanyId(companyId));
    }

    @PutMapping("/{companyId}/applications/{applicationId}/select")
    public ResponseEntity<String> selectStudent(@PathVariable Long companyId, @PathVariable Long applicationId) {
        var app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        if (!app.getCompany().getId().equals(companyId)) {
            return ResponseEntity.badRequest().body("Application does not belong to this company");
        }
        app.setStatus("Selected");
        applicationRepository.save(app);
        return ResponseEntity.ok("Student selected");
    }

    private double parseCgpa(String text) {
        if (text == null || text.isBlank()) {
            return 0.0;
        }
        String numeric = text.replaceAll("[^0-9.]", "");
        if (numeric.isBlank()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(numeric);
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }
}



