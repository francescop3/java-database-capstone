package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.model.Admin;
import com.project.back_end.model.Doctor;
import com.project.back_end.model.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(
            TokenService tokenService,
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            DoctorService doctorService,
            PatientService patientService) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<?> validateToken(String token, String role) {
        try {
            if (tokenService.validateToken(token, role)) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token.");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token.");
        }
    }

    public ResponseEntity<?> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);

            if (admin == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password.");
            }

            if (!admin.getPassword().equals(password)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password.");
            }

            String token = tokenService.generateToken(
                    admin.getUsername(),
                    "admin");

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error.");
        }
    }

    public List<Doctor> filterDoctor(
            String name,
            String specialty,
            String time) {

        boolean hasName = name != null && !name.isBlank();
        boolean hasSpecialty = specialty != null && !specialty.isBlank();
        boolean hasTime = time != null && !time.isBlank();

        if (!hasName && !hasSpecialty && !hasTime) {
            return doctorService.getDoctors();
        }

        if (hasName && hasSpecialty && hasTime) {
            return doctorService.filterDoctorsByNameSpecilityandTime(
                    name, specialty, time);
        }

        if (hasName && hasSpecialty) {
            return doctorService.filterDoctorByNameAndSpecility(
                    name, specialty);
        }

        if (hasName && hasTime) {
            return doctorService.filterDoctorByNameAndTime(
                    name, time);
        }

        if (hasSpecialty && hasTime) {
            return doctorService.filterDoctorByTimeAndSpecility(
                    time, specialty);
        }

        if (hasName) {
            return doctorService.findDoctorByName(name);
        }

        if (hasSpecialty) {
            return doctorService.filterDoctorBySpecility(specialty);
        }

        return doctorService.filterDoctorsByTime(time);
    }

    public int validateAppointment(
            Long doctorId,
            LocalDateTime appointmentTime) {

        try {
            Doctor doctor = doctorRepository
                    .findById(doctorId)
                    .orElse(null);

            if (doctor == null) {
                return -1;
            }

            LocalDate date = appointmentTime.toLocalDate();

            List<String> availableTimes =
                    doctorService.getDoctorAvailability(
                            doctorId, date);

            String requestedTime =
                    appointmentTime.toLocalTime().toString();

            for (String availableTime : availableTimes) {
                if (availableTime.equals(requestedTime)) {
                    return 1;
                }
            }

            return 0;

        } catch (Exception e) {
            return 0;
        }
    }

    public boolean validatePatient(String email, String phone) {
        try {
            return patientRepository.findByEmailOrPhone(email, phone) == null;
        } catch (Exception e) {
            return false;
        }
    }

    public ResponseEntity<?> validatePatientLogin(
            String email,
            String password) {

        try {
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password.");
            }

            if (!patient.getPassword().equals(password)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password.");
            }

            String token = tokenService.generateToken(
                    patient.getEmail(),
                    "loggedPatient");

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error.");
        }
    }

    public ResponseEntity<?> filterPatient(
            String token,
            String condition,
            String doctorName) {

        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Patient not found.");
            }

            Long patientId = patient.getId();

            boolean hasCondition =
                    condition != null && !condition.isBlank();

            boolean hasDoctor =
                    doctorName != null && !doctorName.isBlank();

            if (hasCondition && hasDoctor) {
                return ResponseEntity.ok(
                        patientService.filterByDoctorAndCondition(
                                patientId, doctorName, condition));
            }

            if (hasCondition) {
                return ResponseEntity.ok(
                        patientService.filterByCondition(
                                patientId, condition));
            }

            if (hasDoctor) {
                return ResponseEntity.ok(
                        patientService.filterByDoctor(
                                patientId, doctorName));
            }

            return ResponseEntity.ok(
                    patientService.getPatientAppointment(patientId));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error.");
        }
    }
}