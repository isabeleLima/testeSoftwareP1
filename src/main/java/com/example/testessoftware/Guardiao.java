    package com.example.testessoftware;

    public class Guardiao extends Criatura {
        public Guardiao(int id) {
            super(id);
            this.moedas = 1;
        }

        @Override
        public void mover() {
            double r = (Math.random() * 2 - 1);
            this.posicao += r * this.moedas;
        }
    }
