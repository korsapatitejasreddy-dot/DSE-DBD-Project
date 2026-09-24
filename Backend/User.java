package com.mediconnect.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection="users")
public class User {
    @Id private String id;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String role = "PATIENT";
    private Instant createdAt = Instant.now();

    public User() {}
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getRole(){return role;} public void setRole(String v){role=v;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
}
