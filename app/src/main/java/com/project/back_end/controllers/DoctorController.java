package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    // GET doctor availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String token) {

        ResponseEntity<?> validation = service.validateToken(token, user);

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        return ResponseEntity.ok(
            doctorService.getDoctorAvailability(doctorId, date)
        );
    }

    // GET all doctors
    @GetMapping
    public ResponseEntity<?> getDoctor() {

        List<Doctor> doctors = doctorService.getDoctors();

        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);

        return ResponseEntity.ok(response);
    }

    // POST save doctor
    @PostMapping("/{token}")
    public ResponseEntity<?> saveDoctor(
            @Valid @RequestBody Doctor doctor,
            @PathVariable String token) {

        ResponseEntity<?> validation = service.validateToken(token, "admin");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        int result = doctorService.saveDoctor(doctor);

        if (result == 1) {
            return ResponseEntity.ok(
                    Map.of("message", "Doctor added successfully")
            );
        }

        if (result == -1) {
            return ResponseEntity.status(409).body(
                    Map.of("message", "Doctor already exists")
            );
        }

        return ResponseEntity.internalServerError().body(
                Map.of("message", "Error adding doctor")
        );
    }

    // POST doctor login
    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(
            @Valid @RequestBody Login login) {

        return ResponseEntity.ok(
            doctorService.validateDoctor(
                login.getEmail(), 
                login.getPassword()
            )
        );
    }

    // PUT update doctor
    @PutMapping("/{token}")
    public ResponseEntity<?> updateDoctor(
            @Valid @RequestBody Doctor doctor,
            @PathVariable String token) {

        ResponseEntity<?> validation = service.validateToken(token, "admin");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        int result = doctorService.updateDoctor(doctor);

        if (result == 1) {
            return ResponseEntity.ok(
                    Map.of("message", "Doctor updated successfully")
            );
        }

        if (result == -1) {
            return ResponseEntity.status(404).body(
                    Map.of("message", "Doctor not found")
            );
        }

        return ResponseEntity.internalServerError().body(
                Map.of("message", "Error updating doctor")
        );
    }

    // DELETE doctor
    @DeleteMapping("/{doctorId}/{token}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable Long doctorId,
            @PathVariable String token) {

        ResponseEntity<?> validation = service.validateToken(token, "admin");

        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }

        int result = doctorService.deleteDoctor(doctorId);

        if (result == 1) {
            return ResponseEntity.ok(
                    Map.of("message", "Doctor deleted successfully")
            );
        }

        if (result == -1) {
            return ResponseEntity.status(404).body(
                    Map.of("message", "Doctor not found")
            );
        }

        return ResponseEntity.internalServerError().body(
                Map.of("message", "Error deleting doctor")
        );
    }

    // GET filtered doctors
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<?> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        return ResponseEntity.ok(
            service.filterDoctor(name, time, speciality)
        );
    }
}