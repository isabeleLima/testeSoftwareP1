package com.example.testessoftware;

import java.util.*;

public class Simulacao {

    private List<Criatura> entidades;
    private Guardiao guardiao;

    public void setGuardiao(Guardiao guardiao) {
        this.guardiao = guardiao;
    }

    public Simulacao(int n) {
        if (n < 0) throw new IllegalArgumentException("O número de criaturas não pode ser negativo.");
        entidades = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            entidades.add(new Criatura(i + 1));
        }
        this.guardiao = new Guardiao(n + 1);
    }

    // Método principal com a lógica corrigida e simplificada
    public void iteracao() {
        // 1. Mover todas as criaturas (exceto o guardião, que se move depois)
        for (Criatura c : entidades) {
            c.mover();
        }

        // 2. Identificar criaturas na mesma posição para formar clusters
        Map<Double, List<Criatura>> posicoesOcupadas = new HashMap<>();
        for (Criatura c : entidades) {
            // Arredondamos a posição para agrupar criaturas próximas
            double chavePosicao = Math.round(c.getPosicao());
            posicoesOcupadas.computeIfAbsent(chavePosicao, k -> new ArrayList<>()).add(c);
        }

        // 3. Criar a nova lista de entidades com os clusters
        List<Criatura> novasEntidades = new ArrayList<>();
        for (List<Criatura> ocupantes : posicoesOcupadas.values()) {
            if (ocupantes.size() > 1) {
                novasEntidades.add(new Cluster(ocupantes)); // Adiciona um novo cluster
            } else {
                novasEntidades.add(ocupantes.get(0)); // Adiciona a criatura única
            }
        }

        // 4. Lógica de roubo: Cada entidade rouba da mais próxima
        // Esta é a seção crucial que o teste Mockito verifica
        List<Criatura> entidadesAposRoubo = new ArrayList<>(novasEntidades);
        for (Criatura entidadeAtual : novasEntidades) {
            Criatura vizinho = acheProximo(entidadeAtual, entidadesAposRoubo);
            if (vizinho != null && vizinho.getMoedas() > 0) {
                double valorRoubo = vizinho.getMoedas() / 2.0;
                vizinho.removerMoedas(valorRoubo);
                entidadeAtual.addMoedas(valorRoubo);
            }
        }

        this.entidades = entidadesAposRoubo;

        // 5. Processar o Guardião
        guardiao.mover();

        // Verifica se a nova posição do guardião está ocupada por um cluster
        Iterator<Criatura> it = this.entidades.iterator();
        while (it.hasNext()) {
            Criatura c = it.next();
            if (c instanceof Cluster && Math.round(c.getPosicao()) == Math.round(guardiao.getPosicao())) {
                guardiao.addMoedas(c.getMoedas()); // Guardião absorve as moedas
                it.remove(); // Cluster é eliminado
            }
        }
    }

    // Função auxiliar para encontrar a criatura mais próxima
    private Criatura acheProximo(Criatura atual, List<Criatura> todos) {
        Criatura maisProximo = null;
        double menorDistancia = Double.MAX_VALUE;

        for (Criatura outro : todos) {
            if (outro.equals(atual)) { // Usar .equals() é mais seguro que '=='
                continue;
            }
            double d = atual.distanceTo(outro);
            if (d < menorDistancia) {
                menorDistancia = d;
                maisProximo = outro;
            }
        }
        return maisProximo;
    }

    // Getters para testes e visualização
    public List<Criatura> getEntidades() { return entidades; }
    public Guardiao getGuardiao() { return guardiao; }

    // Método de condição de sucesso (permanece o mesmo)
    public boolean isBemSucedida() {
        if (entidades.isEmpty()) {
            return true;
        }
        if (entidades.size() == 1) {
            return guardiao.getMoedas() > entidades.get(0).getMoedas();
        }
        return false;
    }
}