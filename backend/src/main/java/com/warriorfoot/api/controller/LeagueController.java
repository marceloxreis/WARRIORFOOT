package com.warriorfoot.api.controller;

import com.warriorfoot.api.model.dto.LeagueDTO;
import com.warriorfoot.api.service.LeagueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/leagues")
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueDTO> getLeagueDashboard(@PathVariable UUID leagueId) {
        LeagueDTO leagueDTO = leagueService.getLeagueDashboard(leagueId);
        return ResponseEntity.ok(leagueDTO);
    }
}
