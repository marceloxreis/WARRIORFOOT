package com.warriorfoot.api.controller;

import com.warriorfoot.api.model.dto.PlayerDTO;
import com.warriorfoot.api.model.dto.TeamDTO;
import com.warriorfoot.api.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> getTeam(@PathVariable UUID teamId) {
        TeamDTO team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team);
    }

    @GetMapping("/{teamId}/players")
    public ResponseEntity<List<PlayerDTO>> getTeamPlayers(@PathVariable UUID teamId) {
        List<PlayerDTO> players = teamService.getTeamPlayers(teamId);
        return ResponseEntity.ok(players);
    }
}
