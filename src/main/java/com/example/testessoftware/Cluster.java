package com.example.testessoftware;

import java.util.ArrayList;
import java.util.List;

public class Cluster extends Criatura {
    private List<Criatura> membros = new ArrayList<>();

    public Cluster(List<Criatura> membros) {
        super(-1);
        this.membros.addAll(membros);
        this.moedas = 0;
        this.posicao = membros.get(0).getPosicao(); // posição do cluster = posição do primeiro membro
        for (Criatura c : membros) {
            this.moedas += c.getMoedas();
        }
    }

    public void adicionarMembro(Criatura c) {
        this.membros.add(c);
        this.moedas += c.getMoedas();
    }

    public List<Criatura> getMembros() {
        return membros;
    }
}
