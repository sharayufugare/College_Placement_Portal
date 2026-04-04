/*package com.example.placementportal.controller;

import com.example.placementportal.model.Application;
import com.example.placementportal.repository.ApplicationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tnp")
@CrossOrigin
public class TnpApiController {

    private final ApplicationRepository applicationRepo;

    public TnpApiController(ApplicationRepository applicationRepo) {
        this.applicationRepo = applicationRepo;
    }

    // 🔹 All applications
    @GetMapping("/applications")
    public List<Application> getAllApplications() {
        return applicationRepo.findAll();
    }

    // 🔹 Company wise
    @GetMapping("/applications/company/{companyId}")
    public List<Application> getByCompany(@PathVariable Long companyId) {
        return applicationRepo.findByCompanyId(companyId);
    }
}
*/
package com.example.placementportal.controller;

import com.example.placementportal.dto.ApplicationDTO;
import com.example.placementportal.model.Application;
import com.example.placementportal.repository.ApplicationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tnp")
@CrossOrigin(origins = "*")
public class TnpApiController {

    private final ApplicationRepository applicationRepo;

    public TnpApiController(ApplicationRepository applicationRepo) {
        this.applicationRepo = applicationRepo;
    }

    @GetMapping("/applications")
    public List<ApplicationDTO> getAllApplications() {
        return applicationRepo.findAll()
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

    @GetMapping("/applications/summary")
    public AnalyticsSummary getApplicationsSummary() {
        List<Application> applications = applicationRepo.findAll();

        Set<Long> uniqueStudentIds = applications.stream()
                .map(app -> app.getStudent().getId())
                .collect(Collectors.toSet());
        long totalStudents = uniqueStudentIds.size();

        long totalPlaced = applications.stream()
                .filter(app -> isPlaced(app.getStatus()))
                .count();

        long totalInternships = applications.stream()
                .filter(app -> isInternship(app.getStatus()))
                .count();

        double placementRate = totalStudents > 0 ? (totalPlaced * 100.0 / totalStudents) : 0.0;

        Map<String, Long> placementsByYear = applications.stream()
                .filter(app -> isPlaced(app.getStatus()))
                .filter(app -> app.getAppliedDate() != null)
                .collect(Collectors.groupingBy(app -> String.valueOf(app.getAppliedDate().getYear()), Collectors.counting()));

        Map<String, Long> internshipsByYear = applications.stream()
                .filter(app -> isInternship(app.getStatus()))
                .filter(app -> app.getAppliedDate() != null)
                .collect(Collectors.groupingBy(app -> String.valueOf(app.getAppliedDate().getYear()), Collectors.counting()));

        Map<String, Long> studentsByYear = applications.stream()
                .filter(app -> app.getAppliedDate() != null)
                .collect(Collectors.groupingBy(app -> String.valueOf(app.getAppliedDate().getYear()),
                        Collectors.mapping(app -> app.getStudent().getId(), Collectors.toSet())))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (long) entry.getValue().size()));

        List<YearlyCount> placementTrends = placementsByYear.entrySet().stream()
                .map(e -> new YearlyCount(e.getKey(), e.getValue()))
                .sorted((a, b) -> a.year().compareTo(b.year()))
                .toList();

        List<YearlyCount> internshipTrends = internshipsByYear.entrySet().stream()
                .map(e -> new YearlyCount(e.getKey(), e.getValue()))
                .sorted((a, b) -> a.year().compareTo(b.year()))
                .toList();

        List<YearlyCount> studentsTrends = studentsByYear.entrySet().stream()
                .map(e -> new YearlyCount(e.getKey(), e.getValue()))
                .sorted((a, b) -> a.year().compareTo(b.year()))
                .toList();

        List<TopCompany> topCompanies = applications.stream()
                .filter(app -> isPlaced(app.getStatus()))
                .collect(Collectors.groupingBy(app -> app.getCompany().getCompanyName(), Collectors.counting()))
                .entrySet().stream()
                .map(e -> new TopCompany(e.getKey(), e.getValue()))
                .sorted((a, b) -> Long.compare(b.placedCount(), a.placedCount()))
                .limit(6)
                .toList();

        return new AnalyticsSummary(totalStudents, totalPlaced, totalInternships, placementRate, placementTrends, internshipTrends, studentsTrends, topCompanies);
    }

    private boolean isPlaced(String status) {
        return status != null && (status.equalsIgnoreCase("selected") || status.equalsIgnoreCase("placed") || status.equalsIgnoreCase("offer"));
    }

    private boolean isInternship(String status) {
        return status != null && status.toLowerCase().contains("intern");
    }

    public static record AnalyticsSummary(
            long totalStudents,
            long totalPlaced,
            long totalInternships,
            double placementRate,
            List<YearlyCount> placementTrends,
            List<YearlyCount> internshipTrends,
            List<YearlyCount> studentsTrends,
            List<TopCompany> topCompanies
    ) {}

    public static record YearlyCount(String year, long value) {}

    public static record TopCompany(String companyName, long placedCount) {}

    @PutMapping("/applications/{id}/accept")
    public ResponseEntity<String> acceptApplication(@PathVariable Long id) {

        Application app = applicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus("Selected");
        applicationRepo.save(app);

        return ResponseEntity.ok("Application Selected");
    }

    @PutMapping("/applications/{id}/reject")
    public ResponseEntity<String> rejectApplication(@PathVariable Long id) {

        Application app = applicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus("Rejected");
        applicationRepo.save(app);

        return ResponseEntity.ok("Application Rejected");
    }

    @PutMapping("/applications/{id}/placed")
    public ResponseEntity<String> markPlaced(@PathVariable Long id) {
        Application app = applicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus("Placed");
        applicationRepo.save(app);

        return ResponseEntity.ok("Application marked as Placed");
    }


}

