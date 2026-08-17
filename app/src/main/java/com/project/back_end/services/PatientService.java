package com.project.back_end.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public List<AppointmentDTO> getPatientAppointment(Long patientId) {
        try {
            return appointmentRepository.findByPatientId(patientId)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Transactional
    public List<AppointmentDTO> filterByCondition(
            Long patientId,
            String condition) {

        try {
            int status;

            if ("future".equalsIgnoreCase(condition)) {
                status = 0;
            } else if ("past".equalsIgnoreCase(condition)) {
                status = 1;
            } else {
                return List.of();
            }

            return appointmentRepository
                    .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
                            patientId, status)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Transactional
    public List<AppointmentDTO> filterByDoctor(
            Long patientId,
            String doctorName) {

        try {
            return appointmentRepository
                    .filterByDoctorNameAndPatientId(
                            doctorName, patientId)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Transactional
    public List<AppointmentDTO> filterByDoctorAndCondition(
            Long patientId,
            String doctorName,
            String condition) {

        try {
            int status;

            if ("future".equalsIgnoreCase(condition)) {
                status = 0;
            } else if ("past".equalsIgnoreCase(condition)) {
                status = 1;
            } else {
                return List.of();
            }

            return appointmentRepository
                    .filterByDoctorNameAndPatientIdAndStatus(
                            doctorName, patientId, status)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Patient getPatientDetails(String token) {
        try {
            String email = tokenService.extractEmail(token);

            return patientRepository.findByEmail(email);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getName(),
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                appointment.getPatient().getEmail(),
                appointment.getPatient().getPhone(),
                appointment.getPatient().getAddress(),
                appointment.getAppointmentTime(),
                appointment.getStatus()
        );
    }
}