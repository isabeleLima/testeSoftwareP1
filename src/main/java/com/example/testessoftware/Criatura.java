package com.example.testessoftware;

/**
 * Representa uma entidade no simulador com posição e moedas.
 *
 * Invariantes de Classe:
 * - A quantidade de 'moedas' nunca deve ser negativa.
 * - O 'id' da criatura, uma vez definido, não deve ser alterado externamente.
 */
public class Criatura {
    protected int id;
    protected double posicao;
    protected double moedas;

    public Criatura(int id) {
        this.id = id;
        this.moedas = 1_000_000;
        this.posicao = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPosicao() {
        return posicao;
    }

    public void setPosicao(double position) {
        this.posicao = position;
    }

    public double getMoedas() {
        return moedas;
    }

    public void setMoedas(double moedas) {
        this.moedas = moedas;
    }

    /**
     * Adiciona uma quantidade de moedas ao total da criatura.
     *
     * @param amount A quantidade de moedas a ser adicionada.
     * Pré-condição: O valor de 'amount' não deve ser negativo.
     * Pós-condição: this.moedas = moedas_anteriores + amount.
     */
    public void addMoedas(double amount) {
        if (amount < 0) return; // Garante a pré-condição
        this.moedas += amount;
    }

    /**
     * Remove uma quantidade de moedas do total da criatura.
     *
     * @param amount A quantidade de moedas a ser removida.
     * Pré-condição: O valor de 'amount' não deve ser negativo.
     * Pós-condição: A quantidade de moedas nunca será menor que zero.
     * this.moedas = max(0, moedas_anteriores - amount).
     */
    public void removerMoedas(double amount) {
        if (amount < 0) return; // Garante a pré-condição
        this.moedas = Math.max(0, this.moedas - amount); // Garante a invariante
    }

    public void mover() {
        double r = (Math.random() * 2 - 1); // Aleatório entre -1 e 1
        posicao += r * moedas;
    }

    public double distanceTo(Criatura other) {
        return Math.abs(this.posicao - other.posicao);
    }






}
