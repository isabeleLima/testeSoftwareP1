package com.example.testessoftware.integracao;

import com.example.testessoftware.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suíte de Testes de Integração para GerenciadorUsuarios.
 * Foca na interação com o DatabaseManager e o banco de dados SQLite real.
 * Rastreabilidade: Requisitos "Funcionalidades de Usuários" e "Estatísticas".
 */
public class GerenciadorUsuariosIntegrationTest {

    private GerenciadorUsuarios gerenciador;
    private Connection conn;

    @BeforeEach
    void setUp() throws SQLException {
        // Usa um banco de dados em memória para isolamento e velocidade
        conn = DatabaseManager.connect();
        DatabaseManager.initializeDatabase(); // Garante que o schema seja criado
        gerenciador = new GerenciadorUsuarios();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpa o banco de dados após cada teste para garantir isolamento
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM usuarios");
            stmt.execute("UPDATE estatisticas_globais SET total_simulacoes = 0, total_sucessos = 0 WHERE id = 1");
        }
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * TIPO: Teste de Integração (Caminho Feliz)
     * OBJETIVO: Testa o fluxo completo de adicionar um usuário e depois autenticá-lo.
     */
    @Test
    void testAdicionarEAutenticarUsuario_Dominio() {
        // Arrange & Act
        boolean adicionado = gerenciador.adicionarUsuario("teste", "senha123", "avatar.png");
        Usuario autenticado = gerenciador.autenticar("teste", "senha123");

        // Assert
        assertTrue(adicionado, "O usuário deveria ter sido adicionado com sucesso.");
        assertNotNull(autenticado, "A autenticação deveria ter retornado um objeto Usuario.");
        assertEquals("teste", autenticado.getLogin(), "O login do usuário autenticado está incorreto.");
    }

    /**
     * TIPO: Teste de Integração (Fronteira/Erro)
     * OBJETIVO: Garante que o banco de dados impõe a restrição de chave primária.
     */
    @Test
    void testAdicionarUsuarioDuplicado_Fronteira() {
        // Arrange
        gerenciador.adicionarUsuario("duplicado", "senha1", "avatar1.png");

        // Act & Assert
        boolean adicionadoNovamente = gerenciador.adicionarUsuario("duplicado", "senha2", "avatar2.png");
        assertFalse(adicionadoNovamente, "Não deveria ser possível adicionar um usuário com login duplicado.");
    }

    /**
     * TIPO: Teste de Integração (Transacional)
     * OBJETIVO: Testa a remoção de um usuário e a atualização das estatísticas globais.
     */
    @Test
    void testRemoverUsuario_EstatísticasGlobaisAtualizadas() {
        // Arrange
        gerenciador.adicionarUsuario("user_para_remover", "123", "a.png");
        Usuario u = gerenciador.autenticar("user_para_remover", "123");
        u.incrementarTotal();
        u.incrementarSucesso();
        gerenciador.registrarResultadoSimulacao(u, true); // 1 simulação, 1 sucesso

        // Act
        boolean removido = gerenciador.removerUsuario("user_para_remover");

        // Assert
        assertTrue(removido, "O usuário deveria ter sido removido.");
        assertNull(gerenciador.autenticar("user_para_remover", "123"), "Usuário removido não deveria autenticar.");

        // Verifica se as estatísticas globais foram decrementadas
        EstatisticasService service = new EstatisticasService(gerenciador);
        EstatisticasSimulacao stats = service.getEstatisticasAtuais();
        assertEquals(0, stats.getTotalSimulacoesGlobais(), "Total de simulações globais deveria ser 0 após a remoção.");
        assertEquals(0, stats.getTotalSucessosGlobais(), "Total de sucessos globais deveria ser 0 após a remoção.");
    }

    /**
     * TIPO: Teste de Integração (Transacional)
     * OBJETIVO: Garante que as estatísticas do usuário e globais são atualizadas
     * corretamente numa transação.
     */
    @Test
    void testRegistrarResultadoSimulacao() {
        // Arrange
        gerenciador.adicionarUsuario("jogador1", "senha", "b.png");
        Usuario u = gerenciador.autenticar("jogador1", "senha");

        // Act: Simula uma execução bem-sucedida
        u.incrementarTotal();
        u.incrementarSucesso();
        gerenciador.registrarResultadoSimulacao(u, true);

        // Assert
        Usuario usuarioAtualizado = gerenciador.autenticar("jogador1", "senha");
        assertEquals(1, usuarioAtualizado.getTotalSimulacoes(), "O total de simulações do usuário deveria ser 1.");
        assertEquals(1, usuarioAtualizado.getSimulacoesBemSucedidas(), "As simulações de sucesso do usuário deveriam ser 1.");

        EstatisticasService service = new EstatisticasService(gerenciador);
        EstatisticasSimulacao stats = service.getEstatisticasAtuais();
        assertEquals(1, stats.getTotalSimulacoesGlobais(), "O total de simulações globais deveria ser 1.");
        assertEquals(1, stats.getTotalSucessosGlobais(), "O total de sucessos globais deveria ser 1.");
    }
}