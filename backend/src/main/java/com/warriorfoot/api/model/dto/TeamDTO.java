package com.warriorfoot.api.model.dto;

import java.util.UUID;

public record TeamDTO(
    UUID id,
    String name,
    String colorPrimary,
    String colorSecondary,
    Integer divisionLevel,
    String managerName
) {}
