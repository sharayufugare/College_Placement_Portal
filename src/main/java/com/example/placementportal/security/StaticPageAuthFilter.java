package com.example.placementportal.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class StaticPageAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public StaticPageAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        boolean studentPage = "/student_dashboard.html".equals(path);
        boolean studentApi = path.startsWith("/student/")
                && !path.equals("/student/register")
                && !path.equals("/student/login")
                && !path.equals("/student/all");
        boolean adminPage = "/tnp_dashboard.html".equals(path);
        boolean adminApi = path.startsWith("/api/company/")
                && !(request.getMethod().equalsIgnoreCase("GET")
                && (path.equals("/api/company/all") || path.equals("/api/company/search")));

        if (studentPage || studentApi || adminPage || adminApi) {
            boolean requiresAdmin = adminPage || adminApi;
            String tokenName = requiresAdmin ? "tnp_admin_token" : "studentToken";
            String token = getCookieValue(request, tokenName);
            if (token == null || token.isBlank()) {
                token = extractBearerToken(request);
            }

            if (!isValidToken(token)) {
                if (studentApi || adminApi) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } else if (adminPage) {
                    response.sendRedirect("/tnp_admin_login.html");
                } else {
                    response.sendRedirect("/student_login.html");
                }
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return auth.substring(7).trim();
        }
        return null;
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        Optional<Cookie> cookie = Arrays.stream(cookies)
                .filter(c -> name.equals(c.getName()))
                .findFirst();
        return cookie.map(Cookie::getValue).orElse(null);
    }

    private boolean isValidToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        try {
            jwtUtil.extractEmail(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
