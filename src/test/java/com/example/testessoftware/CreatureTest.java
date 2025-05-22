package com.example.testessoftware;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreatureTest {

    /**
     * Testa se a criatura é criada com o ID correto e ouro inicial padrão (1 milhão).
     */
    @Test
    void testConstructorAndInitialValues() {
        Creature c = new Creature(5);
        assertEquals(5, c.getId());
        assertEquals(1_000_000, c.getGold());
        assertEquals(0, c.getPosition(), 0.0001);
    }

    /**
     * Teste simples de adição de ouro.
     */
    @Test
    void testAddGold() {
        Creature c = new Creature(1);
        c.addGold(500_000);
        assertEquals(1_500_000, c.getGold());
    }

    /**
     * Teste simples de remoção de ouro.
     */
    @Test
    void testRemoveGold() {
        Creature c = new Creature(1);
        c.removeGold(300_000);
        assertEquals(700_000, c.getGold());
    }

    /**
     * Testa a distância absoluta entre duas criaturas.
     */
    @Test
    void testDistanceTo() {
        Creature a = new Creature(1);
        Creature b = new Creature(2);
        a.setPosition(3.5);
        b.setPosition(1.0);
        assertEquals(2.5, a.distanceTo(b), 0.0001);
        assertEquals(2.5, b.distanceTo(a), 0.0001); // simétrico
    }

    /**
     * Testa o movimento aleatório de uma criatura.
     * Como é aleatório, testamos apenas se o valor está dentro de um intervalo esperado.
     */
    @Test
    void testMoveRandomlyChangesPosition() {
        Creature c = new Creature(1);
        double before = c.getPosition();
        c.move(); // aleatório

        // A posição deve ter mudado, com um delta proporcional ao ouro
        double after = c.getPosition();
        assertNotEquals(before, after, "A posição deve mudar após o movimento");

        double delta = Math.abs(after - before);
        assertTrue(delta <= c.getGold(), "O deslocamento não deve ultrapassar ouro");
    }

    /**
     * Teste MC/DC: garante que setters funcionam corretamente.
     */
    @Test
    void testSetters() {
        Creature c = new Creature(0);
        c.setId(99);
        c.setGold(123_456);
        c.setPosition(7.89);

        assertEquals(99, c.getId());
        assertEquals(123_456, c.getGold());
        assertEquals(7.89, c.getPosition(), 0.0001);
    }

    /**
     * Teste de fronteira: remover todo o ouro.
     */
    @Test
    void testRemoveAllGold() {
        Creature c = new Creature(1);
        c.removeGold(1_000_000);
        assertEquals(0, c.getGold());
    }

    /**
     * Teste de fronteira: adicionar 0 ouro.
     */
    @Test
    void testAddZeroGold() {
        Creature c = new Creature(1);
        c.addGold(0);
        assertEquals(1_000_000, c.getGold());
    }

    /**
     * Teste de fronteira: mover com 0 ouro (posição não deve mudar).
     */
    @Test
    void testMoveWithZeroGold() {
        Creature c = new Creature(1);
        c.setGold(0);
        double posBefore = c.getPosition();
        c.move();
        assertEquals(posBefore, c.getPosition(), 0.0001, "Com 0 ouro, a posição não muda");
    }
}
