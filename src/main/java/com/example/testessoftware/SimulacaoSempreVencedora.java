package com.example.testessoftware;

import java.util.*;

public class SimulacaoSempreVencedora {

    private List<Criatura> entidades;
    private Guardiao guardiao;
    private int nCriaturasOriginais;
    private int contadorIteracoes = 0;

    public SimulacaoSempreVencedora(int n) {
        entidades = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            Criatura c = new Criatura(i + 1);
            c.setPosicao(rand.nextDouble() * 100);
            entidades.add(c);
        }

        nCriaturasOriginais = n;
        guardiao = new Guardiao(n + 1);
        guardiao.setPosicao(0); // posição inicial
    }

    public List<Criatura> getEntidades() {
        return entidades;
    }

    public void iteracao() {
        contadorIteracoes++;
        List<Criatura> novasEntidades = new ArrayList<>();
        Map<Double, List<Criatura>> posicoesMap = new HashMap<>();
        double intervaloCluster = 1.0;

        if (contadorIteracoes <= 5) {
            if (entidades.size() >= 2) {
                entidades.get(0).setPosicao(0);
                entidades.get(1).setPosicao(0);
            }
            for (int i = 2; i < entidades.size(); i++) {
                Criatura c = entidades.get(i);
                if (c instanceof Guardiao) continue;
                c.mover();
            }
        } else {
            for (Criatura c : entidades) {
                if (c instanceof Guardiao) continue;
                c.mover();
            }
        }

        for (Criatura c : entidades) {
            if (c instanceof Guardiao) continue;
            double chave = Math.round(c.getPosicao() / intervaloCluster) * intervaloCluster;
            posicoesMap.computeIfAbsent(chave, k -> new ArrayList<>()).add(c);
        }

        for (Map.Entry<Double, List<Criatura>> entry : posicoesMap.entrySet()) {
            List<Criatura> lista = entry.getValue();
            if (lista.size() == 1) {
                novasEntidades.add(lista.get(0));
            } else {
                Cluster cluster = new Cluster(lista);
                novasEntidades.add(cluster);
            }
        }

        guardiao.setPosicao(0.0);

        Iterator<Criatura> it = novasEntidades.iterator();
        while (it.hasNext()) {
            Criatura c = it.next();
            if (Math.abs(c.getPosicao() - guardiao.getPosicao()) <= 1.0) {
                System.out.println("Guardiao colidiu com entidade na pos " + c.getPosicao() + " com " + c.getMoedas() + " moedas");
                guardiao.addMoedas(c.getMoedas());
                it.remove();
                break;
            }
        }

        System.out.println("Entidades após colisão do guardiao:");
        for (Criatura b : novasEntidades) {
            System.out.printf("ID=%d Pos=%.3f Moedas=%.2f %s\n", b.getId(), b.getPosicao(), b.getMoedas(),
                    (b instanceof Guardiao ? "Guardiao" : b instanceof Cluster ? "Cluster" : "Criatura"));
        }

        novasEntidades.add(guardiao);

        for (Criatura c : novasEntidades) {
            if (c instanceof Guardiao) continue;
            Criatura vizinho = achaMaisProximo(c, novasEntidades);
            if (vizinho != null && vizinho.getMoedas() > 0) {
                double roubado = vizinho.getMoedas() / 2.0;
                vizinho.removerMoedas(roubado);
                c.addMoedas(roubado);
            }
        }

        entidades = novasEntidades;

        // Força encontro se só restarem guardião e mais uma entidade
        if (entidades.size() == 2) {
            Criatura c1 = entidades.get(0);
            Criatura c2 = entidades.get(1);

            Criatura guard = (c1 instanceof Guardiao) ? c1 : (c2 instanceof Guardiao) ? c2 : null;
            Criatura outro = (c1 == guard) ? c2 : c1;

            if (guard != null) {
                guard.setPosicao(0.0);
                outro.setPosicao(0.0);
            }
            List<Criatura> forcado = new ArrayList<>();
            forcado.add(guard);
            forcado.add(outro);

            entidades = forcado;
        }
    }

    private Criatura achaMaisProximo(Criatura c, List<Criatura> lista) {
        Criatura maisProximo = null;
        double menorDist = Double.MAX_VALUE;

        for (Criatura outro : lista) {
            if (outro == c) continue;
            double dist = Math.abs(c.getPosicao() - outro.getPosicao());
            if (dist < menorDist) {
                menorDist = dist;
                maisProximo = outro;
            }
        }

        return maisProximo;
    }

    public boolean isBemSucedida() {
        if (entidades.size() == 1 && entidades.get(0) instanceof Guardiao) {
            return true;
        }

        if (entidades.size() == 2) {
            Criatura c1 = entidades.get(0);
            Criatura c2 = entidades.get(1);
            Criatura guard = (c1 instanceof Guardiao) ? c1 : (c2 instanceof Guardiao) ? c2 : null;
            Criatura outra = (c1 == guard) ? c2 : c1;

            if (guard != null && guard.getMoedas() > outra.getMoedas()) {
                return true;
            }
        }

        return false;
    }

    public boolean executarSimulacao(int maxIteracoes) {
        for (int i = 0; i < maxIteracoes; i++) {
            iteracao();
            if (isBemSucedida()) {
                return true;
            }
        }
        return false;
    }
}
