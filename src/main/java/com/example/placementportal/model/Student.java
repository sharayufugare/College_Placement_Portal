package com.example.placementportal.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String prn;

    @Column(nullable = false)
    private String role = "STUDENT";

    @JsonAlias("fullName")
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String skills;
    private String criteria;
    private String cgpa;
    private String branch;
    @Column(name = "\"year\"")
    private String year;
    private String phone;
    private String linkedin;
    private String github;
    private String gender;
    private String achievements;

    private String resumePath;     // stored locally in /uploads/resumes
    private String photoPath;      // stored locally in /uploads/photos
    public String getName() {
        return name;
    }

}


