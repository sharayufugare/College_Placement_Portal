package com.example.placementportal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HomeController {

    @GetMapping("/")
    public void home(HttpServletResponse response) throws IOException {
        response.sendRedirect("/HomePage.html");
    }

    @GetMapping("/test")
    public String test() {
        return "Test API Working";
    }
}