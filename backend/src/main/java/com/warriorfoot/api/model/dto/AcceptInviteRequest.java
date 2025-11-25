package com.warriorfoot.api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AcceptInviteRequest(
    @NotBlank(message = "Token is required")
    String token,

    @NotBlank(message = "Full name is required")
    String fullName,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password,

    @NotBlank(message = "Password confirmation is required")
    String passwordConfirmation,

    @NotNull(message = "Selected team is required")
    UUID selectedTeamId
) {}
