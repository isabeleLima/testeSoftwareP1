package com.example.testessoftware;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Gerencia os utilizadores utilizando um banco de dados SQLite, garantindo a consistência dos dados.
 */
public class GerenciadorUsuarios {

    public GerenciadorUsuarios() {
        DatabaseManager.initializeDatabase();
    }

    /**
     * Remove um utilizador e as suas estatísticas correspondentes do banco de dados
     * de forma transacional.
     * @param login O login do utilizador a ser removido.
     * @return true se o utilizador foi removido com sucesso, false caso contrário.
     */
    public boolean removerUsuario(String login) {
        String sqlSelect = "SELECT totalSimulacoes, simulacoesBemSucedidas FROM usuarios WHERE login = ?";
        String sqlDelete = "DELETE FROM usuarios WHERE login = ?";
        String sqlUpdateStats = "UPDATE estatisticas_globais SET "
                + "total_simulacoes = total_simulacoes - ?, "
                + "total_sucessos = total_sucessos - ? "
                + "WHERE id = 1";

        try (Connection conn = DatabaseManager.connect()) {
            // Inicia a transação
            conn.setAutoCommit(false);

            int totalSimulacoesDoUtilizador = 0;
            int sucessosDoUtilizador = 0;

            // 1. Primeiro, busca as estatísticas do utilizador que será removido.
            try (PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
                pstmtSelect.setString(1, login);
                ResultSet rs = pstmtSelect.executeQuery();
                if (rs.next()) {
                    totalSimulacoesDoUtilizador = rs.getInt("totalSimulacoes");
                    sucessosDoUtilizador = rs.getInt("simulacoesBemSucedidas");
                } else {
                    // Se o utilizador não existe, não há nada a fazer.
                    conn.rollback();
                    return false;
                }
            }

            // 2. Remove o utilizador da tabela 'usuarios'.
            try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {
                pstmtDelete.setString(1, login);
                int affectedRows = pstmtDelete.executeUpdate();
                if (affectedRows == 0) {
                    // Se a remoção falhou por algum motivo, desfaz a transação.
                    conn.rollback();
                    return false;
                }
            }

            // 3. Atualiza a tabela de estatísticas globais, subtraindo os valores do utilizador removido.
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateStats)) {
                pstmtUpdate.setInt(1, totalSimulacoesDoUtilizador);
                pstmtUpdate.setInt(2, sucessosDoUtilizador);
                pstmtUpdate.executeUpdate();
            }

            // 4. Se todas as operações foram bem-sucedidas, efetiva a transação.
            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao remover utilizador (transação revertida): " + e.getMessage());
            // A exceção já teria causado um rollback implícito, mas é boa prática ter um bloco catch.
            return false;
        }
    }

    // --- Outros métodos permanecem os mesmos ---

    public void registrarResultadoSimulacao(Usuario usuario, boolean sucesso) {
        String sqlUsuario = "UPDATE usuarios SET simulacoesBemSucedidas = ?, totalSimulacoes = ? WHERE login = ?";
        String sqlGlobal = "UPDATE estatisticas_globais SET "
                + "total_simulacoes = total_simulacoes + 1, "
                + "total_sucessos = total_sucessos + ? "
                + "WHERE id = 1";
        try (Connection conn = DatabaseManager.connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmtUsuario = conn.prepareStatement(sqlUsuario);
                 PreparedStatement pstmtGlobal = conn.prepareStatement(sqlGlobal)) {
                pstmtUsuario.setInt(1, usuario.getSimulacoesBemSucedidas());
                pstmtUsuario.setInt(2, usuario.getTotalSimulacoes());
                pstmtUsuario.setString(3, usuario.getLogin());
                pstmtUsuario.executeUpdate();
                pstmtGlobal.setInt(1, sucesso ? 1 : 0);
                pstmtGlobal.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro ao registrar resultado (rollback executado): " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erro na conexão ao registrar resultado: " + e.getMessage());
        }
    }

    public boolean adicionarUsuario(String login, String senha, String avatar) {
        String sql = "INSERT INTO usuarios(login, senha, avatar) VALUES(?,?,?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, senha);
            pstmt.setString(3, avatar);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar utilizador: " + e.getMessage());
            return false;
        }
    }

    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String senhaDoBanco = rs.getString("senha");
                if (senhaDoBanco.equals(senha)) {
                    Usuario u = new Usuario();
                    u.setLogin(rs.getString("login"));
                    u.setSenha(senhaDoBanco);
                    u.setAvatar(rs.getString("avatar"));
                    u.setSimulacoesBemSucedidas(rs.getInt("simulacoesBemSucedidas"));
                    u.setTotalSimulacoes(rs.getInt("totalSimulacoes"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Map<String, Usuario> getTodosUsuarios() {
        String sql = "SELECT * FROM usuarios";
        Map<String, Usuario> usuarios = new HashMap<>();
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setAvatar(rs.getString("avatar"));
                u.setSimulacoesBemSucedidas(rs.getInt("simulacoesBemSucedidas"));
                u.setTotalSimulacoes(rs.getInt("totalSimulacoes"));
                usuarios.put(u.getLogin(), u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return usuarios;
    }
}
