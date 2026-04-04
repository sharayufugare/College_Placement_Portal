package com.example.placementportal.dto;

import java.time.LocalDate;

public class ApplicationDTO {
    private Long id;
    private String studentName;
    private String companyName;
    private String status;
    private LocalDate appliedDate;

    public ApplicationDTO(Long id,String studentName, String companyName, String status, LocalDate appliedDate) {
       this.id=id;
        this.studentName = studentName;
        this.companyName = companyName;
        this.status = status;
        this.appliedDate = appliedDate;
    }
    public Long getId() { return id; }
    public String getStudentName() { return studentName; }
    public String getCompanyName() { return companyName; }
    public String getStatus() { return status; }
    public LocalDate getAppliedDate() { return appliedDate; }



}

