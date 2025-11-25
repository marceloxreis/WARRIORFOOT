package com.warriorfoot.api.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invites")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    private User inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @Column(name = "invitee_email", nullable = false)
    private String inviteeEmail;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (expiresAt == null) {
            expiresAt = createdAt.plusDays(7);
        }
        if (status == null) {
            status = InviteStatus.PENDING;
        }
    }

    public enum InviteStatus {
        PENDING, ACCEPTED, EXPIRED
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getInviter() {
        return inviter;
    }

    public void setInviter(User inviter) {
        this.inviter = inviter;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public String getInviteeEmail() {
        return inviteeEmail;
    }

    public void setInviteeEmail(String inviteeEmail) {
        this.inviteeEmail = inviteeEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public InviteStatus getStatus() {
        return status;
    }

    public void setStatus(InviteStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
