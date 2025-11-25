package com.warriorfoot.api.service;

import com.warriorfoot.api.model.entity.*;
import com.warriorfoot.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InviteService {

    private final InviteRepository inviteRepository;
    private final UserRepository userRepository;
    private final UserLeagueRepository userLeagueRepository;
    private final TeamRepository teamRepository;
    private final EmailService emailService;

    public InviteService(
            InviteRepository inviteRepository,
            UserRepository userRepository,
            UserLeagueRepository userLeagueRepository,
            TeamRepository teamRepository,
            EmailService emailService) {
        this.inviteRepository = inviteRepository;
        this.userRepository = userRepository;
        this.userLeagueRepository = userLeagueRepository;
        this.teamRepository = teamRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Invite createInvite(UUID inviterId, UUID leagueId, String inviteeEmail, String inviteeName) {
        User inviter = userRepository.findById(inviterId)
                .orElseThrow(() -> new IllegalArgumentException("Inviter not found"));

        // Validate inviter is in the league
        userLeagueRepository.findByUserIdAndLeagueId(inviterId, leagueId)
                .orElseThrow(() -> new IllegalArgumentException("You are not a member of this league"));

        // Validate email format (basic check, detailed validation handled by controller)
        if (inviteeEmail == null || !inviteeEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Validate inviter email != invitee email
        if (inviter.getEmail().equalsIgnoreCase(inviteeEmail)) {
            throw new IllegalArgumentException("You cannot invite yourself");
        }

        // Create invite
        Invite invite = new Invite();
        invite.setInviter(inviter);

        League league = new League();
        league.setId(leagueId);
        invite.setLeague(league);

        invite.setInviteeEmail(inviteeEmail.toLowerCase());
        invite.setInviteeName(inviteeName);
        invite.setToken(UUID.randomUUID().toString());
        invite.setStatus(Invite.InviteStatus.PENDING);
        invite.setExpiresAt(LocalDateTime.now().plusDays(7));

        Invite savedInvite = inviteRepository.save(invite);

        // Send email
        emailService.sendInviteEmail(inviteeEmail, inviteeName, inviter.getFullName(), savedInvite.getToken());

        return savedInvite;
    }

    public Invite validateInviteToken(String token) {
        Invite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invite token"));

        if (invite.getStatus() != Invite.InviteStatus.PENDING) {
            throw new IllegalArgumentException("This invite has already been " + invite.getStatus().toString().toLowerCase());
        }

        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            invite.setStatus(Invite.InviteStatus.EXPIRED);
            inviteRepository.save(invite);
            throw new IllegalArgumentException("This invite has expired");
        }

        return invite;
    }

    @Transactional
    public void acceptInvite(String token, UUID newUserId) {
        Invite invite = validateInviteToken(token);
        invite.setStatus(Invite.InviteStatus.ACCEPTED);
        inviteRepository.save(invite);
    }

    public List<Team> getAvailableDivision4Teams(UUID leagueId) {
        List<Team> allDivision4Teams = teamRepository.findByLeagueIdAndDivisionLevel(leagueId, 4);

        // Filter out teams already assigned to users
        return allDivision4Teams.stream()
                .filter(team -> userLeagueRepository.findByUserIdAndLeagueId(null, leagueId).isEmpty() ||
                                !isTeamAssigned(team.getId(), leagueId))
                .collect(Collectors.toList());
    }

    private boolean isTeamAssigned(UUID teamId, UUID leagueId) {
        List<UserLeague> allUsersInLeague = userLeagueRepository.findByLeagueId(leagueId);
        return allUsersInLeague.stream()
                .anyMatch(ul -> ul.getTeam().getId().equals(teamId));
    }

    public Invite getInviteByToken(String token) {
        return inviteRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invite not found"));
    }
}
