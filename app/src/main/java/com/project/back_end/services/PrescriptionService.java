package com.project.back_end.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.model.Prescription;
import com.project.back_end.repo.PrescriptionRepository;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public ResponseEntity<?> savePrescription(Prescription prescription) {
        try {
            List<Prescription> existingPrescriptions =
                    prescriptionRepository.findByAppointmentId(
                            prescription.getAppointmentId());

            if (!existingPrescriptions.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Prescription already exists.");

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            prescriptionRepository.save(prescription);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Prescription saved successfully.");

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (Exception e) {
            e.printStackTrace();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Error saving prescription.");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    public ResponseEntity<?> getPrescription(Long appointmentId) {
        try {
            List<Prescription> prescriptions =
                    prescriptionRepository.findByAppointmentId(appointmentId);

            Map<String, Object> response = new HashMap<>();
            response.put("prescriptions", prescriptions);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        } catch (Exception e) {
            e.printStackTrace();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Error fetching prescription.");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}