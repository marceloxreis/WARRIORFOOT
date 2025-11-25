package com.warriorfoot.api.util;

import com.warriorfoot.api.model.entity.Player;
import com.warriorfoot.api.model.entity.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerFactory {

    private static final String[] FIRST_NAMES = {
        "John", "Michael", "David", "James", "Robert", "William", "Richard", "Thomas", "Daniel", "Paul",
        "Mark", "George", "Steven", "Andrew", "Peter", "Joseph", "Kenneth", "Joshua", "Kevin", "Brian",
        "Carlos", "Luis", "Diego", "Marco", "Antonio", "Jose", "Juan", "Miguel", "Pedro", "Fernando",
        "Andre", "Paulo", "Ricardo", "Roberto", "Alessandro", "Francesco", "Giovanni", "Lorenzo", "Matteo",
        "Lucas", "Pierre", "Jean", "Antoine", "Alexandre", "Nicolas", "Thomas", "Hugo", "Theo", "Louis"
    };

    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
        "Silva", "Santos", "Oliveira", "Costa", "Fernandez", "Perez", "Gonzalez", "Lopez", "Sanchez", "Ramirez",
        "Rossi", "Russo", "Ferrari", "Esposito", "Bianchi", "Romano", "Colombo", "Ricci", "Marino", "Greco",
        "Müller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner", "Becker", "Schulz", "Hoffmann",
        "Dubois", "Martin", "Bernard", "Petit", "Robert", "Richard", "Durand", "Leroy", "Moreau", "Simon"
    };

    private final Random random;

    public PlayerFactory() {
        this.random = new Random();
    }

    public PlayerFactory(long seed) {
        this.random = new Random(seed);
    }

    public List<Player> generatePlayersForTeam(Team team) {
        List<Player> players = new ArrayList<>();
        int divisionLevel = team.getDivisionLevel();
        
        players.addAll(generateByPosition(team, Player.Position.GK, 2, divisionLevel));
        players.addAll(generateByPosition(team, Player.Position.DF, 6, divisionLevel));
        players.addAll(generateByPosition(team, Player.Position.MF, 8, divisionLevel));
        players.addAll(generateByPosition(team, Player.Position.FW, 6, divisionLevel));
        
        return players;
    }

    private List<Player> generateByPosition(Team team, Player.Position position, int count, int divisionLevel) {
        List<Player> players = new ArrayList<>();
        
        int baseMean = switch (divisionLevel) {
            case 1 -> 80;
            case 2 -> 70;
            case 3 -> 60;
            case 4 -> 55;
            default -> 50;
        };
        
        int stdDev = switch (divisionLevel) {
            case 1 -> 5;
            case 2 -> 6;
            case 3 -> 7;
            case 4 -> 8;
            default -> 10;
        };
        
        for (int i = 0; i < count; i++) {
            Player player = new Player();
            player.setTeam(team);
            player.setName(generateName());
            player.setAge(16 + random.nextInt(25));
            player.setPosition(position);
            
            int overall = generateOverall(baseMean, stdDev);
            player.setOverall(overall);
            generateStats(player, overall, position);
            player.setMarketValue(calculateMarketValue(overall, player.getAge(), position));
            
            players.add(player);
        }
        
        return players;
    }

    private String generateName() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    private int generateOverall(int mean, int stdDev) {
        double gaussian = random.nextGaussian();
        int overall = (int) Math.round(mean + (gaussian * stdDev));
        return Math.max(40, Math.min(99, overall));
    }

    private void generateStats(Player player, int overall, Player.Position position) {
        switch (position) {
            case GK -> generateGKStats(player, overall);
            case DF -> generateDFStats(player, overall);
            case MF -> generateMFStats(player, overall);
            case FW -> generateFWStats(player, overall);
        }
    }

    private void generateGKStats(Player player, int overall) {
        player.setDiving(varyStat(overall, 5));
        player.setHandling(varyStat(overall, 5));
        player.setKicking(varyStat(overall, 8));
        player.setReflexes(varyStat(overall, 5));
        player.setSpeed(varyStat(overall - 20, 5));
        player.setPositioning(varyStat(overall, 5));
        
        player.setPace(varyStat(overall - 25, 5));
        player.setAcceleration(varyStat(overall - 25, 5));
        player.setSprintSpeed(varyStat(overall - 25, 5));
        player.setPhysical(varyStat(overall - 10, 5));
        player.setJumping(varyStat(overall - 5, 5));
        player.setStrength(varyStat(overall - 10, 5));
        player.setStamina(varyStat(overall - 15, 5));
        player.setAggression(varyStat(overall - 15, 10));
    }

    private void generateDFStats(Player player, int overall) {
        player.setDefending(varyStat(overall, 5));
        player.setInterceptions(varyStat(overall, 5));
        player.setHeadingAcc(varyStat(overall - 5, 5));
        player.setDefAware(varyStat(overall, 5));
        player.setStandTackle(varyStat(overall, 5));
        player.setSlideTackle(varyStat(overall - 5, 5));
        
        player.setPhysical(varyStat(overall - 5, 5));
        player.setJumping(varyStat(overall - 10, 5));
        player.setStrength(varyStat(overall - 5, 5));
        player.setStamina(varyStat(overall - 5, 5));
        player.setAggression(varyStat(overall - 10, 10));
        
        player.setPace(varyStat(overall - 15, 8));
        player.setAcceleration(varyStat(overall - 15, 8));
        player.setSprintSpeed(varyStat(overall - 15, 8));
        
        player.setPassing(varyStat(overall - 10, 8));
        player.setShortPass(varyStat(overall - 10, 8));
        player.setLongPass(varyStat(overall - 15, 10));
        player.setVision(varyStat(overall - 15, 10));
        
        player.setShooting(varyStat(overall - 20, 10));
        player.setFinishing(varyStat(overall - 25, 10));
        player.setLongShots(varyStat(overall - 20, 10));
    }

    private void generateMFStats(Player player, int overall) {
        player.setPassing(varyStat(overall, 5));
        player.setVision(varyStat(overall, 5));
        player.setShortPass(varyStat(overall, 5));
        player.setLongPass(varyStat(overall - 5, 5));
        player.setCrossing(varyStat(overall - 5, 8));
        player.setCurve(varyStat(overall - 5, 8));
        player.setFkAcc(varyStat(overall - 10, 10));
        
        player.setDribbling(varyStat(overall - 5, 5));
        player.setBallControl(varyStat(overall, 5));
        player.setDribblingSkill(varyStat(overall - 5, 8));
        player.setAgility(varyStat(overall - 5, 5));
        player.setBalance(varyStat(overall - 5, 5));
        player.setReactions(varyStat(overall - 5, 5));
        player.setComposure(varyStat(overall - 5, 8));
        
        player.setPace(varyStat(overall - 10, 8));
        player.setAcceleration(varyStat(overall - 10, 8));
        player.setSprintSpeed(varyStat(overall - 10, 8));
        
        player.setPhysical(varyStat(overall - 10, 8));
        player.setStamina(varyStat(overall - 5, 5));
        player.setStrength(varyStat(overall - 10, 8));
        player.setJumping(varyStat(overall - 15, 8));
        player.setAggression(varyStat(overall - 15, 10));
        
        player.setShooting(varyStat(overall - 10, 8));
        player.setFinishing(varyStat(overall - 15, 10));
        player.setLongShots(varyStat(overall - 10, 10));
        player.setVolleys(varyStat(overall - 15, 10));
        player.setShotPower(varyStat(overall - 10, 10));
        player.setPenalties(varyStat(overall - 15, 10));
        
        player.setDefending(varyStat(overall - 15, 10));
        player.setInterceptions(varyStat(overall - 15, 10));
        player.setStandTackle(varyStat(overall - 20, 10));
    }

    private void generateFWStats(Player player, int overall) {
        player.setShooting(varyStat(overall, 5));
        player.setFinishing(varyStat(overall, 5));
        player.setShotPower(varyStat(overall - 5, 5));
        player.setLongShots(varyStat(overall - 5, 8));
        player.setVolleys(varyStat(overall - 8, 8));
        player.setPenalties(varyStat(overall - 5, 8));
        player.setAttPosition(varyStat(overall, 5));
        
        player.setPace(varyStat(overall - 5, 5));
        player.setAcceleration(varyStat(overall - 5, 5));
        player.setSprintSpeed(varyStat(overall - 5, 5));
        
        player.setDribbling(varyStat(overall - 5, 8));
        player.setBallControl(varyStat(overall - 5, 8));
        player.setDribblingSkill(varyStat(overall - 10, 8));
        player.setAgility(varyStat(overall - 8, 8));
        player.setBalance(varyStat(overall - 10, 8));
        player.setReactions(varyStat(overall - 5, 5));
        player.setComposure(varyStat(overall - 5, 8));
        
        player.setPhysical(varyStat(overall - 10, 8));
        player.setStrength(varyStat(overall - 10, 10));
        player.setJumping(varyStat(overall - 10, 8));
        player.setStamina(varyStat(overall - 10, 8));
        player.setAggression(varyStat(overall - 10, 10));
        
        player.setPassing(varyStat(overall - 15, 10));
        player.setShortPass(varyStat(overall - 15, 10));
        player.setVision(varyStat(overall - 15, 10));
        player.setCrossing(varyStat(overall - 20, 10));
        
        player.setDefending(varyStat(overall - 30, 10));
        player.setHeadingAcc(varyStat(overall - 10, 10));
    }

    private int varyStat(int base, int variance) {
        int stat = base + random.nextInt(variance * 2 + 1) - variance;
        return Math.max(30, Math.min(99, stat));
    }

    private long calculateMarketValue(int overall, int age, Player.Position position) {
        double positionMultiplier = switch (position) {
            case FW -> 1.3;
            case MF -> 1.1;
            case DF -> 0.9;
            case GK -> 0.8;
        };
        
        double ageFactor = 1.0;
        if (age < 24) {
            ageFactor = 1.2;
        } else if (age > 30) {
            ageFactor = 0.8;
        }
        
        long baseValue = (long) (Math.pow(overall, 2) * 1000);
        return (long) (baseValue * positionMultiplier * ageFactor);
    }
}
