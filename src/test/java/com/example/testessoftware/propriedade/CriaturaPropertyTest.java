package com.example.testessoftware.propriedade;

import com.example.testessoftware.Criatura;
import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TIPO: Teste Baseado em Propriedade.
 * OBJETIVO: Verificar as invariantes e lógicas da classe Criatura
 * para uma grande variedade de entradas aleatórias.
 */
class CriaturaPropertyTest {

    /**
     * Propriedade: Remover qualquer quantidade de moedas nunca resulta em um saldo negativo.
     */
    @Property
    void testRemoverMoedasNuncaDeixaSaldoNegativo(@ForAll("positiveDoubles") double moedasIniciais, @ForAll("positiveDoubles") double valorRemover) {
        // Arrange
        Criatura c = new Criatura(1);
        c.setMoedas(moedasIniciais);

        // Act
        c.removerMoedas(valorRemover);

        // Assert
        assertTrue(c.getMoedas() >= 0, "O saldo nunca deve ser negativo após a remoção.");
    }

    /**
     * Propriedade: Adicionar um valor não negativo sempre aumenta o saldo corretamente.
     */
    @Property
    void testAdicionarMoedasSempreAumentaSaldo(@ForAll("positiveDoubles") double moedasIniciais, @ForAll("positiveDoubles") double valorAdicionar) {
        // Arrange
        Criatura c = new Criatura(1);
        c.setMoedas(moedasIniciais);
        double saldoEsperado = moedasIniciais + valorAdicionar;

        // Act
        c.addMoedas(valorAdicionar);

        // Assert
        assertEquals(saldoEsperado, c.getMoedas(), 0.001);
    }

    // Fornecedor de dados para números de ponto flutuante não negativos
    @Provide
    Arbitrary<Double> positiveDoubles() {
        return Arbitraries.doubles().greaterOrEqual(0);
    }
}