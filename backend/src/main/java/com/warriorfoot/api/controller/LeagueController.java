package com.warriorfoot.api.controller;

import com.warriorfoot.api.model.dto.LeagueDTO;
import com.warriorfoot.api.model.dto.UserLeagueDTO;
import com.warriorfoot.api.model.entity.UserLeague;
import com.warriorfoot.api.service.LeagueService;
import com.warriorfoot.api.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/leagues")
public class LeagueController {

    private final LeagueService leagueService;
    private final SessionService sessionService;

    public LeagueController(LeagueService leagueService, SessionService sessionService) {
        this.leagueService = leagueService;
        this.sessionService = sessionService;
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueDTO> getLeagueDashboard(@PathVariable UUID leagueId) {
        LeagueDTO leagueDTO = leagueService.getLeagueDashboard(leagueId);
        return ResponseEntity.ok(leagueDTO);
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<UserLeagueDTO>> getUserLeagues(@CookieValue("session_token") String sessionToken) {
        UUID userId = sessionService.getUserIdFromSession(sessionToken);
        List<UserLeague> userLeagues = leagueService.getUserLeagues(userId);
        
        List<UserLeagueDTO> dtos = userLeagues.stream()
            .map(ul -> new UserLeagueDTO(
                ul.getLeagueId(),
                ul.getTeam().getId(),
                ul.getTeam().getName(),
                ul.getTeam().getDivisionLevel(),
                ul.getCreatedAt()
            ))
            .toList();
        
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createNewLeague(@CookieValue("session_token") String sessionToken) {
        UUID userId = sessionService.getUserIdFromSession(sessionToken);
        UUID newLeagueId = leagueService.createNewLeagueForUser(userId);
        
        List<UserLeague> userLeagues = leagueService.getUserLeagues(userId);
        UserLeague newLeague = userLeagues.stream()
            .filter(ul -> ul.getLeagueId().equals(newLeagueId))
            .findFirst()
            .orElseThrow();
        
        return ResponseEntity.ok(Map.of(
            "leagueId", newLeagueId,
            "teamId", newLeague.getTeam().getId(),
            "teamName", newLeague.getTeam().getName(),
            "divisionLevel", newLeague.getTeam().getDivisionLevel()
        ));
    }
}
