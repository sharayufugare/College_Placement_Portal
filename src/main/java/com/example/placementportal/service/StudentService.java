/*package com.example.placementportal.service;

import com.example.placementportal.model.Student;
import java.util.Optional;
import java.util.List;

public interface StudentService {
    Student registerStudent(Student student);
    Optional<Student> loginStudent(String email, String password);
    Optional<Student> getStudentById(Long id);
    List<Student> getAllStudents();
    Student updateStudent(Long id, Student updatedStudent);
}
*/
package com.example.placementportal.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.placementportal.model.Student;
import com.example.placementportal.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repo;
    private final PasswordEncoder encoder;

    public Student createStudent(Student s) {
        if (!StringUtils.hasText(s.getPrn())) {
            throw new IllegalArgumentException("PRN is required");
        }
        if (!StringUtils.hasText(s.getEmail())) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!StringUtils.hasText(s.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }
        if (repo.findByPrn(s.getPrn()).isPresent()) {
            throw new IllegalArgumentException("PRN already exists");
        }
        if (repo.findByEmail(s.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (!StringUtils.hasText(s.getRole())) {
            s.setRole("STUDENT");
        }
        s.setPassword(encoder.encode(s.getPassword()));
        return repo.save(s);
    }

    public Optional<Student> login(String email, String password) {
        Optional<Student> s = repo.findByEmail(email);
        if (s.isPresent() && encoder.matches(password, s.get().getPassword())) {
            return s;
        }
        return Optional.empty();
    }

    public Optional<Student> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public Student updateProfile(Long id, Student updated) {
        Student s = repo.findById(id).orElseThrow();

        s.setName(updated.getName());
        s.setSkills(updated.getSkills());
        s.setCriteria(updated.getCriteria());
        s.setCgpa(updated.getCgpa());
        s.setBranch(updated.getBranch());
        s.setYear(updated.getYear());
        s.setPhone(updated.getPhone());
        s.setLinkedin(updated.getLinkedin());
        s.setGithub(updated.getGithub());
        s.setGender(updated.getGender());
        s.setAchievements(updated.getAchievements());

        return repo.save(s);
    }

    public Student saveResume(Long id, String path) {
        Student s = repo.findById(id).orElseThrow();
        s.setResumePath(path);
        return repo.save(s);
    }

    public Student savePhoto(Long id, String path) {
        Student s = repo.findById(id).orElseThrow();
        s.setPhotoPath(path);
        return repo.save(s);
    }
}
