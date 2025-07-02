package com.example.testessoftware;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    private GerenciadorUsuarios gerenciador;
    private Usuario usuarioLogado;
    private JLabel lblBemVindo;
    private JButton btnSimular, btnSair, btnEstatisticas;

    public TelaPrincipal(GerenciadorUsuarios gerenciador, Usuario usuario) {
        this.gerenciador = gerenciador;
        this.usuarioLogado = usuario;

        setTitle("Simulação - Usuário: " + usuario.getLogin());
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Usar BorderLayout para o frame principal
        setLayout(new BorderLayout(10, 10));
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblBemVindo = new JLabel("Bem-vindo, " + usuarioLogado.getLogin());
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 18));
        topo.add(lblBemVindo);
        add(topo, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnSimular = new JButton("Iniciar nova simulação");
        btnEstatisticas = new JButton("Mostrar Estatísticas");
        btnSair = new JButton("Sair");

        botoes.add(btnSimular);
        botoes.add(btnEstatisticas);
        botoes.add(btnSair);

        add(botoes, BorderLayout.SOUTH);

        // Espaço para futuro painel (ex: lista de usuários ou logs)
        add(new JPanel(), BorderLayout.CENTER);

        // Listeners
        btnSimular.addActionListener(e -> iniciarSimulacaoComVisualizacao());

        btnEstatisticas.addActionListener(e -> {
            EstatisticasSimulacao estatisticas = new EstatisticasSimulacao();
            for (Usuario u : gerenciador.getTodosUsuarios().values()) {
                estatisticas.registrarSimulacao(u, false);
            }
            TelaEstatisticas telaEst = new TelaEstatisticas(gerenciador.getTodosUsuarios().values(), estatisticas);
            telaEst.setVisible(true);
        });

        btnSair.addActionListener(e -> {
            new TelaLogin(gerenciador).setVisible(true);
            this.dispose();
        });
    }

    private void iniciarSimulacaoComVisualizacao() {
        SimulacaoSempreVencedora simulacao = new SimulacaoSempreVencedora(5);
        SimulacaoPainel painelVisual = new SimulacaoPainel();
        painelVisual.setEntidades(simulacao.getEntidades());

        final JFrame telaSimulacao = new JFrame("Visualização da Simulação"); // final para lambda
        telaSimulacao.setSize(800, 500);
        telaSimulacao.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        telaSimulacao.setLocationRelativeTo(this);
        telaSimulacao.add(painelVisual);
        telaSimulacao.setVisible(true);

        new Thread(() -> {
            final boolean[] sucesso = {false};  // wrapper para mutabilidade dentro do lambda

            for (int i = 0; i < 20; i++) {
                simulacao.iteracao();
                painelVisual.setEntidades(simulacao.getEntidades());
                painelVisual.repaint();

                if (simulacao.isBemSucedida()) {
                    sucesso[0] = true;
                    break;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            SwingUtilities.invokeLater(() -> {
                usuarioLogado.incrementarTotal();
                if (sucesso[0]) {
                    usuarioLogado.incrementarSucesso();
                    JOptionPane.showMessageDialog(telaSimulacao, "Simulação finalizada com SUCESSO!", "Resultado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(telaSimulacao, "Simulação finalizada sem sucesso.", "Resultado", JOptionPane.WARNING_MESSAGE);
                }

                gerenciador.salvarUsuarios();

                telaSimulacao.dispose();   // Fecha a janela da simulação
                this.toFront();            // (Opcional) traz a tela principal para frente
            });
        }).start();
    }
}
