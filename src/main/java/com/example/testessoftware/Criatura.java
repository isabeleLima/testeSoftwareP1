package com.example.testessoftware;

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

    public void addMoedas(double amount) {
        if (amount < 0) return; // impede adição negativa
        this.moedas += amount;
    }

    public void removerMoedas(double amount) {
        if (amount < 0) return; // impede subtração negativa
        this.moedas = Math.max(0, this.moedas - amount); // garante no mínimo 0
    }

    public void mover() {
        double r = (Math.random() * 2 - 1); // Aleatório entre -1 e 1
        posicao += r * moedas;
    }

    public double distanceTo(Criatura other) {
        return Math.abs(this.posicao - other.posicao);
    }






}
