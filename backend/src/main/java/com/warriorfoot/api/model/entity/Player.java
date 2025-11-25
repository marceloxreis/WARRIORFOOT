package com.warriorfoot.api.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @Column(nullable = false)
    private Integer overall;

    @Column(name = "market_value", nullable = false)
    private Long marketValue;

    private Integer pace;
    private Integer acceleration;
    @Column(name = "sprint_speed")
    private Integer sprintSpeed;
    
    private Integer shooting;
    @Column(name = "att_position")
    private Integer attPosition;
    private Integer finishing;
    @Column(name = "shot_power")
    private Integer shotPower;
    @Column(name = "long_shots")
    private Integer longShots;
    private Integer volleys;
    private Integer penalties;
    
    private Integer passing;
    private Integer vision;
    private Integer crossing;
    @Column(name = "fk_acc")
    private Integer fkAcc;
    @Column(name = "short_pass")
    private Integer shortPass;
    @Column(name = "long_pass")
    private Integer longPass;
    private Integer curve;
    
    private Integer dribbling;
    private Integer agility;
    private Integer balance;
    private Integer reactions;
    @Column(name = "ball_control")
    private Integer ballControl;
    @Column(name = "dribbling_skill")
    private Integer dribblingSkill;
    private Integer composure;
    
    private Integer defending;
    private Integer interceptions;
    @Column(name = "heading_acc")
    private Integer headingAcc;
    @Column(name = "def_aware")
    private Integer defAware;
    @Column(name = "stand_tackle")
    private Integer standTackle;
    @Column(name = "slide_tackle")
    private Integer slideTackle;
    
    private Integer physical;
    private Integer jumping;
    private Integer stamina;
    private Integer strength;
    private Integer aggression;
    
    private Integer diving;
    private Integer handling;
    private Integer kicking;
    private Integer reflexes;
    private Integer speed;
    private Integer positioning;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Position {
        GK, DF, MF, FW
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public Long getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Long marketValue) {
        this.marketValue = marketValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPace() { return pace; }
    public void setPace(Integer pace) { this.pace = pace; }
    public Integer getAcceleration() { return acceleration; }
    public void setAcceleration(Integer acceleration) { this.acceleration = acceleration; }
    public Integer getSprintSpeed() { return sprintSpeed; }
    public void setSprintSpeed(Integer sprintSpeed) { this.sprintSpeed = sprintSpeed; }
    public Integer getShooting() { return shooting; }
    public void setShooting(Integer shooting) { this.shooting = shooting; }
    public Integer getAttPosition() { return attPosition; }
    public void setAttPosition(Integer attPosition) { this.attPosition = attPosition; }
    public Integer getFinishing() { return finishing; }
    public void setFinishing(Integer finishing) { this.finishing = finishing; }
    public Integer getShotPower() { return shotPower; }
    public void setShotPower(Integer shotPower) { this.shotPower = shotPower; }
    public Integer getLongShots() { return longShots; }
    public void setLongShots(Integer longShots) { this.longShots = longShots; }
    public Integer getVolleys() { return volleys; }
    public void setVolleys(Integer volleys) { this.volleys = volleys; }
    public Integer getPenalties() { return penalties; }
    public void setPenalties(Integer penalties) { this.penalties = penalties; }
    public Integer getPassing() { return passing; }
    public void setPassing(Integer passing) { this.passing = passing; }
    public Integer getVision() { return vision; }
    public void setVision(Integer vision) { this.vision = vision; }
    public Integer getCrossing() { return crossing; }
    public void setCrossing(Integer crossing) { this.crossing = crossing; }
    public Integer getFkAcc() { return fkAcc; }
    public void setFkAcc(Integer fkAcc) { this.fkAcc = fkAcc; }
    public Integer getShortPass() { return shortPass; }
    public void setShortPass(Integer shortPass) { this.shortPass = shortPass; }
    public Integer getLongPass() { return longPass; }
    public void setLongPass(Integer longPass) { this.longPass = longPass; }
    public Integer getCurve() { return curve; }
    public void setCurve(Integer curve) { this.curve = curve; }
    public Integer getDribbling() { return dribbling; }
    public void setDribbling(Integer dribbling) { this.dribbling = dribbling; }
    public Integer getAgility() { return agility; }
    public void setAgility(Integer agility) { this.agility = agility; }
    public Integer getBalance() { return balance; }
    public void setBalance(Integer balance) { this.balance = balance; }
    public Integer getReactions() { return reactions; }
    public void setReactions(Integer reactions) { this.reactions = reactions; }
    public Integer getBallControl() { return ballControl; }
    public void setBallControl(Integer ballControl) { this.ballControl = ballControl; }
    public Integer getDribblingSkill() { return dribblingSkill; }
    public void setDribblingSkill(Integer dribblingSkill) { this.dribblingSkill = dribblingSkill; }
    public Integer getComposure() { return composure; }
    public void setComposure(Integer composure) { this.composure = composure; }
    public Integer getDefending() { return defending; }
    public void setDefending(Integer defending) { this.defending = defending; }
    public Integer getInterceptions() { return interceptions; }
    public void setInterceptions(Integer interceptions) { this.interceptions = interceptions; }
    public Integer getHeadingAcc() { return headingAcc; }
    public void setHeadingAcc(Integer headingAcc) { this.headingAcc = headingAcc; }
    public Integer getDefAware() { return defAware; }
    public void setDefAware(Integer defAware) { this.defAware = defAware; }
    public Integer getStandTackle() { return standTackle; }
    public void setStandTackle(Integer standTackle) { this.standTackle = standTackle; }
    public Integer getSlideTackle() { return slideTackle; }
    public void setSlideTackle(Integer slideTackle) { this.slideTackle = slideTackle; }
    public Integer getPhysical() { return physical; }
    public void setPhysical(Integer physical) { this.physical = physical; }
    public Integer getJumping() { return jumping; }
    public void setJumping(Integer jumping) { this.jumping = jumping; }
    public Integer getStamina() { return stamina; }
    public void setStamina(Integer stamina) { this.stamina = stamina; }
    public Integer getStrength() { return strength; }
    public void setStrength(Integer strength) { this.strength = strength; }
    public Integer getAggression() { return aggression; }
    public void setAggression(Integer aggression) { this.aggression = aggression; }
    public Integer getDiving() { return diving; }
    public void setDiving(Integer diving) { this.diving = diving; }
    public Integer getHandling() { return handling; }
    public void setHandling(Integer handling) { this.handling = handling; }
    public Integer getKicking() { return kicking; }
    public void setKicking(Integer kicking) { this.kicking = kicking; }
    public Integer getReflexes() { return reflexes; }
    public void setReflexes(Integer reflexes) { this.reflexes = reflexes; }
    public Integer getSpeed() { return speed; }
    public void setSpeed(Integer speed) { this.speed = speed; }
    public Integer getPositioning() { return positioning; }
    public void setPositioning(Integer positioning) { this.positioning = positioning; }
}
