package com.mediconnect.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection="appointments")
public class Appointment {
    @Id private String id;
    private String patientName;
    private String email;
    private String department;
    private String doctor;
    private String date;
    private String time;
    private String reason;
    private String status = "BOOKED";
    private Instant createdAt = Instant.now();

    public Appointment() {}
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getPatientName(){return patientName;} public void setPatientName(String v){patientName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getDepartment(){return department;} public void setDepartment(String v){department=v;}
    public String getDoctor(){return doctor;} public void setDoctor(String v){doctor=v;}
    public String getDate(){return date;} public void setDate(String v){date=v;}
    public String getTime(){return time;} public void setTime(String v){time=v;}
    public String getReason(){return reason;} public void setReason(String v){reason=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
}
