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
    private final com.warriorfoot.api.repository.UserLeagueRepository userLeagueRepository;

    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository,
                       com.warriorfoot.api.repository.UserLeagueRepository userLeagueRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.userLeagueRepository = userLeagueRepository;
    }

    @Transactional(readOnly = true)
    public TeamDTO getTeamById(UUID teamId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        // Find the manager (user) who owns this team
        String managerName = userLeagueRepository.findByTeamId(teamId)
            .map(userLeague -> userLeague.getUser().getFullName())
            .orElse("No Manager");

        return new TeamDTO(
            team.getId(),
            team.getName(),
            team.getColorPrimary(),
            team.getColorSecondary(),
            team.getDivisionLevel(),
            managerName
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
