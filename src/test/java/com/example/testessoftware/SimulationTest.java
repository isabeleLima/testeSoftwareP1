package com.example.testessoftware;//package com.example.testessoftware;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.util.List;

public class SimulationTest {

  // ------------------------
  // TESTES DA CLASSE CREATURE
  // ------------------------

  // ---------- DOMÍNIO ----------

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

  // ---------- FRONTEIRA ----------

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

  // ---------- ESTRUTURAL / MC/DC ----------

  /**
   * Testa o movimento aleatório de uma criatura.
   */
  @Test
  void testMoveRandomlyChangesPosition() {
    Creature c = new Creature(1);
    double before = c.getPosition();
    c.move(); // aleatório
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

  // ------------------------
  // TESTES DA CLASSE SIMULATION
  // ------------------------

  // ---------- DOMÍNIO ----------

  /**
   * Domínio: construtor com n=5 deve criar 5 criaturas
   * com IDs 1..5 e ouro=1_000_000.
   */
  @Test
  void testConstructorCreatesNCreatures() {
    int n = 5;
    Simulation sim = new Simulation(n);
    List<Creature> list = sim.getCreatures();
    assertEquals(n, list.size());

    for (int i = 0; i < n; i++) {
      Creature c = list.get(i);
      assertEquals(i + 1, c.getId());
      assertEquals(1_000_000, c.getGold());
      assertEquals(0, c.getPosition(), 0.0001);
    }
  }

  /**
   * Domínio: n=2, sem movimento, garante
   * 1 rouba metade de 2, depois 2 rouba metade de 1.
   */
  @Test
  void testIterateTwoCreaturesStealBackAndForth() {
    class FixedCreature extends Creature {
      public FixedCreature(int id) { super(id); }
      @Override public void move() {}
    }

    Simulation sim = new Simulation(0);
    FixedCreature c1 = new FixedCreature(1);
    FixedCreature c2 = new FixedCreature(2);
    c1.setPosition(0);
    c2.setPosition(10);
    c1.setGold(100);
    c2.setGold(200);

    sim.getCreatures().clear();
    sim.getCreatures().add(c1);
    sim.getCreatures().add(c2);

    sim.iterate();

    assertEquals(100, c1.getGold(), 0.0001);
    assertEquals(200, c2.getGold(), 0.0001);
  }

  // ---------- FRONTEIRA ----------

  /**
   * Fronteira: n=1, nenhuma mudança após iterate().
   */
  @Test
  void testIterateSingleCreatureDoesNothing() {
    Simulation sim = new Simulation(1);
    Creature only = sim.getCreatures().get(0);
    double goldBefore = only.getGold();

    sim.iterate();

    assertEquals(goldBefore, only.getGold(), 0.0001);
  }

  /**
   * Fronteira estrutural: distâncias iguais, deve escolher o primeiro na lista.
   */
  @Test
  void testFindNearestWithEqualDistances() throws Exception {
    Simulation sim = new Simulation(0);
    Creature c1 = new Creature(1);
    Creature c2 = new Creature(2);
    Creature c3 = new Creature(3);

    c1.setPosition(0);
    c2.setPosition(10);
    c3.setPosition(20);

    sim.getCreatures().clear();
    sim.getCreatures().add(c1);
    sim.getCreatures().add(c2);
    sim.getCreatures().add(c3);

    Method m = Simulation.class.getDeclaredMethod("findNearest", Creature.class);
    m.setAccessible(true);
    Creature nearest = (Creature) m.invoke(sim, c2);

    assertSame(c1, nearest);
  }

  // ---------- ESTRUTURAL / MC/DC ----------

  /**
   * Cobertura estrutural: roubo não ocorre se o vizinho não tem ouro.
   */
  @Test
  void testIterateNoStealWhenNeighborHasZeroGold() {
    class FixedCreature extends Creature {
      public FixedCreature(int id) { super(id); }
      @Override public void move() {} // evita movimento aleatório
    }

    Simulation sim = new Simulation(0);
    FixedCreature c1 = new FixedCreature(1);
    FixedCreature c2 = new FixedCreature(2);

    // Configura posições e ouro
    c1.setPosition(0);
    c2.setPosition(1); // suficientemente perto para ser considerado vizinho

    c1.setGold(1_000_000);
    c2.setGold(0); // c2 sem ouro

    sim.getCreatures().add(c1);
    sim.getCreatures().add(c2);

    sim.iterate();

    // c1 não deve conseguir roubar nada de c2
    assertEquals(1_000_000, c1.getGold(), 0.0001);
    assertEquals(0, c2.getGold(), 0.0001);
  }

}
