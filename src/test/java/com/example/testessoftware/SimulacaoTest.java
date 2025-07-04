package com.example.testessoftware;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suíte de testes final e completa para a classe Simulacao.
 * Este arquivo implementa o plano de ação definido na análise, combinando
 * testes de especificação, fronteira, estruturais e de unidade isolados com Mockito.
 */
@ExtendWith(MockitoExtension.class)
public class SimulacaoTest {

  // ===================================================================================
  // Seção 1: Testes de Especificação, Domínio e Fronteira (Caixa-Preta)
  // Estes testes validam os requisitos sem conhecer a implementação interna.
  // ===================================================================================

  /**
   * TIPO: Teste de Especificação / Teste de Domínio
   * REQUISITO: "Criação de Criaturas"
   * OBJETIVO: Garante que o construtor funciona para um número típico de criaturas.
   */
  @Test
  void testConstrutor_Dominio_NumeroPositivoDeCriaturas() {
    Simulacao sim = new Simulacao(5);
    assertEquals(5, sim.getEntidades().size(), "Deve criar 5 criaturas.");
    assertNotNull(sim.getGuardiao(), "Deve criar um guardião.");
    assertEquals(6, sim.getGuardiao().getId(), "ID do guardião deve ser n+1.");
  }

  /**
   * TIPO: Teste de Fronteira
   * REQUISITO: "Criação de Criaturas"
   * OBJETIVO: Testa o caso limite de n=0.
   */
  @Test
  void testConstrutor_Fronteira_ZeroCriaturas() {
    Simulacao sim = new Simulacao(0);
    assertTrue(sim.getEntidades().isEmpty(), "Não deve criar criaturas.");
    assertEquals(1, sim.getGuardiao().getId(), "ID do guardião deve ser 1 para n=0.");
  }

  /**
   * TIPO: Teste de Fronteira
   * REQUISITO: "Criação de Criaturas"
   * OBJETIVO: Testa o caso limite de n=-1 (entrada inválida).
   */
  @Test
  void testConstrutor_Fronteira_NumeroNegativoDeCriaturas() {
    // Garante que o construtor lança uma exceção para entradas inválidas.
    assertThrows(IllegalArgumentException.class, () -> {
      new Simulacao(-1);
    }, "O construtor deve lançar uma exceção para n negativo.");
  }

  /**
   * TIPO: Teste de Especificação / Teste de Unidade com Dublês
   * REQUISITO: "Formação de Cluster"
   * OBJETIVO: Valida que criaturas na mesma posição formam um cluster e somam suas moedas.
   * NOTA: Este teste foi refatorado para usar Stubs e garantir um resultado previsível.
   */
  @Test
  void testClusterFormation_Dominio_DuasCriaturasNaMesmaPosicao() {
    Simulacao sim = new Simulacao(0); // Começa com 0 para ter controle total
    // Usa a criatura previsível para garantir que a posição não mude aleatoriamente
    CriaturaPrevisivel c1 = new CriaturaPrevisivel(1);
    CriaturaPrevisivel c2 = new CriaturaPrevisivel(2);

    // Força a colisão
    c1.setPosicao(10.0);
    c2.setPosicao(10.0);
    c1.setMoedas(100);
    c2.setMoedas(200);

    sim.getEntidades().addAll(Arrays.asList(c1, c2));
    sim.iteracao();

    // Agora as asserções são confiáveis
    assertEquals(1, sim.getEntidades().size(), "As duas criaturas deveriam formar um único cluster.");
    assertTrue(sim.getEntidades().get(0) instanceof Cluster, "A entidade restante deve ser um cluster.");
    // Como não há outros vizinhos para roubar, a soma deve ser exata.
    assertEquals(300, sim.getEntidades().get(0).getMoedas(), "O cluster deve ter a soma exata das moedas dos membros.");
  }

  // ===================================================================================
  // Seção 2: Testes Estruturais (Caixa-Branca)
  // Estes testes olham para a estrutura interna do código para garantir a cobertura lógica.
  // ===================================================================================

  /**
   * TIPO: Teste Estrutural (MC/DC)
   * REQUISITO: "Simulação bem-sucedida"
   * OBJETIVO: Validar todos os caminhos lógicos do método isBemSucedida().
   */
  @Test
  void testIsBemSucedida_Estrutural() {
    // Caso 1: Apenas o guardião (n=0). Deve ser sucesso.
    Simulacao sim1 = new Simulacao(0);
    assertTrue(sim1.isBemSucedida(), "Com 0 criaturas, deve restar apenas o guardião, sendo um sucesso.");

    // Caso 2: Guardião e 1 criatura, guardião mais rico. Deve ser sucesso.
    Simulacao sim2 = new Simulacao(1);
    sim2.getGuardiao().setMoedas(1000);
    sim2.getEntidades().get(0).setMoedas(500);
    assertTrue(sim2.isBemSucedida(), "Guardião mais rico que a única criatura deve ser um sucesso.");

    // Caso 3: Guardião e 1 criatura, guardião mais pobre. Não deve ser sucesso.
    Simulacao sim3 = new Simulacao(1);
    sim3.getGuardiao().setMoedas(300);
    sim3.getEntidades().get(0).setMoedas(500);
    assertFalse(sim3.isBemSucedida(), "Guardião mais pobre que a única criatura não deve ser um sucesso.");

    // Caso 4: Múltiplas criaturas. Não deve ser sucesso.
    Simulacao sim4 = new Simulacao(2);
    assertFalse(sim4.isBemSucedida(), "Com múltiplas criaturas, a simulação não terminou.");
  }

  // ===================================================================================
  // Seção 3: Testes de Unidade Isolados com Dublês (Mocks e Stubs)
  // Estes testes substituem os testes instáveis, garantindo resultados previsíveis.
  // ===================================================================================

  // --- Dublês de Teste (Stubs) para controlar o comportamento ---
  class CriaturaPrevisivel extends Criatura {
    public CriaturaPrevisivel(int id) { super(id); }
    @Override public void mover() { /* Não faz nada para ser previsível */ }
  }

  class GuardiaoPrevisivel extends Guardiao {
    public GuardiaoPrevisivel(int id) { super(id); }
    @Override public void mover() { /* Não faz nada para ser previsível */ }
  }

  class ClusterPrevisivel extends Cluster {
    public ClusterPrevisivel(List<Criatura> membros) { super(membros); }
    @Override public void mover() { /* Não faz nada para ser previsível */ }
  }

  /**
   * TIPO: Teste de Unidade com Dublês (Stubs)
   * REQUISITO: "Guardião elimina cluster"
   * OBJETIVO: Substitui o teste instável 'testGuardiaoEliminaCluster' por uma versão
   * determinística que usa stubs para controlar as posições.
   */
  @Test
  void testGuardiaoEliminaCluster_ComStubs() {
    // 1. Cenário
    Criatura c1 = new Criatura(1);
    c1.setMoedas(800);
    Criatura c2 = new Criatura(2);
    c2.setMoedas(1200);

    ClusterPrevisivel cluster = new ClusterPrevisivel(Arrays.asList(c1, c2));
    cluster.setPosicao(50.0);

    GuardiaoPrevisivel guardiao = new GuardiaoPrevisivel(3);
    guardiao.setMoedas(100);
    guardiao.setPosicao(50.0);

    Simulacao sim = new Simulacao(0);
    sim.getEntidades().add(cluster);
    sim.setGuardiao(guardiao); // Requer um setter em Simulacao.java

    // 2. Ação
    sim.iteracao();

    // 3. Verificação
    assertEquals(2100.0, guardiao.getMoedas(), "Guardião deveria ter absorvido as moedas do cluster.");
    assertTrue(sim.getEntidades().isEmpty(), "O cluster deveria ter sido removido.");
  }

  /**
   * TIPO: Teste de Unidade com Mocks
   * REQUISITO: "Cluster rouba moedas"
   * OBJETIVO: Substitui o teste instável 'testClusterRoubaMetadeDasMoedasDoVizinho' por
   * uma versão que usa Mockito para verificar a interação de "roubo".
   */
  @Test
  void testClusterRoubaMetadeDasMoedasDoVizinho_ComMockito() {
    // 1. Cenário
    Criatura c1 = mock(Criatura.class);
    Criatura c2 = mock(Criatura.class);
    Criatura vizinhoMock = mock(Criatura.class);

    when(c1.getPosicao()).thenReturn(10.0);
    when(c1.getMoedas()).thenReturn(100.0);
    when(c2.getPosicao()).thenReturn(10.0);
    when(c2.getMoedas()).thenReturn(200.0);
    when(vizinhoMock.getPosicao()).thenReturn(20.0);
    when(vizinhoMock.getMoedas()).thenReturn(500.0);

    Simulacao sim = new Simulacao(0);
    sim.getEntidades().addAll(Arrays.asList(c1, c2, vizinhoMock));

    // 2. Ação
    sim.iteracao();

    // 3. Verificação
    // Verificamos se a interação correta (o roubo) aconteceu.
    verify(vizinhoMock, times(1)).removerMoedas(250.0);
  }
}
