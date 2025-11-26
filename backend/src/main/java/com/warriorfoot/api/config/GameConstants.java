package com.warriorfoot.api.config;

/**
 * Game configuration constants
 */
public final class GameConstants {

    private GameConstants() {
        // Prevent instantiation
    }

    // League Configuration
    public static final int TOTAL_TEAMS_PER_LEAGUE = 32;
    public static final int TOTAL_DIVISIONS = 4;
    public static final int STARTING_DIVISION = 4;
    public static final int TEAMS_PER_DIVISION = 8;

    // Player Configuration
    public static final int PLAYERS_PER_TEAM = 22;
    public static final int GOALKEEPERS_PER_TEAM = 2;
    public static final int DEFENDERS_PER_TEAM = 6;
    public static final int MIDFIELDERS_PER_TEAM = 8;
    public static final int FORWARDS_PER_TEAM = 6;

    // Player Attributes
    public static final int MIN_PLAYER_AGE = 16;
    public static final int MAX_PLAYER_AGE = 40;
    public static final int PLAYER_AGE_RANGE = 25; // Used for random generation
    public static final int MIN_PLAYER_STAT = 35;
    public static final int MAX_PLAYER_STAT = 99;
    public static final int MIN_PLAYER_OVERALL = 40;
    public static final int MAX_PLAYER_OVERALL = 99;

    // Invite Configuration
    public static final int INVITE_EXPIRATION_DAYS = 7;

    // Default Values
    public static final String DEFAULT_LEAGUE_NAME = "My First League";
}
