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
    public ResponseEntity<List<UserLeagueDTO>> getUserLeagues(@RequestHeader("Authorization") String authHeader) {
        String sessionToken = authHeader.replace("Bearer ", "");
        UUID userId = sessionService.getUserIdFromSession(sessionToken);
        List<UserLeagueDTO> dtos = leagueService.getUserLeaguesWithCreatorFlag(userId);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createNewLeague(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody Map<String, String> request) {
        String sessionToken = authHeader.replace("Bearer ", "");
        UUID userId = sessionService.getUserIdFromSession(sessionToken);
        String leagueName = request.getOrDefault("name", "My League");
        UUID newLeagueId = leagueService.createNewLeagueForUser(userId, leagueName);

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

    @DeleteMapping("/{leagueId}")
    public ResponseEntity<Void> deleteLeague(
        @PathVariable UUID leagueId,
        @RequestHeader("Authorization") String authHeader) {
        try {
            String sessionToken = authHeader.replace("Bearer ", "");
            UUID userId = sessionService.getUserIdFromSession(sessionToken);
            leagueService.deleteLeague(userId, leagueId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @PostMapping("/{leagueId}/leave")
    public ResponseEntity<Void> leaveLeague(
        @PathVariable UUID leagueId,
        @RequestHeader("Authorization") String authHeader) {
        try {
            String sessionToken = authHeader.replace("Bearer ", "");
            UUID userId = sessionService.getUserIdFromSession(sessionToken);
            leagueService.leaveLeague(userId, leagueId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }
}
