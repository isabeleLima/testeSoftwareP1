package com.example.testessoftware.sistema;

import com.example.testessoftware.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suíte de Testes de Sistema.
 * Valida as jornadas do usuário do início ao fim, focando na lógica
 * de negócio que suporta as ações da UI.
 */
public class SimuladorSystemTest {

    private GerenciadorUsuarios gerenciador;
    private Connection conn;

    @BeforeEach
    void setUp() throws SQLException {
        // Usa um banco de dados em memória para simular um ambiente limpo a cada teste
        conn = DatabaseManager.connect();
        DatabaseManager.initializeDatabase();
        gerenciador = new GerenciadorUsuarios();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpa o banco de dados
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM usuarios");
            stmt.execute("UPDATE estatisticas_globais SET total_simulacoes = 0, total_sucessos = 0 WHERE id = 1");
        }
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * RASTREABILIDADE: Jornada do Usuário - Cadastro, Login e Visualização de Estatísticas.
     * 1. Usuário se cadastra.
     * 2. Usuário faz login.
     * 3. Usuário visualiza a tela de estatísticas (que deve estar vazia).
     */
    @Test
    void testJornada_CadastroLoginEVisualizacaoDeEstatisticas() {
        // 1. Cadastro
        boolean cadastrado = gerenciador.adicionarUsuario("jornada_user", "pass123", "avatar.png");
        assertTrue(cadastrado);

        // 2. Login
        Usuario u = gerenciador.autenticar("jornada_user", "pass123");
        assertNotNull(u);

        // 3. Visualização de Estatísticas
        EstatisticasService statsService = new EstatisticasService(gerenciador);
        EstatisticasSimulacao stats = statsService.getEstatisticasAtuais();

        assertNotNull(stats);
        assertEquals(0, stats.getTotalSimulacoesGlobais());
        assertEquals(1, stats.getUsuarios().size(), "Deveria haver 1 usuário registrado nas estatísticas.");
    }

    /**
     * RASTREABILIDADE: Jornada do Usuário - Execução de Simulação.
     * 1. Usuário se cadastra e faz login.
     * 2. Usuário executa uma simulação com sucesso.
     * 3. Verifica se suas estatísticas pessoais e as globais foram atualizadas.
     */
    @Test
    void testJornada_ExecutarSimulacaoEVerificarAtualizacao() {
        // 1. Cadastro e Login
        gerenciador.adicionarUsuario("simulador_user", "abc", "b.png");
        Usuario u = gerenciador.autenticar("simulador_user", "abc");
        assertNotNull(u);

        // 2. Simula a finalização de uma simulação bem-sucedida
        u.incrementarTotal();
        u.incrementarSucesso();
        gerenciador.registrarResultadoSimulacao(u, true);

        // 3. Verificação
        EstatisticasService statsService = new EstatisticasService(gerenciador);
        EstatisticasSimulacao stats = statsService.getEstatisticasAtuais();

        assertEquals(1, stats.getTotalSimulacoesGlobais(), "O total global de simulações deveria ser 1.");
        assertEquals(1, stats.getTotalSucessosGlobais(), "O total global de sucessos deveria ser 1.");

        Usuario usuarioAtualizado = stats.getUsuarios().iterator().next();
        assertEquals(1, usuarioAtualizado.getTotalSimulacoes());
        assertEquals(1, usuarioAtualizado.getSimulacoesBemSucedidas());
    }

    /**
     * RASTREABILIDADE: Jornada do Usuário - Remoção da Própria Conta.
     * 1. Dois usuários se cadastram.
     * 2. Um deles remove a própria conta.
     * 3. Verifica se o usuário foi removido e se o outro permanece.
     */
    @Test
    void testJornada_RemocaoContaPropria() {
        // 1. Cadastro
        gerenciador.adicionarUsuario("user_a_ser_removido", "1", "1.png");
        gerenciador.adicionarUsuario("user_permanente", "2", "2.png");

        // 2. Remoção
        boolean removido = gerenciador.removerUsuario("user_a_ser_removido");
        assertTrue(removido);

        // 3. Verificação
        Map<String, Usuario> usuariosRestantes = gerenciador.getTodosUsuarios();
        assertEquals(1, usuariosRestantes.size(), "Apenas um usuário deveria restar.");
        assertNull(usuariosRestantes.get("user_a_ser_removido"));
        assertNotNull(usuariosRestantes.get("user_permanente"));
    }

    /**
     * RASTREABILIDADE: Jornada do Usuário - Admin Remove Outro Usuário.
     * 1. Admin e um usuário comum são cadastrados.
     * 2. Admin remove a conta do usuário comum.
     * 3. Verifica se a conta do usuário foi removida.
     */
    @Test
    void testJornada_AdminRemoveOutroUsuario() {
        // 1. Cadastro
        gerenciador.adicionarUsuario("admin", "admin_pass", "admin.png");
        gerenciador.adicionarUsuario("usuario_alvo", "alvo_pass", "alvo.png");

        // 2. Admin remove o alvo (simulando a lógica do botão)
        boolean removido = gerenciador.removerUsuario("usuario_alvo");
        assertTrue(removido);

        // 3. Verificação
        assertNull(gerenciador.autenticar("usuario_alvo", "alvo_pass"), "O usuário alvo não deveria mais existir.");
        assertNotNull(gerenciador.autenticar("admin", "admin_pass"), "A conta do admin deveria permanecer intacta.");
    }
}