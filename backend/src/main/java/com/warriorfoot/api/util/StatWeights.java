package com.warriorfoot.api.util;

import com.warriorfoot.api.model.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class StatWeights {

    public enum WeightMode {
        FIXED,
        POSITIONAL
    }

    private static final Map<Player.Position, Map<String, Double>> POSITIONAL_WEIGHTS = new HashMap<>();

    static {
        Map<String, Double> gkWeights = new HashMap<>();
        gkWeights.put("diving", 1.5);
        gkWeights.put("handling", 1.5);
        gkWeights.put("kicking", 1.2);
        gkWeights.put("reflexes", 1.5);
        gkWeights.put("speed", 1.0);
        gkWeights.put("positioning", 1.3);
        gkWeights.put("pace", 0.3);
        gkWeights.put("physical", 0.5);
        gkWeights.put("shooting", 0.1);
        gkWeights.put("passing", 0.4);
        gkWeights.put("dribbling", 0.3);
        gkWeights.put("defending", 0.2);
        POSITIONAL_WEIGHTS.put(Player.Position.GK, gkWeights);

        Map<String, Double> dfWeights = new HashMap<>();
        dfWeights.put("pace", 0.7);
        dfWeights.put("shooting", 0.3);
        dfWeights.put("passing", 0.8);
        dfWeights.put("dribbling", 0.5);
        dfWeights.put("defending", 1.5);
        dfWeights.put("physical", 1.2);
        POSITIONAL_WEIGHTS.put(Player.Position.DF, dfWeights);

        Map<String, Double> mfWeights = new HashMap<>();
        mfWeights.put("pace", 0.9);
        mfWeights.put("shooting", 1.0);
        mfWeights.put("passing", 1.4);
        mfWeights.put("dribbling", 1.3);
        mfWeights.put("defending", 0.7);
        mfWeights.put("physical", 0.8);
        POSITIONAL_WEIGHTS.put(Player.Position.MF, mfWeights);

        Map<String, Double> fwWeights = new HashMap<>();
        fwWeights.put("pace", 1.3);
        fwWeights.put("shooting", 1.5);
        fwWeights.put("passing", 0.7);
        fwWeights.put("dribbling", 1.2);
        fwWeights.put("defending", 0.2);
        fwWeights.put("physical", 0.9);
        POSITIONAL_WEIGHTS.put(Player.Position.FW, fwWeights);
    }

    public static int calculateOverall(Player player, WeightMode mode) {
        if (mode == WeightMode.FIXED) {
            return calculateFixedOverall(player);
        } else {
            return calculatePositionalOverall(player);
        }
    }

    private static int calculateFixedOverall(Player player) {
        int overall;
        if (player.getPosition() == Player.Position.GK) {
            overall = (player.getDiving() + player.getHandling() + player.getKicking() +
                    player.getReflexes() + player.getSpeed() + player.getPositioning() +
                    player.getPace() + player.getPhysical()) / 8;
        } else {
            overall = (player.getPace() + player.getShooting() + player.getPassing() +
                    player.getDribbling() + player.getDefending() + player.getPhysical()) / 6;
        }
        return Math.max(40, Math.min(99, overall));
    }

    private static int calculatePositionalOverall(Player player) {
        Map<String, Double> weights = POSITIONAL_WEIGHTS.get(player.getPosition());
        
        if (player.getPosition() == Player.Position.GK) {
            double weightedSum = 
                player.getDiving() * weights.get("diving") +
                player.getHandling() * weights.get("handling") +
                player.getKicking() * weights.get("kicking") +
                player.getReflexes() * weights.get("reflexes") +
                player.getSpeed() * weights.get("speed") +
                player.getPositioning() * weights.get("positioning") +
                player.getPace() * weights.get("pace") +
                player.getPhysical() * weights.get("physical") +
                player.getShooting() * weights.get("shooting") +
                player.getPassing() * weights.get("passing") +
                player.getDribbling() * weights.get("dribbling") +
                player.getDefending() * weights.get("defending");
            
            double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();
            int overall = (int) Math.round(weightedSum / totalWeight);
            return Math.max(40, Math.min(99, overall));
        } else {
            double weightedSum =
                player.getPace() * weights.get("pace") +
                player.getShooting() * weights.get("shooting") +
                player.getPassing() * weights.get("passing") +
                player.getDribbling() * weights.get("dribbling") +
                player.getDefending() * weights.get("defending") +
                player.getPhysical() * weights.get("physical");
            
            double totalWeight = weights.values().stream().mapToDouble(Double::doubleValue).sum();
            int overall = (int) Math.round(weightedSum / totalWeight);
            return Math.max(40, Math.min(99, overall));
        }
    }
}
