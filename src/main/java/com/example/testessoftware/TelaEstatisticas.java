package com.example.testessoftware;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class TelaEstatisticas extends JFrame {

    public TelaEstatisticas(Collection<Usuario> usuarios, EstatisticasSimulacao estatisticas) {
        setTitle("Estatísticas da Simulação");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] colunas = {"Login", "Simulações Totais", "Simulações Bem Sucedidas", "Avatar"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);

        for (Usuario u : usuarios) {
            Object[] linha = {
                    u.getLogin(),
                    u.getTotalSimulacoes(),
                    u.getSimulacoesBemSucedidas(),
                    u.getAvatar()
            };
            modelo.addRow(linha);
        }

        JTable tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);

        JLabel lblTotais = new JLabel(String.format("Total Simulações: %d   |   Total Sucessos: %d   |   Média Sucesso/Usuário: %.2f   |   Taxa Global de Sucesso: %.2f%%",
                estatisticas.getTotalSimulacoes(),
                estatisticas.getTotalSucessos(),
                estatisticas.getMediaSucessosPorUsuario(usuarios),
                estatisticas.getTaxaSucessoGlobal() * 100));

        lblTotais.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(lblTotais, BorderLayout.SOUTH);
    }
}
