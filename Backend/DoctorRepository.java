package com.mediconnect.repository;

import com.mediconnect.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DoctorRepository extends MongoRepository<Doctor, String> {

    List<Doctor> findByDepartmentIgnoreCase(String department);

    List<Doctor> findByAvailableTrue();
}