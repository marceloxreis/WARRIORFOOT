package com.warriorfoot.api.model.dto;

import java.util.UUID;

public record AuthResponse(
    String sessionToken,
    UUID userId,
    String fullName,
    String email,
    UUID activeLeagueId,
    UUID activeTeamId
) {}
