package com.warriorfoot.api.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_leagues")
@IdClass(UserLeague.UserLeagueId.class)
public class UserLeague {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "league_id")
    private UUID leagueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", insertable = false, updatable = false)
    private League league;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(UUID leagueId) {
        this.leagueId = leagueId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public static class UserLeagueId implements Serializable {
        private UUID userId;
        private UUID leagueId;

        public UserLeagueId() {}

        public UserLeagueId(UUID userId, UUID leagueId) {
            this.userId = userId;
            this.leagueId = leagueId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserLeagueId that = (UserLeagueId) o;
            return Objects.equals(userId, that.userId) && Objects.equals(leagueId, that.leagueId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, leagueId);
        }
    }
}
