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
