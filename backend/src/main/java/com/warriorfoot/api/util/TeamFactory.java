package com.warriorfoot.api.util;

import com.warriorfoot.api.model.entity.League;
import com.warriorfoot.api.model.entity.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeamFactory {

    private static final String[] CITIES = {
        "Manchester", "Liverpool", "Madrid", "Barcelona", "Munich", "Milan", "Turin", "Rome",
        "Paris", "Lyon", "London", "Lisbon", "Porto", "Amsterdam", "Berlin", "Hamburg",
        "Vienna", "Prague", "Warsaw", "Budapest", "Athens", "Istanbul", "Moscow", "Kiev",
        "Copenhagen", "Stockholm", "Oslo", "Helsinki", "Dublin", "Glasgow", "Cardiff", "Brussels"
    };

    private static final String[] SUFFIXES = {
        "United", "City", "Athletic", "FC", "Rovers", "Wanderers", "Rangers", "Albion"
    };

    private static final String[] COLORS = {
        "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF",
        "#800000", "#008000", "#000080", "#808000", "#800080", "#008080",
        "#FFA500", "#A52A2A", "#800080", "#000000", "#FFFFFF", "#808080",
        "#FFD700", "#C0C0C0", "#CD7F32", "#FF1493", "#4B0082", "#FF4500"
    };

    private final Random random;

    public TeamFactory() {
        this.random = new Random();
    }

    public TeamFactory(long seed) {
        this.random = new Random(seed);
    }

    public List<Team> generateTeams(League league, int totalTeams) {
        List<Team> teams = new ArrayList<>();
        List<String> usedNames = new ArrayList<>();
        
        int teamsPerDivision = totalTeams / 4;
        
        for (int division = 4; division >= 1; division--) {
            for (int i = 0; i < teamsPerDivision; i++) {
                Team team = new Team();
                team.setLeague(league);
                team.setName(generateUniqueName(usedNames));
                team.setColorPrimary(COLORS[random.nextInt(COLORS.length)]);
                team.setColorSecondary(COLORS[random.nextInt(COLORS.length)]);
                team.setDivisionLevel(division);
                teams.add(team);
            }
        }
        
        return teams;
    }

    private String generateUniqueName(List<String> usedNames) {
        String name;
        int attempts = 0;
        do {
            String city = CITIES[random.nextInt(CITIES.length)];
            String suffix = SUFFIXES[random.nextInt(SUFFIXES.length)];
            name = city + " " + suffix;
            attempts++;
        } while (usedNames.contains(name) && attempts < 100);
        
        usedNames.add(name);
        return name;
    }
}
