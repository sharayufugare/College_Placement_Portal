package com.example.placementportal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "skills")
    private String skills;

    @Column(name = "criteria")
    private String criteria;

    @Column(name = "status")
    private String status;

    private String registrationLink;
    private String branch;
    private String eligibleBatches;
    private String degree;
    private String stipend;
    private String location;
    private String workMode;
    private String specification;
    private String driveDate;
    private String deadline;
    private String internPeriod;

    public Company() {}

    public Company(String companyName, String skills, String criteria, String status) {
        this.companyName = companyName;
        this.skills = skills;
        this.criteria = criteria;
        this.status = status;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getCriteria() { return criteria; }
    public void setCriteria(String criteria) { this.criteria = criteria; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRegistrationLink() { return registrationLink; }
    public void setRegistrationLink(String registrationLink) { this.registrationLink = registrationLink; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public String getEligibleBatches() { return eligibleBatches; }
    public void setEligibleBatches(String eligibleBatches) { this.eligibleBatches = eligibleBatches; }
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public String getStipend() { return stipend; }
    public void setStipend(String stipend) { this.stipend = stipend; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getWorkMode() { return workMode; }
    public void setWorkMode(String workMode) { this.workMode = workMode; }
    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }
    public String getDriveDate() { return driveDate; }
    public void setDriveDate(String driveDate) { this.driveDate = driveDate; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getInternPeriod() { return internPeriod; }
    public void setInternPeriod(String internPeriod) { this.internPeriod = internPeriod; }
}
