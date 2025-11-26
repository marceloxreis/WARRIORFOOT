package com.warriorfoot.api.service;

import com.warriorfoot.api.config.GameConstants;
import com.warriorfoot.api.model.dto.AuthResponse;
import com.warriorfoot.api.model.dto.LoginRequest;
import com.warriorfoot.api.model.dto.RegisterRequest;
import com.warriorfoot.api.model.entity.User;
import com.warriorfoot.api.model.entity.UserLeague;
import com.warriorfoot.api.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final LeagueService leagueService;
    private final SessionService sessionService;

    public AuthService(UserRepository userRepository,
                       LeagueService leagueService,
                       SessionService sessionService) {
        this.userRepository = userRepository;
        this.leagueService = leagueService;
        this.sessionService = sessionService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (!request.password().equals(request.passwordConfirmation())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPasswordHash(BCrypt.hashpw(request.password(), BCrypt.gensalt()));
        user = userRepository.save(user);

        UUID leagueId = leagueService.createLeagueForUser(user.getId(), GameConstants.DEFAULT_LEAGUE_NAME);

        List<UserLeague> userLeagues = leagueService.getUserLeagues(user.getId());
        UUID teamId = userLeagues.get(0).getTeam().getId();

        String sessionToken = sessionService.createSession(user.getId(), leagueId);

        return new AuthResponse(
            sessionToken,
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            leagueId,
            teamId
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!BCrypt.checkpw(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        List<UserLeague> userLeagues = leagueService.getUserLeagues(user.getId());
        if (userLeagues.isEmpty()) {
            throw new IllegalStateException("User has no associated leagues");
        }

        UserLeague primaryLeague = userLeagues.get(0);
        String sessionToken = sessionService.createSession(user.getId(), primaryLeague.getLeagueId());

        return new AuthResponse(
            sessionToken,
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            primaryLeague.getLeagueId(),
            primaryLeague.getTeam().getId()
        );
    }

    public void logout(String sessionToken) {
        sessionService.deleteSession(sessionToken);
    }
}
