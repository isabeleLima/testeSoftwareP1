package com.example.testessoftware;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CriaturaTest {
    // ------------------------
    // TESTES DA CLASSE CREATURE
    // ------------------------

    // ---------- DOMÍNIO ----------

   //criar criatura
    @Test
    void testConstructorAndInitialValues() {
        Criatura c = new Criatura(5);
        assertEquals(5, c.getId());
        assertEquals(1_000_000, c.getMoedas());
        assertEquals(0, c.getPosition(), 0.0001);
    }

    //add moedas
    @Test
    void testAddGold() {
        Criatura c = new Criatura(1);
        c.addMoedas(500_000);
        assertEquals(1_500_000, c.getMoedas());
    }

    //remover moedas
    @Test
    void testRemoveGold() {
        Criatura c = new Criatura(1);
        c.removerMoedas(300_000);
        assertEquals(700_000, c.getMoedas());
    }

    //distancia entre criaturas
    @Test
    void testDistanceTo() {
        Criatura a = new Criatura(1);
        Criatura b = new Criatura(2);

        a.setPosicao(3.5);
        b.setPosicao(1.0);

        assertEquals(2.5, a.distanceTo(b), 0.0001);
        assertEquals(2.5, b.distanceTo(a), 0.0001); // simétrico
    }

    // ---------- FRONTEIRA ----------

    //zerar moedas
    @Test
    void testRemoveAllGold() {
        Criatura c = new Criatura(1);
        c.removerMoedas(1_000_000);
        assertEquals(0, c.getMoedas());
    }

    //add 0 moedas
    @Test
    void testAddZeroGold() {
        Criatura c = new Criatura(1);
        c.addMoedas(0);
        assertEquals(1_000_000, c.getMoedas());
    }

    //tentar mover com 0 moedas
    @Test
    void testMoveWithZeroGold() {
        Criatura c = new Criatura(1);
        c.setMoedas(0);
        double posBefore = c.getPosition();
        c.mover();
        assertEquals(posBefore, c.getPosition(), 0.0001, "Com 0 ouro, a posição não muda");
    }

    // ---------- ESTRUTURAL / MC/DC ----------

    //movimento aleatorio
    @Test
    void testMoveRandomlyChangesPosition() {
        Criatura c = new Criatura(1);
        double before = c.getPosition();
        c.mover(); // aleatório
        double after = c.getPosition();

        assertNotEquals(before, after, "A posição deve mudar após o movimento");
        double delta = Math.abs(after - before);
        assertTrue(delta <= c.getMoedas(), "O deslocamento não deve ultrapassar ouro");
    }

    //testar setters
    @Test
    void testSetters() {
        Criatura c = new Criatura(0);

        c.setId(99);
        c.setMoedas(123_456);
        c.setPosicao(7.89);

        assertEquals(99, c.getId());
        assertEquals(123_456, c.getMoedas());
        assertEquals(7.89, c.getPosition(), 0.0001);
    }

    //testar add moedas negativas
    void testCannotAddNegativeGold() {
        Criatura c = new Criatura(1);
        c.addMoedas(-500);
        assertEquals(1_000_000, c.getMoedas(), "Não deve adicionar valores negativos");
    }

    //testar remover valores negativos
    @Test
    void testCannotRemoveNegativeGold() {
        Criatura c = new Criatura(1);
        c.removerMoedas(-200);
        assertEquals(1_000_000, c.getMoedas(), "Não deve remover valores negativos");
    }

    //nao se pode ter moedas negativas
    @Test
    void testCannotHaveNegativeGoldAfterRemoval() {
        Criatura c = new Criatura(1);
        c.removerMoedas(1_500_000);
        assertEquals(0, c.getMoedas(), "Não deve permitir moedas negativas");
    }
}
