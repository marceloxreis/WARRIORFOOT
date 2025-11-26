package com.warriorfoot.api.repository;

import com.warriorfoot.api.model.entity.UserLeague;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserLeagueRepository extends JpaRepository<UserLeague, UserLeague.UserLeagueId> {
    List<UserLeague> findByUserId(UUID userId);
    Optional<UserLeague> findByUserIdAndLeagueId(UUID userId, UUID leagueId);
    Optional<UserLeague> findFirstByLeagueIdOrderByJoinedAtAsc(UUID leagueId);
    List<UserLeague> findByLeagueId(UUID leagueId);
    Optional<UserLeague> findByTeamId(UUID teamId);
}
