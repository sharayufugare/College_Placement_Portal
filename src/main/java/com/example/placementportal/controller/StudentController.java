package com.example.placementportal.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.placementportal.dto.ApplicationDTO;
import com.example.placementportal.model.Company;
import com.example.placementportal.model.Student;
import com.example.placementportal.repository.ApplicationRepository;
import com.example.placementportal.repository.CompanyRepository;
import com.example.placementportal.repository.StudentRepository;
import com.example.placementportal.service.StudentService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CompanyRepository companyRepository;


    private final StudentService studentService;
    private final StudentRepository repo;

    // ---------------- REGISTER ----------------
    @PostMapping("/register")
    public Student register(@RequestBody Student s) {
        try {
            return studentService.createStudent(s);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to register student");
        }
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Student request) {
        Optional<Student> s = studentService.login(request.getEmail(), request.getPassword());
        if (s.isPresent()) {
            return ResponseEntity.ok(s.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // ---------------- DASHBOARD ----------------
    @GetMapping("/dashboard/{id}")
    public Student dashboard(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    // ---------------- UPDATE PROFILE ----------------
    @PutMapping("/update/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student updated) {
        return studentService.updateProfile(id, updated);
    }

    // ---------------- UPLOAD RESUME ----------------
    @PostMapping("/uploadResume/{id}")
    public ResponseEntity<?> uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.home") + "/uploads/resumes/";
            new File(uploadDir).mkdirs();
            String filename = id + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadDir + filename));
            studentService.saveResume(id, "/uploads/resumes/" + filename);
            return ResponseEntity.ok("Resume uploaded");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("ERROR: " + e.getMessage());
        }
    }


    // ---------------- UPLOAD PHOTO ----------------
    @PostMapping("/uploadPhoto/{id}")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.home") + "/uploads/photos/";
            new File(uploadDir).mkdirs();
            String filename = id + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadDir + filename));
            studentService.savePhoto(id, "/uploads/photos/" + filename);
            return ResponseEntity.ok("Photo uploaded");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("ERROR: " + e.getMessage());
        }
    }

    // ---------------- VIEW ELIGIBLE COMPANIES ----------------
    @GetMapping("/eligible/{studentId}")
    public List<Company> getEligibleCompanies(@PathVariable Long studentId) {
        Student student = repo.findById(studentId).orElseThrow();
        List<Company> allCompanies = companyRepository.findAll();
        List<Company> eligible = new ArrayList<>();

        double studentCgpa = parseCgpa(student.getCgpa());
        String[] studentSkills = student.getSkills() != null ? student.getSkills().split(",") : new String[0];

        for (Company company : allCompanies) {
            double minCgpa = parseCgpa(company.getCriteria());
            if (studentCgpa >= minCgpa) {
                String[] requiredSkills = company.getSkills() != null ? company.getSkills().split(",") : new String[0];
                boolean hasAllSkills = true;
                for (String reqSkill : requiredSkills) {
                    boolean hasSkill = false;
                    for (String studSkill : studentSkills) {
                        if (studSkill.trim().equalsIgnoreCase(reqSkill.trim())) {
                            hasSkill = true;
                            break;
                        }
                    }
                    if (!hasSkill) {
                        hasAllSkills = false;
                        break;
                    }
                }
                if (hasAllSkills) {
                    eligible.add(company);
                }
            }
        }

        return eligible;
    }

    private double parseCgpa(String text) {
        if (text == null || text.isBlank()) {
            return 0.0;
        }
        Matcher matcher = Pattern.compile("(\\d+(?:\\.\\d+)?)").matcher(text);
        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1));
            } catch (NumberFormatException ignored) {
                return 0.0;
            }
        }
        return 0.0;
    }

  @GetMapping("/applications/{studentId}")
  public List<ApplicationDTO> getMyApplications(@PathVariable Long studentId) {
      return applicationRepository.findByStudentId(studentId)
              .stream()
              .map(app -> new ApplicationDTO(
                      app.getId(),
                      app.getStudent().getName(),
                      app.getCompany().getCompanyName(),
                      app.getStatus(),
                      app.getAppliedDate()
              ))
              .toList();
  }

}

