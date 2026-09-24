package com.mediconnect.controller;
import com.mediconnect.dto.*;
import com.mediconnect.model.User;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users=users; this.encoder=encoder; this.jwt=jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest r) {
        String email=r.email().trim().toLowerCase();
        if(users.existsByEmail(email))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","Email is already registered"));

        User u=new User();
        u.setFullName(r.fullName().trim());
        u.setEmail(email);
        u.setPhone(r.phone().trim());
        u.setPassword(encoder.encode(r.password()));
        users.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Account created successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest r) {
        String email=r.email().trim().toLowerCase();
        User u=users.findByEmail(email).orElse(null);
        if(u==null || !encoder.matches(r.password(),u.getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Invalid email or password"));

        return ResponseEntity.ok(Map.of(
            "message","Login successful",
            "token",jwt.generateToken(u.getEmail()),
            "user",Map.of("id",u.getId(),"fullName",u.getFullName(),"email",u.getEmail(),"phone",u.getPhone(),"role",u.getRole())
        ));
    }
}
