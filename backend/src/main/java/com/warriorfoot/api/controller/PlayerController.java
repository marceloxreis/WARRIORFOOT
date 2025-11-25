package com.warriorfoot.api.controller;

import com.warriorfoot.api.model.dto.PlayerDetailsDTO;
import com.warriorfoot.api.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerDetailsDTO> getPlayerDetails(@PathVariable UUID playerId) {
        PlayerDetailsDTO player = playerService.getPlayerDetails(playerId);
        return ResponseEntity.ok(player);
    }
}
