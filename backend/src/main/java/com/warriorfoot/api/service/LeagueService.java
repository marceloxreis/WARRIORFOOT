package com.warriorfoot.api.service;

import com.warriorfoot.api.model.dto.LeagueDTO;
import com.warriorfoot.api.model.dto.TeamDTO;
import com.warriorfoot.api.model.entity.League;
import com.warriorfoot.api.model.entity.Player;
import com.warriorfoot.api.model.entity.Team;
import com.warriorfoot.api.model.entity.UserLeague;
import com.warriorfoot.api.repository.LeagueRepository;
import com.warriorfoot.api.repository.PlayerRepository;
import com.warriorfoot.api.repository.TeamRepository;
import com.warriorfoot.api.repository.UserLeagueRepository;
import com.warriorfoot.api.util.PlayerFactory;
import com.warriorfoot.api.util.TeamFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final UserLeagueRepository userLeagueRepository;

    public LeagueService(LeagueRepository leagueRepository,
                         TeamRepository teamRepository,
                         PlayerRepository playerRepository,
                         UserLeagueRepository userLeagueRepository) {
        this.leagueRepository = leagueRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.userLeagueRepository = userLeagueRepository;
    }

    @Transactional
    public UUID createLeagueForUser(UUID userId) {
        League league = new League();
        league = leagueRepository.save(league);

        TeamFactory teamFactory = new TeamFactory();
        List<Team> teams = teamFactory.generateTeams(league, 32);
        teams = teamRepository.saveAll(teams);

        PlayerFactory playerFactory = new PlayerFactory();
        for (Team team : teams) {
            List<Player> players = playerFactory.generatePlayersForTeam(team);
            playerRepository.saveAll(players);
        }

        List<Team> division4Teams = teams.stream()
            .filter(t -> t.getDivisionLevel() == 4)
            .toList();

        Team assignedTeam = division4Teams.get(new Random().nextInt(division4Teams.size()));

        UserLeague userLeague = new UserLeague();
        userLeague.setUserId(userId);
        userLeague.setLeagueId(league.getId());
        userLeague.setTeam(assignedTeam);
        userLeagueRepository.save(userLeague);

        return league.getId();
    }

    @Transactional
    public void assignUserToLeague(UUID userId, UUID leagueId) {
        List<Team> availableTeams = teamRepository.findAvailableTeamsByLeague(leagueId);
        
        if (availableTeams.isEmpty()) {
            throw new IllegalStateException("No available teams in this league");
        }

        List<Team> division4Available = availableTeams.stream()
            .filter(t -> t.getDivisionLevel() == 4)
            .toList();

        Team assignedTeam;
        if (!division4Available.isEmpty()) {
            assignedTeam = division4Available.get(new Random().nextInt(division4Available.size()));
        } else {
            assignedTeam = availableTeams.get(new Random().nextInt(availableTeams.size()));
        }

        UserLeague userLeague = new UserLeague();
        userLeague.setUserId(userId);
        userLeague.setLeagueId(leagueId);
        userLeague.setTeam(assignedTeam);
        userLeagueRepository.save(userLeague);
    }

    public List<UserLeague> getUserLeagues(UUID userId) {
        return userLeagueRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public LeagueDTO getLeagueDashboard(UUID leagueId) {
        List<Team> teams = teamRepository.findByLeagueId(leagueId);
        
        Map<Integer, List<TeamDTO>> divisions = teams.stream()
            .collect(Collectors.groupingBy(
                Team::getDivisionLevel,
                LinkedHashMap::new,
                Collectors.mapping(
                    t -> new TeamDTO(
                        t.getId(),
                        t.getName(),
                        t.getColorPrimary(),
                        t.getColorSecondary(),
                        t.getDivisionLevel()
                    ),
                    Collectors.toList()
                )
            ));
        
        return new LeagueDTO(divisions);
    }
}
