package com.warriorfoot.api.model.dto;

import java.util.UUID;

public record InviteValidationResponse(
    boolean valid,
    String inviterName,
    String inviteeName,
    UUID leagueId,
    String errorMessage
) {}
