package com.warriorfoot.api.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserLeagueDTO(
    UUID leagueId,
    String leagueName,
    UUID teamId,
    String teamName,
    Integer divisionLevel,
    LocalDateTime createdAt,
    boolean isCreator
) {}
