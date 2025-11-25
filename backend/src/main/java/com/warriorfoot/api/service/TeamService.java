package com.warriorfoot.api.service;

import com.warriorfoot.api.model.dto.PlayerDTO;
import com.warriorfoot.api.model.dto.TeamDTO;
import com.warriorfoot.api.model.entity.Player;
import com.warriorfoot.api.model.entity.Team;
import com.warriorfoot.api.repository.PlayerRepository;
import com.warriorfoot.api.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public TeamDTO getTeamById(UUID teamId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        
        return new TeamDTO(
            team.getId(),
            team.getName(),
            team.getColorPrimary(),
            team.getColorSecondary(),
            team.getDivisionLevel()
        );
    }

    @Transactional(readOnly = true)
    public List<PlayerDTO> getTeamPlayers(UUID teamId) {
        List<Player> players = playerRepository.findByTeamId(teamId);
        
        return players.stream()
            .map(p -> new PlayerDTO(
                p.getId(),
                p.getName(),
                p.getAge(),
                p.getPosition().name(),
                p.getOverall(),
                p.getMarketValue()
            ))
            .toList();
    }
}
