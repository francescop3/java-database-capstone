import { openModal } from "../components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

document.getElementById('addDocBtn').addEventListener('click', () => {
    openModal('addDoctor');
});


document.addEventListener("DOMContentLoaded", () => {
    loadDoctorCards();

    document.getElementById("searchBar")?.addEventListener("input", filterDoctorsOnChange);
    document.getElementById("timeFilter")?.addEventListener("change", filterDoctorsOnChange);
    document.getElementById("specialtyFilter")?.addEventListener("change", filterDoctorsOnChange);
});

async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (error) {
        console.error(error);
    }
}

async function filterDoctorsOnChange() {
    const name = document.getElementById("searchBar")?.value || null;
    const time = document.getElementById("timeFilter")?.value || null;
    const specialty = document.getElementById("specialtyFilter")?.value || null;

    try {
        const data = await filterDoctors(name, time, specialty);

        if (data.doctors && data.doctors.length > 0) {
            renderDoctorCards(data.doctors);
        } else {
            document.getElementById("content").innerHTML =
                "No doctors found with the given filters.";
        }
    } catch (error) {
        alert("An error occurred while filtering doctors.");
    }
}

function renderDoctorCards(doctors) {
    const content = document.getElementById("content");
    content.innerHTML = "";

    doctors.forEach(doctor => {
        content.appendChild(createDoctorCard(doctor));
    });
}

window.adminAddDoctor = async function () {
    const name = document.getElementById("doctorName").value;
    const email = document.getElementById("doctorEmail").value;
    const phone = document.getElementById("doctorPhone").value;
    const password = document.getElementById("doctorPassword").value;
    const specialty = document.getElementById("doctorSpecialty").value;
    const availableTimes = document.getElementById("doctorAvailableTimes").value;

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Authentication token not found.");
        return;
    }

    const doctor = {
        name,
        email,
        phone,
        password,
        specialty,
        availableTimes
    };

    const result = await saveDoctor(doctor, token);

    if (result.success) {
        alert(result.message);
        document.getElementById("modal").style.display = "none";
        window.location.reload();
    } else {
        alert(result.message);
    }
};