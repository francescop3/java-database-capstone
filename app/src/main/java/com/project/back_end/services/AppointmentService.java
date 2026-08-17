package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final Service service;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            Service service,
            TokenService tokenService,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.service = service;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public String updateAppointment(Long appointmentId, Long patientId, LocalDateTime appointmentTime) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

            if (appointment == null) {
                return "Appointment not found.";
            }

            if (!appointment.getPatient().getId().equals(patientId)) {
                return "Patient ID does not match.";
            }

            if (appointment.getStatus() != 0) {
                return "Appointment cannot be updated.";
            }

            Doctor doctor = appointment.getDoctor();

            if (doctor == null) {
                return "Doctor not found.";
            }

            appointment.setAppointmentTime(appointmentTime);
            appointmentRepository.save(appointment);

            return "Appointment updated successfully.";
        } catch (Exception e) {
            return "Error updating appointment.";
        }
    }

    @Transactional
    public String cancelAppointment(Long appointmentId) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

            if (appointment == null) {
                return "Appointment not found.";
            }

            appointmentRepository.delete(appointment);

            return "Appointment cancelled successfully.";
        } catch (Exception e) {
            return "Error cancelling appointment.";
        }
    }

    @Transactional
    public List<Appointment> getAppointments(
            Long doctorId,
            LocalDate date,
            String patientName) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

        if (patientName == null || patientName.isBlank()) {
            return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                    doctorId,
                    start,
                    end);
        }

        return appointmentRepository
                .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                        doctorId,
                        patientName,
                        start,
                        end);
    }

    @Transactional
    public void changeStatus(Long appointmentId, int status) {
        appointmentRepository.updateStatus(status, appointmentId);
    }
}