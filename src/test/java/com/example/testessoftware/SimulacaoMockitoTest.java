package com.example.testessoftware;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SimulacaoMockitoTest {

    //region ========= Dublês de Teste (Stubs) =========
    /**
     * Stub para o Guardião, garantindo que seu movimento seja previsível.
     */
    class GuardiaoPrevisivel extends Guardiao {
        public GuardiaoPrevisivel(int id) { super(id); }
        @Override
        public void mover() { /* Intencionalmente vazio para remover aleatoriedade */ }
    }

    class ClusterPrevisivel extends Cluster {
        public ClusterPrevisivel(List<Criatura> membros) { super(membros); }
        @Override
        public void mover() { /* Intencionalmente vazio para garantir que a posição não mude */ }
    }

    @Test
    void testGuardiaoEliminaCluster_ComMockito() {
        // 1. Cenário (Arrange)
        // Mocks para as criaturas que formarão o cluster.
        Criatura c1Mock = mock(Criatura.class);
        Criatura c2Mock = mock(Criatura.class);
        when(c1Mock.getMoedas()).thenReturn(800.0);
        when(c2Mock.getMoedas()).thenReturn(1200.0);

        // **A SOLUÇÃO (PARTE 1):** Usamos nosso Cluster com movimento previsível.
        ClusterPrevisivel clusterPrevisivel = new ClusterPrevisivel(Arrays.asList(c1Mock, c2Mock));
        clusterPrevisivel.setPosicao(50.0); // Posição fixa

        // **A SOLUÇÃO (PARTE 2):** Usamos nosso Guardião com movimento previsível.
        GuardiaoPrevisivel guardiaoPrevisivel = new GuardiaoPrevisivel(3);
        guardiaoPrevisivel.setMoedas(100.0);
        guardiaoPrevisivel.setPosicao(50.0); // Posição fixa

        // Preparamos a simulação com nossos dublês previsíveis.
        Simulacao simulacao = new Simulacao(0);
        simulacao.getEntidades().add(clusterPrevisivel);
        simulacao.setGuardiao(guardiaoPrevisivel);

        // 2. Ação (Act)
        simulacao.iteracao();

        // 3. Verificação (Assert)
        assertEquals(2100.0, guardiaoPrevisivel.getMoedas(), "Guardião deveria ter absorvido as moedas do cluster.");
        assertTrue(simulacao.getEntidades().isEmpty(), "O cluster deveria ter sido removido.");
    }

    @Test
    void testClusterRoubaMetadeDasMoedasDoVizinho_ComMockito() {
        // Este teste não precisava de alterações, mas o mantemos para a suíte completa.
        Criatura c1 = mock(Criatura.class);
        Criatura c2 = mock(Criatura.class);
        Criatura vizinhoMock = mock(Criatura.class);

        when(c1.getPosicao()).thenReturn(10.0);
        when(c1.getMoedas()).thenReturn(100.0);
        when(c2.getPosicao()).thenReturn(10.0);
        when(c2.getMoedas()).thenReturn(200.0);
        when(vizinhoMock.getPosicao()).thenReturn(20.0);
        when(vizinhoMock.getMoedas()).thenReturn(500.0);

        Simulacao simulacao = new Simulacao(0);
        List<Criatura> listaDeMocks = Arrays.asList(c1, c2, vizinhoMock);
        simulacao.getEntidades().addAll(listaDeMocks);

        simulacao.iteracao();

        verify(vizinhoMock, times(1)).removerMoedas(250.0);
    }
}