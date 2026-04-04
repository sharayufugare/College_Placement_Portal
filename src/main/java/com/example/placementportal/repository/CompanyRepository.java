package com.example.placementportal.repository;

import com.example.placementportal.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findBySkillsContaining(String skills);
    List<Company> findByCriteriaContaining(String criteria);
    List<Company> findBySkillsContainingAndCriteriaContaining(String skills, String criteria);
}


