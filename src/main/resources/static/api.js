// static/api.js — shared API helpers (load config.js first for Live Server / split-origin dev)

function resolveApiUrl(path) {
    if (!path) return window.API_BASE || window.location.origin;
    if (path.startsWith("http")) return path;
    if (typeof window.apiUrl === "function") return window.apiUrl(path);
    const base = window.API_BASE || window.location.origin;
    return base + (path.startsWith("/") ? path : "/" + path);
}

// ---- LOGIN ----
async function loginUser(data) {
    const res = await fetch(resolveApiUrl("/auth/login"), {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });
    return res.json();
}

// ---- LEGACY: token-parameter variants (same-origin or with resolveApiUrl) ----
async function getStudentByIdWithToken(id, token) {
    const res = await fetch(resolveApiUrl(`/student/dashboard/${id}`), {
        headers: { "Authorization": "Bearer " + token }
    });
    return res.json();
}

async function updateStudent(id, data, token) {
    const res = await fetch(resolveApiUrl(`/student/update/${id}`), {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(data)
    });
    return res.json();
}

async function uploadResume(id, file, token) {
    let formData = new FormData();
    formData.append("file", file);

    const res = await fetch(resolveApiUrl(`/student/uploadResume/${id}`), {
        method: "POST",
        headers: { "Authorization": "Bearer " + token },
        body: formData
    });
    return res.text();
}

async function uploadPhoto(id, file, token) {
    let formData = new FormData();
    formData.append("file", file);

    const res = await fetch(resolveApiUrl(`/student/uploadPhoto/${id}`), {
        method: "POST",
        headers: { "Authorization": "Bearer " + token },
        body: formData
    });
    return res.text();
}

async function apiFetch(url, options = {}) {
    const token = localStorage.getItem('token');
    const headers = options.headers || {};
    if (token) headers['Authorization'] = `Bearer ${token}`;

    if (options.body && !(options.body instanceof FormData) && !headers['Content-Type']) {
        headers['Content-Type'] = 'application/json';
    }

    const fullUrl = resolveApiUrl(url);
    const res = await fetch(fullUrl, { ...options, headers });
    let text = await res.text();
    let data;
    try { data = text ? JSON.parse(text) : null; } catch (e) { data = text; }
    return { ok: res.ok, status: res.status, data, rawText: text };
}

async function getCompanies() {
    const r = await apiFetch('/api/company/all');
    if (r.ok) return Array.isArray(r.data) ? r.data : [];
    console.error('Failed to fetch companies', r);
    return [];
}

async function getStudent(studentId) {
    if (!studentId) return null;
    const r = await apiFetch(`/student/dashboard/${studentId}`);
    if (r.ok) return r.data;
    console.error('Failed to fetch student', r);
    return null;
}

async function applyToCompany(studentId, companyId) {
    const payload = { studentId, companyId };
    const r = await apiFetch('/student/apply', { method: 'POST', body: JSON.stringify(payload) });
    return r;
}
