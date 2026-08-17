package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.model.Appointment;
import com.project.back_end.model.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (doctor == null) {
            return List.of();
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                        doctorId, start, end);

        List<LocalTime> bookedTimes = appointments.stream()
                .map(Appointment::getAppointmentTime)
                .map(LocalDateTime::toLocalTime)
                .toList();

        return doctor.getAvailableAppointmentTimes().stream()
                .filter(time -> {
                    LocalTime localTime = LocalTime.parse(time);
                    return !bookedTimes.contains(localTime);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1;
            }

            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Doctor doctor) {
        try {
            if (doctor.getId() == null ||
                    !doctorRepository.existsById(doctor.getId())) {
                return -1;
            }

            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public int deleteDoctor(Long doctorId) {
        try {
            if (!doctorRepository.existsById(doctorId)) {
                return -1;
            }

            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);

            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public String validateDoctor(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email);

        if (doctor == null) {
            return "Invalid email or password.";
        }

        if (!doctor.getPassword().equals(password)) {
            return "Invalid email or password.";
        }

        return tokenService.generateToken(doctor.getId(), "doctor");
    }

    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameLike(name);
    }

    @Transactional
    public List<Doctor> filterDoctorsByNameSpecilityandTime(
            String name,
            String specialty,
            String time) {

        List<Doctor> doctors =
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name, specialty);

        return filterDoctorByTime(doctors, time);
    }

    @Transactional
    public List<Doctor> filterDoctorByTime(
            List<Doctor> doctors,
            String time) {

        if (time == null || time.isBlank()) {
            return doctors;
        }

        String normalizedTime = time.toUpperCase();

        return doctors.stream()
                .filter(doctor -> doctor.getAvailableAppointmentTimes()
                        .stream()
                        .anyMatch(appointmentTime ->
                                isTimePeriod(appointmentTime, normalizedTime)))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Doctor> filterDoctorByNameAndTime(
            String name,
            String time) {

        List<Doctor> doctors = doctorRepository.findByNameLike(name);

        return filterDoctorByTime(doctors, time);
    }

    @Transactional
    public List<Doctor> filterDoctorByNameAndSpecility(
            String name,
            String specialty) {

        return doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name, specialty);
    }

    @Transactional
    public List<Doctor> filterDoctorByTimeAndSpecility(
            String time,
            String specialty) {

        List<Doctor> doctors =
                doctorRepository.findBySpecialtyIgnoreCase(specialty);

        return filterDoctorByTime(doctors, time);
    }

    @Transactional
    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    @Transactional
    public List<Doctor> filterDoctorsByTime(String time) {
        List<Doctor> doctors = doctorRepository.findAll();

        return filterDoctorByTime(doctors, time);
    }

    private boolean isTimePeriod(String appointmentTime, String period) {
        LocalTime time = LocalTime.parse(appointmentTime);

        if ("AM".equals(period)) {
            return time.isBefore(LocalTime.NOON);
        }

        if ("PM".equals(period)) {
            return !time.isBefore(LocalTime.NOON);
        }

        return false;
    }
}