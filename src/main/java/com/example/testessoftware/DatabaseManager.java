package com.example.testessoftware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gerencia a conexão com o banco de dados SQLite e a criação do schema.
 */
public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:simulacao.db";

    /**
     * Estabelece uma conexão com o banco de dados.
     * @return um objeto Connection.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Inicializa o banco de dados, criando todas as tabelas necessárias.
     */
    public static void initializeDatabase() {
        criarTabelaUsuarios();
        criarTabelaEstatisticas();
    }

    private static void criarTabelaUsuarios() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + " login TEXT PRIMARY KEY NOT NULL,"
                + " senha TEXT NOT NULL,"
                + " avatar TEXT,"
                + " simulacoesBemSucedidas INTEGER DEFAULT 0,"
                + " totalSimulacoes INTEGER DEFAULT 0"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cria a tabela para armazenar estatísticas globais da aplicação.
     */
    private static void criarTabelaEstatisticas() {
        String sqlTabela = "CREATE TABLE IF NOT EXISTS estatisticas_globais ("
                + " id INTEGER PRIMARY KEY CHECK (id = 1),"
                + " total_simulacoes INTEGER NOT NULL DEFAULT 0,"
                + " total_sucessos INTEGER NOT NULL DEFAULT 0"
                + ");";

        String sqlInsert = "INSERT OR IGNORE INTO estatisticas_globais (id, total_simulacoes, total_sucessos) VALUES (1, 0, 0);";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlTabela);
            stmt.execute(sqlInsert);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de estatísticas: " + e.getMessage());
        }
    }
}
