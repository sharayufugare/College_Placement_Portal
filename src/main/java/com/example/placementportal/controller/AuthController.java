package com.example.placementportal.controller;

import com.example.placementportal.model.Student;
import com.example.placementportal.security.GoogleTokenVerifier;
import com.example.placementportal.security.JwtUtil;
import com.example.placementportal.service.StudentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final StudentService studentService;
    private final JwtUtil jwtUtil;
    private final GoogleTokenVerifier googleTokenVerifier;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        Student student = studentService
                .login(req.getEmail(), req.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Check domain restriction
        if (!req.getEmail().endsWith("@pccoer.in")) {
            throw new RuntimeException("Use official college email only!");
        }

        String token = jwtUtil.generateToken(req.getEmail());

        return new LoginResponse(student.getId(), token, student.getName(), student.getEmail());
    }

    @PostMapping("/google")
    public LoginResponse loginWithGoogle(@RequestBody GoogleLoginRequest req) {
        GoogleTokenVerifier.GoogleTokenInfo tokenInfo = googleTokenVerifier.verifyIdToken(req.getIdToken());
        Student student = studentService.findByEmail(tokenInfo.email())
                .orElseThrow(() -> new RuntimeException("No registered student found for this college email"));

        String token = jwtUtil.generateToken(student.getEmail());
        return new LoginResponse(student.getId(), token, student.getName(), student.getEmail());
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class GoogleLoginRequest {
        private String idToken;
    }

    @Data
    public static class LoginResponse {
        private Long studentId;
        private String token;
        private String name;
        private String email;

        public LoginResponse(Long id, String token, String name, String email) {
            this.studentId = id;
            this.token = token;
            this.name = name;
            this.email = email;
        }
    }
}
