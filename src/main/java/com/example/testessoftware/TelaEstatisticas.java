package com.example.testessoftware;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class TelaEstatisticas extends JFrame {

    public TelaEstatisticas(EstatisticasSimulacao estatisticas) {
        setTitle("Estatísticas da Simulação");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] colunas = {"Login", "Simulações Totais", "Simulações Bem Sucedidas", "Avatar"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);

        if (estatisticas != null && estatisticas.getUsuarios() != null) {
            for (Usuario u : estatisticas.getUsuarios()) {
                Object[] linha = {
                        u.getLogin(),
                        u.getTotalSimulacoes(),
                        u.getSimulacoesBemSucedidas(),
                        u.getAvatar()
                };
                modelo.addRow(linha);
            }
        }

        JTable tabela = new JTable(modelo);
        tabela.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(tabela);

        JLabel lblTotais = new JLabel(String.format(
                "<html><b>Estatísticas Globais:</b> Total de Simulações: %d &nbsp;&nbsp;|&nbsp;&nbsp; Total de Sucessos: %d &nbsp;&nbsp;|&nbsp;&nbsp; Taxa de Sucesso Global: %.2f%% &nbsp;&nbsp;|&nbsp;&nbsp; Média de Sucessos/Utilizador: %.2f</html>",
                estatisticas != null ? estatisticas.getTotalSimulacoesGlobais() : 0,
                estatisticas != null ? estatisticas.getTotalSucessosGlobais() : 0,
                estatisticas != null ? estatisticas.getTaxaSucessoGlobal() : 0.0,
                estatisticas != null ? estatisticas.getMediaSucessosPorUsuario() : 0.0
        ));
        lblTotais.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTotais.setFont(new Font("Arial", Font.PLAIN, 14));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(lblTotais, BorderLayout.SOUTH);
    }
}
