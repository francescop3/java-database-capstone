import { getAllAppointments } from "./services/appointmentServices.js";
import { createPatientRow } from "./components/patientRow.js";

const patientTableBody = document.getElementById("patientTableBody");

let selectedDate = new Date().toISOString().split("T")[0];
const token = localStorage.getItem("token");
let patientName = null;

document.getElementById("searchBar")?.addEventListener("input", () => {
    const value = document.getElementById("searchBar").value.trim();
    patientName = value ? value : null;
    loadAppointments();
});

document.getElementById("todayButton")?.addEventListener("click", () => {
    selectedDate = new Date().toISOString().split("T")[0];
    document.getElementById("datePicker").value = selectedDate;
    loadAppointments();
});

document.getElementById("datePicker")?.addEventListener("change", () => {
    selectedDate = document.getElementById("datePicker").value;
    loadAppointments();
});

async function loadAppointments() {
    try {
        const appointments = await getAllAppointments(
            selectedDate,
            patientName,
            token
        );

        patientTableBody.innerHTML = "";

        if (!appointments || appointments.length === 0) {
            patientTableBody.innerHTML = `
                <tr>
                    <td colspan="5">No Appointments found for today.</td>
                </tr>
            `;
            return;
        }

        appointments.forEach(appointment => {
            const patient = {
                id: appointment.patientId,
                name: appointment.patientName,
                phone: appointment.patientPhone,
                email: appointment.patientEmail
            };

            const row = createPatientRow(patient, appointment);
            patientTableBody.appendChild(row);
        });
    } catch (error) {
        patientTableBody.innerHTML = `
            <tr>
                <td colspan="5">Error loading appointments. Try again later.</td>
            </tr>
        `;
    }
}

document.addEventListener("DOMContentLoaded", () => {
    renderContent();
    loadAppointments();
});