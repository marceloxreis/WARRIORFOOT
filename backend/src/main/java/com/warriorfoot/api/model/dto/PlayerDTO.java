package com.warriorfoot.api.model.dto;

import java.util.UUID;

public record PlayerDTO(
    UUID id,
    String name,
    Integer age,
    String position,
    Integer overall,
    Long marketValue
) {}
