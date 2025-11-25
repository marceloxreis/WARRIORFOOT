package com.warriorfoot.api.model.dto;

import java.util.UUID;

public record AvailableTeamDTO(
    UUID id,
    String name,
    String colorPrimary,
    String colorSecondary
) {}
