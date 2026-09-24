package com.mediconnect.dto;
import jakarta.validation.constraints.*;
public record RegisterRequest(
    @NotBlank String fullName,
    @NotBlank @Email String email,
    @NotBlank String phone,
    @NotBlank @Size(min=6) String password
) {}
