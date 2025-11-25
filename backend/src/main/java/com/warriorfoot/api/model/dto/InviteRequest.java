package com.warriorfoot.api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InviteRequest(
    @NotBlank(message = "Invitee name is required")
    String inviteeName,

    @NotBlank(message = "Invitee email is required")
    @Email(message = "Invalid email format")
    String inviteeEmail
) {}
