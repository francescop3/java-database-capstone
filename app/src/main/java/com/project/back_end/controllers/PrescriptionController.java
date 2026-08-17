package com.project.back_end.controllers;

import com.project.back_end.entities.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}" + "prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Service service;
    private final AppointmentService appointmentService;

    public PrescriptionController(
            PrescriptionService prescriptionService,
            Service service,
            AppointmentService appointmentService) {

        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }

    // POST save prescription
    @PostMapping("/{token}")
    public ResponseEntity<?> savePrescription(
            @Valid @RequestBody Prescription prescription,
            @PathVariable String token) {

        // Validate doctor token
        ResponseEntity<?> validation =
                service.validateToken(token, "doctor");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        // Update appointment status
        ResponseEntity<?> statusResponse =
                appointmentService.changeStatus(
                        prescription.getAppointmentId(),
                        1
                );

        // If changing the appointment status failed,
        // don't save the prescription
        if (!statusResponse.getStatusCode().is2xxSuccessful()) {
            return statusResponse;
        }

        // Save prescription
        return prescriptionService.savePrescription(prescription);
    }

    // GET prescription
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        // Validate doctor token
        ResponseEntity<?> validation =
                service.validateToken(token, "doctor");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        // Get prescription
        return prescriptionService.getPrescription(appointmentId);
    }
}