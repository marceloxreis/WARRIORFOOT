package com.warriorfoot.api.model.dto;

import java.util.UUID;

public record PlayerDetailsDTO(
    UUID id,
    String name,
    Integer age,
    String position,
    Integer overall,
    Long marketValue,
    
    Integer pace,
    Integer acceleration,
    Integer sprintSpeed,
    
    Integer shooting,
    Integer attPosition,
    Integer finishing,
    Integer shotPower,
    Integer longShots,
    Integer volleys,
    Integer penalties,
    
    Integer passing,
    Integer vision,
    Integer crossing,
    Integer fkAcc,
    Integer shortPass,
    Integer longPass,
    Integer curve,
    
    Integer dribbling,
    Integer agility,
    Integer balance,
    Integer reactions,
    Integer ballControl,
    Integer dribblingSkill,
    Integer composure,
    
    Integer defending,
    Integer interceptions,
    Integer headingAcc,
    Integer defAware,
    Integer standTackle,
    Integer slideTackle,
    
    Integer physical,
    Integer jumping,
    Integer stamina,
    Integer strength,
    Integer aggression,
    
    Integer diving,
    Integer handling,
    Integer kicking,
    Integer reflexes,
    Integer speed,
    Integer positioning
) {}
