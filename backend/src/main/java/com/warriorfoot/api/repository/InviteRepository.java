package com.warriorfoot.api.repository;

import com.warriorfoot.api.model.entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InviteRepository extends JpaRepository<Invite, UUID> {
    Optional<Invite> findByToken(String token);
    List<Invite> findByInviteeEmailAndStatus(String email, Invite.InviteStatus status);
    List<Invite> findByLeagueIdAndStatus(UUID leagueId, Invite.InviteStatus status);
}
