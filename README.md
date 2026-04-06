# 🎓 College Placement Portal — PCCOER

[![Java CI Build](https://github.com/sharayufugare/College_Placement_Portal/actions/workflows/ci.yml/badge.svg)](https://github.com/sharayufugare/College_Placement_Portal/actions/workflows/ci.yml)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=java)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)](https://www.postgresql.org/)
[![Deployed on Render](https://img.shields.io/badge/Deployed%20on-Render-46E3B7?logo=render)](https://college-placement-portal-rfka.onrender.com)

A full-stack **Java Spring Boot** web application for managing campus placements at PCCOER. It supports student registration & login, company listings, job applications, TNP admin management,and placement analytics — all deployed on Render with a PostgreSQL database.

🔗 **Live Demo:** [https://college-placement-portal-rfka.onrender.com](https://college-placement-portal-3i0d.onrender.com)

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [API Reference](#-api-reference)
- [Getting Started](#-getting-started)
- [Environment Variables](#-environment-variables)
- [Deployment on Render](#-deployment-on-render)

---

## ✨ Features

### 👨‍🎓 Student Portal
- Register with PRN, email (`@pccoer.in`), and password
- Login with email/password or Google OAuth
- View and update profile (CGPA, skills, branch, year, etc.)
- Upload resume and profile photo
- Browse eligible companies based on CGPA and skills
- Apply to companies and track application status

### 🏢 TNP Admin Portal
- Secure admin registration (coordinator onboarding)
- Login with username/email and password or Google OAuth
- Add, update, and delete company listings
- View all student profiles and applications
- Select/shortlist students for companies
- Placement analytics dashboard

### 📊 Analytics
- Placement statistics by year
- Top recruiting companies
- Offer count and student placement summaries

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.4.5 |
| Security | Spring Security, JWT, Google OAuth2 |
| Database | PostgreSQL (Render managed) |
| ORM | Hibernate / Spring Data JPA |
| Frontend | HTML, CSS, Tailwind CSS, Vanilla JS |
| Build Tool | Maven |
| Deployment | Render (Docker-based) |
| CI/CD | GitHub Actions |

---

## 📁 Project Structure

```
placementportal/
├── src/
│   ├── main/
│   │   ├── java/com/example/placementportal/
│   │   │   ├── controller/       # REST API controllers
│   │   │   ├── model/            # JPA Entity classes
│   │   │   ├── repository/       # Spring Data JPA Repositories
│   │   │   ├── service/          # Business logic
│   │   │   ├── security/         # JWT, Google OAuth, Filters
│   │   │   └── dto/              # Data Transfer Objects
│   │   └── resources/
│   │       ├── static/           # Frontend HTML/JS pages
│   │       └── application.properties
├── Dockerfile
├── pom.xml
└── .github/
    └── workflows/
        └── ci.yml                # GitHub Actions CI
```

---

## 📡 API Reference

### 🔐 Auth — `/auth`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | Student login (email + password) |
| POST | `/auth/google` | Student Google OAuth login |

### 👨‍🎓 Student — `/student`

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/student/register` | Register a new student | No |
| POST | `/student/login` | Student login | No |
| GET | `/student/all` | Get all students | No |
| GET | `/student/dashboard/{id}` | Get student profile | Yes |
| PUT | `/student/update/{id}` | Update student profile | Yes |
| POST | `/student/uploadResume/{id}` | Upload resume | Yes |
| POST | `/student/uploadPhoto/{id}` | Upload profile photo | Yes |
| GET | `/student/eligible/{studentId}` | Get eligible companies | Yes |
| GET | `/student/applications/{studentId}` | Get my applications | Yes |

### 🏢 Company — `/api/company`

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/company/add` | Add new company | Admin |
| GET | `/api/company/all` | Get all companies | No |
| GET | `/api/company/search` | Search companies | No |
| PUT | `/api/company/update/{id}` | Update company | Admin |
| DELETE | `/api/company/delete/{id}` | Delete company | Admin |
| GET | `/api/company/{id}/eligible-students` | Eligible students | Admin |
| GET | `/api/company/{id}/applications` | Company applications | Admin |
| PUT | `/api/company/{cId}/applications/{aId}/select` | Select student | Admin |

### 👩‍💼 Admin — `/api/admin`

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/admin/register` | Register TNP coordinator | No |
| POST | `/api/admin/login` | Admin login | No |
| POST | `/api/admin/google-login` | Admin Google login | No |
| GET | `/api/admin/students` | Get all students | Admin |
| GET | `/api/admin/coordinators` | Get all coordinators | Admin |
| GET | `/api/admin/applications/{companyId}` | Applications by company | Admin |

### 📊 Analytics — `/api/tnp`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tnp/analytics` | Placement analytics summary |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL

### Run Locally

**1. Clone the repository**
```bash
git clone https://github.com/sharayufugare/College_Placement_Portal.git
cd College_Placement_Portal/placementportal
```

**2. Set environment variables**
```bash
# Windows
set SPRING_DATASOURCE_URL=jdbc:postgresql://<external-host>:5432/placement_portal_okd1
set SPRING_DATASOURCE_USERNAME=placement_portal_okd1_user
set SPRING_DATASOURCE_PASSWORD=your_password

# Mac / Linux
export SPRING_DATASOURCE_URL=jdbc:postgresql://<external-host>:5432/placement_portal_okd1
export SPRING_DATASOURCE_USERNAME=placement_portal_okd1_user
export SPRING_DATASOURCE_PASSWORD=your_password
```

**3. Run**
```bash
mvn spring-boot:run
```

**4. Open browser**
```
http://localhost:8080
```

---

## 🔑 Environment Variables

Set these in Render → Web Service → **Environment** tab:

| Variable | Description |
|----------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<internal-host>:5432/placement_portal_okd1` |
| `SPRING_DATASOURCE_USERNAME` | Render DB username |
| `SPRING_DATASOURCE_PASSWORD` | Render DB password |
| `GOOGLE_OAUTH_CLIENT_ID` | Google OAuth Client ID (optional) |
| `JWT_SECRET` | Secret key for JWT signing |

> ⚠️ Do NOT add `PORT` manually — Render injects it automatically.

---

## ☁️ Deployment on Render

1. Push code to GitHub
2. Go to [render.com](https://render.com) → New → **Web Service**
3. Connect your GitHub repo
4. Set **Root Directory** to `placementportal`
5. Set **Environment** to `Docker`
6. Add all environment variables above
7. Click **Deploy**

Every push to `main` triggers a new deployment automatically.

---

## 🖥️ Pages

| Page | URL |
|------|-----|
| Home | `/HomePage.html` |
| Student Login | `/student_login.html` |
| Student Register | `/registration.html` |
| Student Dashboard | `/student_dashboard.html` |
| TNP Admin Login | `/tnp_admin_login.html` |
| TNP Admin Register | `/tnp_registration.html` |
| TNP Dashboard | `/tnp_dashboard.html` |
| Analytics | `/analytics.html` |

---

## 👩‍💻 Author
[Sharayu Fuagre](https://github.com/sharayufugare)
[Shaily Gujarathi](https://github.com/shaily0407)


---
