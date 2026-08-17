import { openBookingOverlay } from "./loggedPatient.js";
import { deleteDoctor } from "./doctorServices.js";
import { fetchPatientDetails } from "./patientServices.js";

function createDoctorCard(doctor) {
    const card = document.createElement("div");
    card.className = "doctor-card";

    const role = localStorage.getItem("userRole");

    const doctorInfo = document.createElement("div");
    doctorInfo.className = "doctor-info";

    const doctorName = document.createElement("h3");
    doctorName.textContent = doctor.name;

    const specialization = document.createElement("p");
    specialization.textContent = doctor.specialization;

    const email = document.createElement("p");
    email.textContent = doctor.email;

    const appointmentTimes = document.createElement("ul");

    doctor.availableAppointmentTimes.forEach(time => {
        const appointmentTime = document.createElement("li");
        appointmentTime.textContent = time;
        appointmentTimes.appendChild(appointmentTime);
    });

    doctorInfo.appendChild(doctorName);
    doctorInfo.appendChild(specialization);
    doctorInfo.appendChild(email);
    doctorInfo.appendChild(appointmentTimes);

    const actions = document.createElement("div");
    actions.className = "card-actions";

    if (role === "admin") {
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.className = "delete-btn";

        deleteButton.addEventListener("click", async () => {
            const token = localStorage.getItem("token");

            const result = await deleteDoctor(doctor.id, token);

            alert(result.message);

            if (result.success) {
                card.remove();
            }
        });

        actions.appendChild(deleteButton);
    } else if (role === "patient") {
        const bookButton = document.createElement("button");
        bookButton.textContent = "Book Now";
        bookButton.className = "book-btn";

        bookButton.addEventListener("click", () => {
            alert("Please log in before booking an appointment.");
        });

        actions.appendChild(bookButton);
    } else if (role === "loggedPatient") {
        const bookButton = document.createElement("button");
        bookButton.textContent = "Book Now";
        bookButton.className = "book-btn";

        bookButton.addEventListener("click", async () => {
            const token = localStorage.getItem("token");

            if (!token) {
                window.location.href = "/";
                return;
            }

            const patient = await fetchPatientDetails(token);

            openBookingOverlay(doctor, patient);
        });

        actions.appendChild(bookButton);
    }

    card.appendChild(doctorInfo);
    card.appendChild(actions);

    return card;
}