package com.example.testessoftware;//package com.example.testessoftware;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.util.List;

public class SimulacaoTest {

  // ------------------------
  // TESTES DA CLASSE SIMULACAO
  // ------------------------

  // ---------- DOMÍNIO ----------

  //n=5 criar 5 criaturas com ids corretos e 1000000 moedas
  @Test
  void testConstructor() {
    int n = 5;
    Simulacao sim = new Simulacao(n);
    List<Criatura> list = sim.getCreatures();
    assertEquals(n, list.size());

    for (int i = 0; i < n; i++) {
      Criatura c = list.get(i);
      assertEquals(i + 1, c.getId());
      assertEquals(1_000_000, c.getMoedas());
      assertEquals(0, c.getPosicao(), 0.0001);
    }
  }

  //n=2 sem movimento 1 rouba metade de 2 e 2 rouba metade de 1
  @Test
  void testIteracaoTwoCreaturesStealBackAndForth() {
    class FixedCreature extends Criatura {
      public FixedCreature(int id) { super(id); }
      @Override public void mover() {}
    }

    Simulacao sim = new Simulacao(0);

    FixedCreature c1 = new FixedCreature(1);
    FixedCreature c2 = new FixedCreature(2);

    c1.setPosicao(0);
    c2.setPosicao(10);
    c1.setMoedas(100);
    c2.setMoedas(200);

    sim.getCreatures().clear();
    sim.getCreatures().add(c1);
    sim.getCreatures().add(c2);

    sim.iteracao();

    assertEquals(100, c1.getMoedas(), 0.0001);
    assertEquals(200, c2.getMoedas(), 0.0001);
  }

  // ---------- FRONTEIRA ----------

  //n=1 nada acontece apos iteracao
  @Test
  void testIteracaoSingleCreatureDoesNothing() {
    Simulacao sim = new Simulacao(1);
    Criatura only = sim.getCreatures().get(0);
    double goldBefore = only.getMoedas();

    sim.iteracao();

    assertEquals(goldBefore, only.getMoedas(), 0.0001);
  }

 //distancias iguais escolhe o primeiro da lista
  @Test
  void testAcheProximoWithEqualDistances() throws Exception {
    Simulacao sim = new Simulacao(0);
    Criatura c1 = new Criatura(1);
    Criatura c2 = new Criatura(2);
    Criatura c3 = new Criatura(3);

    c1.setPosicao(0);
    c2.setPosicao(10);
    c3.setPosicao(20);

    sim.getCreatures().clear();
    sim.getCreatures().add(c1);
    sim.getCreatures().add(c2);
    sim.getCreatures().add(c3);

    Method m = Simulacao.class.getDeclaredMethod("acheProximo", Criatura.class);
    m.setAccessible(true);
    Criatura nearest = (Criatura) m.invoke(sim, c2);

    assertSame(c1, nearest);
  }

  // ---------- ESTRUTURAL / MC/DC ----------

  //quem tem 0 moedas pode roubar mas n pode ser roubado
  @Test
  void testIteracaoNoStealWhenNeighborHasZeroGold() {
    class FixedCreature extends Criatura {
      public FixedCreature(int id) { super(id); }
      @Override public void mover() {}
    }

    Simulacao sim = new Simulacao(0);
    FixedCreature c1 = new FixedCreature(1);
    FixedCreature c2 = new FixedCreature(2);

    c1.setPosicao(0);
    c2.setPosicao(1);

    c1.setMoedas(1_000_000);
    c2.setMoedas(0);

    sim.getCreatures().add(c1);
    sim.getCreatures().add(c2);

    sim.iteracao();

    // c1 não deve conseguir roubar nada de c2 mas c2 rouba de c1
    assertEquals(500_000, c1.getMoedas(), 0.0001);  // c1 perdeu metade das moedas
    assertEquals(500_000, c2.getMoedas(), 0.0001);  // c2 ganhou metade das moedas
  }

  //se a criatura n acha nada
  @Test
  void testIteracaoNoNeighborFoundDoesNothing() {
    class FixedCreature extends Criatura {
      public FixedCreature(int id) { super(id); }
      @Override public void mover() {}
    }

    Simulacao sim = new Simulacao(0);
    FixedCreature c = new FixedCreature(1);
    c.setPosicao(0);
    sim.getCreatures().add(c);

    sim.iteracao(); // Só tem 1 criatura, logo não há vizinho

    assertEquals(1_000_000, c.getMoedas());
  }

  // testa se o cod retorna a propria criatura
  @Test
  void testIteracaoSkipsIfNearestIsSameCreature() {
    class FixedCreature extends Criatura {
      public FixedCreature(int id) { super(id); }
      @Override public void mover() {}
    }

    Simulacao sim = new Simulacao(0);
    FixedCreature c = new FixedCreature(1);
    c.setPosicao(0);

    sim.getCreatures().add(c);

    sim.iteracao();

    assertEquals(1_000_000, c.getMoedas());
  }

  //se a quantidade roubada for == 0 mesmo que o vizinho tenha moedas
  @Test
  void testIteracaoWithTinyAmountOfGold() {
    class FixedCreature extends Criatura {
      public FixedCreature(int id) { super(id); }
      @Override public void mover() {}
    }

    Simulacao sim = new Simulacao(0);
    FixedCreature c1 = new FixedCreature(1);
    FixedCreature c2 = new FixedCreature(2);

    c1.setPosicao(0);
    c2.setPosicao(1);
    c1.setMoedas(0.001);
    c2.setMoedas(1_000_000);

    sim.getCreatures().add(c1);
    sim.getCreatures().add(c2);

    sim.iteracao();

    // Garante que houve transferência mesmo com valor pequeno
    assertTrue(c1.getMoedas() > 0.001, "c1 deve ter roubado algo de c2");
    assertTrue(c2.getMoedas() < 1_000_000);
  }


}
