package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.entities.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    public PatientController(
            PatientService patientService,
            Service service) {

        this.patientService = patientService;
        this.service = service;
    }

    // GET patient details
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatient(
            @PathVariable String token) {

        ResponseEntity<?> validation =
                service.validateToken(token, "patient");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        return patientService.getPatientDetails(token);
    }

    // POST create patient
    @PostMapping
    public ResponseEntity<?> createPatient(
            @Valid @RequestBody Patient patient) {

        boolean valid = service.validatePatient(patient);

        if (!valid) {
            return ResponseEntity.status(409).body(
                    java.util.Map.of(
                            "message",
                            "Patient with this email or phone already exists"
                    )
            );
        }

        int result = patientService.createPatient(patient);

        if (result == 1) {
            return ResponseEntity.status(201).body(
                    java.util.Map.of(
                            "message",
                            "Patient created successfully"
                    )
            );
        }

        return ResponseEntity.internalServerError().body(
                java.util.Map.of(
                        "message",
                        "Error creating patient"
                )
        );
    }

    // POST patient login
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody Login login) {

        return service.validatePatientLogin(login);
    }

    // GET patient appointments
    @GetMapping("/{patientId}/{token}")
    public ResponseEntity<?> getPatientAppointment(
            @PathVariable Long patientId,
            @PathVariable String token) {

        ResponseEntity<?> validation =
                service.validateToken(token, "patient");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        return patientService.getPatientAppointment(patientId);
    }

    // GET filtered patient appointments
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<?> validation =
                service.validateToken(token, "patient");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        return service.filterPatient(
                condition,
                name,
                token
        );
    }
}