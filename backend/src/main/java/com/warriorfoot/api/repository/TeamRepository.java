package com.warriorfoot.api.repository;

import com.warriorfoot.api.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    List<Team> findByLeagueId(UUID leagueId);
    
    List<Team> findByLeagueIdAndDivisionLevel(UUID leagueId, Integer divisionLevel);
    
    @Query("SELECT t FROM Team t WHERE t.league.id = :leagueId AND t.id NOT IN " +
           "(SELECT ul.team.id FROM UserLeague ul WHERE ul.leagueId = :leagueId)")
    List<Team> findAvailableTeamsByLeague(@Param("leagueId") UUID leagueId);
}
