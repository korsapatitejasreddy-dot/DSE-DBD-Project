const API_BASE = "http://localhost:8080/api";

document.addEventListener("DOMContentLoaded", () => {
    const registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const password = document.getElementById("registerPassword").value;
            const confirm = document.getElementById("confirmPassword").value;

            if (password !== confirm) {
                alert("Passwords do not match.");
                return;
            }

            try {
                const response = await fetch(`${API_BASE}/auth/register`, {
                    method: "POST",
                    headers: {"Content-Type":"application/json"},
                    body: JSON.stringify({
                        fullName: document.getElementById("fullName").value.trim(),
                        email: document.getElementById("registerEmail").value.trim(),
                        phone: document.getElementById("phone").value.trim(),
                        password
                    })
                });
                const data = await response.json();
                if (!response.ok) throw new Error(data.message || "Registration failed");
                alert("Account created successfully!");
                location.href = "login.html";
            } catch (err) {
                alert(err.message);
            }
        });
    }

    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            try {
                const response = await fetch(`${API_BASE}/auth/login`, {
                    method: "POST",
                    headers: {"Content-Type":"application/json"},
                    body: JSON.stringify({
                        email: document.getElementById("loginEmail").value.trim(),
                        password: document.getElementById("loginPassword").value
                    })
                });
                const data = await response.json();
                if (!response.ok) throw new Error(data.message || "Login failed");
                localStorage.setItem("jwtToken", data.token);
                localStorage.setItem("currentUser", JSON.stringify(data.user));
                alert("Login successful!");
                location.href = "dashboard.html";
            } catch (err) {
                alert(err.message);
            }
        });
    }

    const appointmentForm = document.getElementById("appointmentForm");
    if (appointmentForm) {
        const user = JSON.parse(localStorage.getItem("currentUser") || "null");
        const email = document.getElementById("appointmentEmail");
        if (user && email) email.value = user.email;

        appointmentForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const payload = {
                patientName: document.getElementById("patientName").value.trim(),
                email: document.getElementById("appointmentEmail").value.trim(),
                department: document.getElementById("department").value,
                doctor: document.getElementById("doctor").value,
                date: document.getElementById("appointmentDate").value,
                time: document.getElementById("appointmentTime").value,
                reason: document.getElementById("reason").value.trim()
            };

            try {
                const response = await fetch(`${API_BASE}/appointments`, {
                    method: "POST",
                    headers: {"Content-Type":"application/json"},
                    body: JSON.stringify(payload)
                });
                const saved = await response.json();
                if (!response.ok) throw new Error(saved.message || "Booking failed");
                alert(`Appointment booked successfully!\n\nAppointment ID: ${saved.id}`);
                appointmentForm.reset();
                if (user && email) email.value = user.email;
            } catch (err) {
                alert(err.message);
            }
        });
    }

    const search = document.getElementById("doctorSearch");
    const filter = document.getElementById("departmentFilter");
    const doctors = document.querySelectorAll(".doctor-card");
    function filterDoctors() {
        if (!search || !filter) return;
        const text = search.value.toLowerCase();
        const dept = filter.value;
        doctors.forEach(d => {
            const okName = d.dataset.name.includes(text);
            const okDept = dept === "all" || d.dataset.department === dept;
            d.style.display = okName && okDept ? "block" : "none";
        });
    }
    if (search && filter) {
        search.addEventListener("input", filterDoctors);
        filter.addEventListener("change", filterDoctors);
    }

    const dateInput = document.getElementById("appointmentDate");
    if (dateInput) dateInput.min = new Date().toISOString().split("T")[0];

    if (location.pathname.endsWith("dashboard.html")) loadDashboard();
});

async function loadDashboard() {
    const user = JSON.parse(localStorage.getItem("currentUser") || "null");
    if (!user) {
        location.href = "login.html";
        return;
    }

    const h1 = document.querySelector(".dashboard-top h1");
    const profile = document.querySelector(".profile-mini strong");
    if (h1) h1.textContent = `Welcome back, ${user.fullName}`;
    if (profile) profile.textContent = user.fullName;

    try {
        const response = await fetch(
            `${API_BASE}/appointments/patient/${encodeURIComponent(user.email)}`
        );
        if (!response.ok) throw new Error("Could not load appointments");
        const appointments = await response.json();
        renderAppointments(appointments);
    } catch (err) {
        console.error(err);
    }

    const logout = document.getElementById("logoutBtn");
    if (logout) {
        logout.addEventListener("click", () => {
            localStorage.removeItem("jwtToken");
            localStorage.removeItem("currentUser");
        });
    }
}

function renderAppointments(list) {
    const panel = document.querySelector(".appointment-panel");
    if (!panel) return;
    panel.querySelectorAll(".appointment-item").forEach(x => x.remove());

    if (!list.length) {
        const p = document.createElement("p");
        p.textContent = "No appointments booked yet.";
        panel.appendChild(p);
        return;
    }

    list.forEach(a => {
        const item = document.createElement("div");
        item.className = "appointment-item";
        item.innerHTML = `
            <div class="doctor-avatar">👨‍⚕️</div>
            <div class="appointment-doctor">
                <h3>${escapeHtml(a.doctor)}</h3>
                <p>${escapeHtml(a.department)}</p>
            </div>
            <div class="appointment-time">
                <strong>${escapeHtml(a.date)}</strong>
                <span>${escapeHtml(a.time)}</span>
            </div>
            <button type="button">Cancel</button>
        `;
        item.querySelector("button").onclick = () => cancelAppointment(a.id);
        panel.appendChild(item);
    });
}

async function cancelAppointment(id) {
    if (!confirm("Cancel this appointment?")) return;
    const response = await fetch(`${API_BASE}/appointments/${id}`, {method:"DELETE"});
    if (!response.ok) {
        alert("Could not cancel appointment.");
        return;
    }
    alert("Appointment cancelled.");
    loadDashboard();
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&","&amp;").replaceAll("<","&lt;")
        .replaceAll(">","&gt;").replaceAll('"',"&quot;")
        .replaceAll("'","&#039;");
}
