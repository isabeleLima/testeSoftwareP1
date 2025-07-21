package com.example.testessoftware;

public class Guardiao extends Criatura {
    public Guardiao(int id) {
        super(id);
        // O valor inicial das moedas do guardião pode ser 0 ou 1, conforme a especificação.
        // Vamos manter em 1 para consistência.
        this.moedas = 1;
    }

    /**
     * O movimento do Guardião foi ajustado para ter uma base de movimento
     * significativa, tornando-o visível na simulação desde o início.
     * Ele usa um valor fixo (semelhante ao das criaturas) mais um bónus
     * das suas próprias moedas.
     */
    @Override
    public void mover() {
        double r = (Math.random() * 2 - 1); // Fator aleatório entre -1 e 1

        // IMPULSO DE MOVIMENTO: Usamos uma base alta para que o movimento seja visível.
        double impulsoBase = 500_000;

        // O movimento final é o impulso base + um bónus das moedas que ele já recolheu.
        this.posicao += r * (impulsoBase + this.moedas);
    }
}