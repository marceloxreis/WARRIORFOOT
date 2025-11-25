package com.warriorfoot.api.model.dto;

import java.util.List;
import java.util.Map;

public record LeagueDTO(
    Map<Integer, List<TeamDTO>> divisions
) {}
