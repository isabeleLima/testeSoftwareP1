package com.example.testessoftware;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Serviço responsável por buscar e consolidar os dados de estatísticas do banco de dados.
 */
public class EstatisticasService {

    private final GerenciadorUsuarios gerenciadorUsuarios;

    public EstatisticasService(GerenciadorUsuarios gerenciadorUsuarios) {
        this.gerenciadorUsuarios = gerenciadorUsuarios;
    }

    /**
     * Busca todas as estatísticas (globais e por utilizador) do banco de dados
     * e as retorna em um objeto consolidado.
     * @return um objeto EstatisticasSimulacao populado com os dados.
     */
    public EstatisticasSimulacao getEstatisticasAtuais() {
        int totalSimulacoes = 0;
        int totalSucessos = 0;

        String sql = "SELECT total_simulacoes, total_sucessos FROM estatisticas_globais WHERE id = 1";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalSimulacoes = rs.getInt("total_simulacoes");
                totalSucessos = rs.getInt("total_sucessos");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar estatísticas globais: " + e.getMessage());
        }

        Collection<Usuario> todosUsuarios = gerenciadorUsuarios.getTodosUsuarios().values();

        return new EstatisticasSimulacao(totalSimulacoes, totalSucessos, todosUsuarios);
    }
}
