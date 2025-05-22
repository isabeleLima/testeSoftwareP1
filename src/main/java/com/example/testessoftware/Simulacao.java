package com.example.testessoftware;

import java.util.*;

public class Simulacao {
    private List<Criatura> criaturas;

    public Simulacao(int n) {
        criaturas = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            criaturas.add(new Criatura(i + 1));
        }
    }

    public List<Criatura> getCreatures() {
        return criaturas;
    }

    public void iteracao() {
        for (Criatura c : criaturas) {
            c.mover();
            Criatura nearest = acheProximo(c);
            if (nearest != null && nearest != c && nearest.getMoedas() > 0) {
                double stolen = nearest.getMoedas() / 2.0;
                if (stolen > 0) {
                    nearest.removerMoedas(stolen);
                    c.addMoedas(stolen);
                }
            }
        }
    }
    private Criatura acheProximo(Criatura source) {
        Criatura closest = null;
        double minDist = Double.MAX_VALUE;

        for (Criatura other : criaturas) {
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
