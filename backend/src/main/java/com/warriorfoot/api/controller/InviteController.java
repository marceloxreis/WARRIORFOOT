package com.warriorfoot.api.controller;

import com.warriorfoot.api.model.dto.*;
import com.warriorfoot.api.model.entity.Invite;
import com.warriorfoot.api.model.entity.Team;
import com.warriorfoot.api.model.entity.User;
import com.warriorfoot.api.model.entity.UserLeague;
import com.warriorfoot.api.repository.UserRepository;
import com.warriorfoot.api.service.InviteService;
import com.warriorfoot.api.service.LeagueService;
import com.warriorfoot.api.service.SessionService;
import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/invites")
public class InviteController {

    private final InviteService inviteService;
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final LeagueService leagueService;

    public InviteController(
            InviteService inviteService,
            SessionService sessionService,
            UserRepository userRepository,
            LeagueService leagueService) {
        this.inviteService = inviteService;
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        this.leagueService = leagueService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendInvite(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody InviteRequest request) {
        try {
            String sessionToken = authHeader.replace("Bearer ", "");
            UUID userId = sessionService.getUserIdFromSession(sessionToken);
            UUID activeLeagueId = sessionService.getActiveLeagueIdFromSession(sessionToken);

            if (activeLeagueId == null) {
                return ResponseEntity.badRequest().build();
            }

            inviteService.createInvite(userId, activeLeagueId, request.inviteeEmail(), request.inviteeName());
            return ResponseEntity.status(201).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<InviteValidationResponse> validateInvite(@RequestParam String token) {
        try {
            Invite invite = inviteService.validateInviteToken(token);

            return ResponseEntity.ok(new InviteValidationResponse(
                true,
                invite.getInviter().getFullName(),
                invite.getInviteeName(),
                invite.getLeague().getId(),
                null
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new InviteValidationResponse(
                false,
                null,
                null,
                null,
                e.getMessage()
            ));
        }
    }

    @GetMapping("/available-teams")
    public ResponseEntity<List<AvailableTeamDTO>> getAvailableTeams(@RequestParam UUID leagueId) {
        List<Team> teams = inviteService.getAvailableDivision4Teams(leagueId);

        List<AvailableTeamDTO> dtos = teams.stream()
            .map(t -> new AvailableTeamDTO(
                t.getId(),
                t.getName(),
                t.getColorPrimary(),
                t.getColorSecondary()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/accept")
    public ResponseEntity<AuthResponse> acceptInvite(@Valid @RequestBody AcceptInviteRequest request) {
        try {
            // Validate passwords match
            if (!request.password().equals(request.passwordConfirmation())) {
                return ResponseEntity.badRequest().build();
            }

            // Validate invite token
            Invite invite = inviteService.validateInviteToken(request.token());

            // Check if user already exists
            User user = userRepository.findByEmail(request.email()).orElse(null);
            boolean isNewUser = (user == null);

            if (isNewUser) {
                // Create new user
                user = new User();
                user.setFullName(request.fullName());
                user.setEmail(request.email());
                user.setPasswordHash(BCrypt.hashpw(request.password(), BCrypt.gensalt()));
                user = userRepository.save(user);
            } else {
                // For existing users, verify password
                if (!BCrypt.checkpw(request.password(), user.getPasswordHash())) {
                    return ResponseEntity.status(401).build();
                }
            }

            // Assign user to league with selected team
            leagueService.assignUserToLeagueWithTeam(user.getId(), invite.getLeague().getId(), request.selectedTeamId());

            // Mark invite as accepted
            inviteService.acceptInvite(request.token(), user.getId());

            // Get user's league info
            List<UserLeague> userLeagues = leagueService.getUserLeagues(user.getId());
            UserLeague newLeague = userLeagues.stream()
                .filter(ul -> ul.getLeagueId().equals(invite.getLeague().getId()))
                .findFirst()
                .orElseThrow();

            // Create session
            String sessionToken = sessionService.createSession(user.getId(), invite.getLeague().getId());

            return ResponseEntity.status(201).body(new AuthResponse(
                sessionToken,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                invite.getLeague().getId(),
                newLeague.getTeam().getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
