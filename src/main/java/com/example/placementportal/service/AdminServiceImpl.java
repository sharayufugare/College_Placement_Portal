package com.example.placementportal.service;

import com.example.placementportal.model.Admin;
import com.example.placementportal.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<Admin> login(String username, String password) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isEmpty() && username.contains("@")) {
            admin = adminRepository.findByEmail(username);
        }
        if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword())) {
            return admin;
        }
        return Optional.empty();
    }

    @Override
    public Admin register(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
}

