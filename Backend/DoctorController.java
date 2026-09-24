package com.mediconnect.controller;

import com.mediconnect.model.Doctor;
import com.mediconnect.repository.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @GetMapping("/available")
    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.findByAvailableTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable String id) {

        return doctorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable String id,
            @RequestBody Doctor updatedDoctor) {

        return doctorRepository.findById(id)
                .map(existing -> {

                    existing.setName(updatedDoctor.getName());
                    existing.setDepartment(updatedDoctor.getDepartment());
                    existing.setSpecialization(updatedDoctor.getSpecialization());
                    existing.setRating(updatedDoctor.getRating());
                    existing.setExperience(updatedDoctor.getExperience());
                    existing.setAvailable(updatedDoctor.isAvailable());

                    return ResponseEntity.ok(
                            doctorRepository.save(existing)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable String id) {

        if (!doctorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        doctorRepository.deleteById(id);

        return ResponseEntity.ok(
                java.util.Map.of("message", "Doctor deleted")
        );
    }
}