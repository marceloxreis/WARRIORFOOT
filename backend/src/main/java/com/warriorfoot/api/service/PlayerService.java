package com.warriorfoot.api.service;

import com.warriorfoot.api.model.dto.PlayerDetailsDTO;
import com.warriorfoot.api.model.entity.Player;
import com.warriorfoot.api.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public PlayerDetailsDTO getPlayerDetails(UUID playerId) {
        Player p = playerRepository.findById(playerId)
            .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        
        return new PlayerDetailsDTO(
            p.getId(),
            p.getName(),
            p.getAge(),
            p.getPosition().name(),
            p.getOverall(),
            p.getMarketValue(),
            p.getPace(),
            p.getAcceleration(),
            p.getSprintSpeed(),
            p.getShooting(),
            p.getAttPosition(),
            p.getFinishing(),
            p.getShotPower(),
            p.getLongShots(),
            p.getVolleys(),
            p.getPenalties(),
            p.getPassing(),
            p.getVision(),
            p.getCrossing(),
            p.getFkAcc(),
            p.getShortPass(),
            p.getLongPass(),
            p.getCurve(),
            p.getDribbling(),
            p.getAgility(),
            p.getBalance(),
            p.getReactions(),
            p.getBallControl(),
            p.getDribblingSkill(),
            p.getComposure(),
            p.getDefending(),
            p.getInterceptions(),
            p.getHeadingAcc(),
            p.getDefAware(),
            p.getStandTackle(),
            p.getSlideTackle(),
            p.getPhysical(),
            p.getJumping(),
            p.getStamina(),
            p.getStrength(),
            p.getAggression(),
            p.getDiving(),
            p.getHandling(),
            p.getKicking(),
            p.getReflexes(),
            p.getSpeed(),
            p.getPositioning()
        );
    }
}
