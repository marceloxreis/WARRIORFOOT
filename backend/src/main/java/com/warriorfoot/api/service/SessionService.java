package com.warriorfoot.api.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionService {

    private static final String SESSION_PREFIX = "session:";
    private static final Duration SESSION_TTL = Duration.ofHours(24);

    private final RedisTemplate<String, Object> redisTemplate;

    public SessionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String createSession(UUID userId, UUID activeLeagueId) {
        String token = UUID.randomUUID().toString();
        String key = SESSION_PREFIX + token;

        Map<String, String> sessionData = new HashMap<>();
        sessionData.put("userId", userId.toString());
        sessionData.put("activeLeagueId", activeLeagueId.toString());

        redisTemplate.opsForHash().putAll(key, sessionData);
        redisTemplate.expire(key, SESSION_TTL);

        return token;
    }

    public Map<Object, Object> getSession(String token) {
        String key = SESSION_PREFIX + token;
        return redisTemplate.opsForHash().entries(key);
    }

    public void updateActiveLeague(String token, UUID leagueId) {
        String key = SESSION_PREFIX + token;
        redisTemplate.opsForHash().put(key, "activeLeagueId", leagueId.toString());
    }

    public void deleteSession(String token) {
        String key = SESSION_PREFIX + token;
        redisTemplate.delete(key);
    }

    public boolean validateSession(String token) {
        String key = SESSION_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public UUID getUserIdFromSession(String token) {
        Map<Object, Object> session = getSession(token);
        String userIdStr = (String) session.get("userId");
        if (userIdStr == null) {
            throw new IllegalArgumentException("Invalid session");
        }
        return UUID.fromString(userIdStr);
    }

    public UUID getActiveLeagueIdFromSession(String token) {
        Map<Object, Object> session = getSession(token);
        String leagueIdStr = (String) session.get("activeLeagueId");
        if (leagueIdStr == null) {
            return null;
        }
        return UUID.fromString(leagueIdStr);
    }
}
