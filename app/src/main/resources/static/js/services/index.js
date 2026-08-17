import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";

const ADMIN_API = API_BASE_URL + '/admin/login';
const DOCTOR_API = API_BASE_URL + '/doctor/login';

window.onload = function () {
    const adminLogin = document.getElementById("adminLogin");
    const doctorLogin = document.getElementById("doctorLogin");

    if (adminLogin) {
        adminLogin.addEventListener("click", () => openModal("adminLogin"));
    }

    if (doctorLogin) {
        doctorLogin.addEventListener("click", () => openModal("doctorLogin"));
    }
};

window.adminLoginHandler = async function () {
    const username = document.getElementById("adminUsername").value;
    const password = document.getElementById("adminPassword").value;

    const admin = {
        username,
        password
    };

    try {
        const response = await fetch(ADMIN_API, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(admin)
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem("token", data.token);
            selectRole("admin");
        } else {
            alert("Invalid admin credentials.");
        }
    } catch (error) {
        alert("Something went wrong. Please try again.");
    }
};

window.doctorLoginHandler = async function () {
    const email = document.getElementById("doctorEmail").value;
    const password = document.getElementById("doctorPassword").value;

    const doctor = {
        email,
        password
    };

    try {
        const response = await fetch(DOCTOR_API, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(doctor)
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem("token", data.token);
            selectRole("doctor");
        } else {
            alert("Invalid doctor credentials.");
        }
    } catch (error) {
        console.error(error);
        alert("Something went wrong. Please try again.");
    }
};