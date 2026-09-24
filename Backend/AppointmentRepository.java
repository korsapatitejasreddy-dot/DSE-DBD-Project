package com.mediconnect.repository;
import com.mediconnect.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment,String> {
    List<Appointment> findByEmailOrderByCreatedAtDesc(String email);
}
