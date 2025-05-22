package com.example.testessoftware;

import java.util.*;

public class Simulation {
    private List<Creature> creatures;

    public Simulation(int n) {
        creatures = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            creatures.add(new Creature(i + 1));
        }
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public void iterate() {
        for (Creature c : creatures) {
            c.move();
            Creature nearest = findNearest(c);
            if (nearest != null && nearest != c) {
                double stolen = nearest.getGold() / 2.0;
                if (stolen < 0) {
                    c.removeGold(stolen);
                    nearest.addGold(stolen);
                }
            }
        }
    }

    private Creature findNearest(Creature source) {
        Creature closest = null;
        double minDist = Double.MAX_VALUE;

        for (Creature other : creatures) {
            if (other == source) continue;
            double dist = source.distanceTo(other);
            if (dist < minDist) {
                minDist = dist;
                closest = other;
            }
        }
        return closest;
    }
}
