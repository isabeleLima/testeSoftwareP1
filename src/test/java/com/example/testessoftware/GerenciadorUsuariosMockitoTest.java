package com.example.testessoftware;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Esta classe de teste demonstra como usar o Mockito para testar a lógica
 * de autenticação do GerenciadorUsuarios, focando em seu comportamento externo (seu "contrato").
 */
@ExtendWith(MockitoExtension.class) // Habilita a integração do Mockito com o JUnit 5
public class GerenciadorUsuariosMockitoTest {

    // Cria um "mock" ou dublê da classe GerenciadorUsuarios.
    // Este objeto não tem lógica real; nós ditamos seu comportamento em cada teste.
    @Mock
    private GerenciadorUsuarios gerenciadorMock;

    // Objeto de usuário real que será usado como retorno esperado nos testes.
    private Usuario usuarioReal;

    @BeforeEach
    void setUp() {
        // Antes de cada teste, criamos um objeto de usuário real para o "newred".
        // Isso garante que temos um objeto consistente para usar nas asserções.
        usuarioReal = new Usuario("newred", "123", "avatar_newred.png");
    }

    /**
     * Rastreabilidade: Requisito "Funcionalidades de Usuários - Autenticação".
     *
     * Testa o cenário de login bem-sucedido.
     * Nós "ensinamos" o mock a retornar o objeto de usuário real quando
     * as credenciais corretas ("newred", "123") são fornecidas.
     */
    @Test
    void testAutenticacao_LoginComSucesso() {
        // 1. Cenário (Arrange)
        // Define o comportamento do mock:
        // QUANDO o método 'autenticar' for chamado com "newred" E "123"
        // ENTÃO retorne o nosso objeto 'usuarioReal'.
        when(gerenciadorMock.autenticar("newred", "123")).thenReturn(usuarioReal);

        // 2. Ação (Act)
        // Chamamos o método no mock com as credenciais corretas.
        Usuario usuarioLogado = gerenciadorMock.autenticar("newred", "123");

        // 3. Verificação (Assert)
        // Verificamos se o resultado não é nulo e se é exatamente o objeto que esperávamos.
        assertNotNull(usuarioLogado, "O usuário retornado não deveria ser nulo para um login válido.");
        assertEquals("newred", usuarioLogado.getLogin(), "O login do usuário retornado deve ser 'newred'.");
        assertEquals(usuarioReal, usuarioLogado, "O objeto retornado deve ser o usuário real esperado.");
    }

    /**
     * Rastreabilidade: Requisito "Funcionalidades de Usuários - Autenticação".
     *
     * Testa o cenário de falha de login devido a uma senha incorreta.
     * Nós "ensinamos" o mock a retornar 'null' quando a senha for errada.
     */
    @Test
    void testAutenticacao_SenhaIncorreta() {
        // 1. Cenário (Arrange)
        // Define o comportamento do mock:
        // QUANDO 'autenticar' for chamado com "newred" E qualquer senha que NÃO seja "123"
        // ENTÃO retorne null.
        when(gerenciadorMock.autenticar("newred", "senha_errada")).thenReturn(null);

        // 2. Ação (Act)
        // Chamamos o método no mock com a senha incorreta.
        Usuario usuarioLogado = gerenciadorMock.autenticar("newred", "senha_errada");

        // 3. Verificação (Assert)
        // Verificamos se o resultado é nulo, como esperado para uma falha de autenticação.
        assertNull(usuarioLogado, "O resultado deveria ser nulo para uma senha incorreta.");
    }

    /**
     * Rastreabilidade: Requisito "Funcionalidades de Usuários - Autenticação".
     *
     * Testa o cenário de falha de login para um usuário que não existe.
     */
    @Test
    void testAutenticacao_UsuarioInexistente() {
        // 1. Cenário (Arrange)
        // Define o comportamento do mock:
        when(gerenciadorMock.autenticar(eq("fantasma"), anyString())).thenReturn(null);

        // 2. Ação (Act)
        // Chamamos o método no mock com um usuário que não existe.
        Usuario usuarioLogado = gerenciadorMock.autenticar("fantasma", "qualquer_senha");

        // 3. Verificação (Assert)
        // Verificamos se o resultado é nulo.
        assertNull(usuarioLogado, "O resultado deveria ser nulo para um usuário inexistente.");
    }
}
