package com.example.testessoftware;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    private GerenciadorUsuarios gerenciador;
    private Usuario usuarioLogado;
    private JLabel lblBemVindo;
    private JButton btnSimular, btnSair, btnEstatisticas, btnRemoverMinhaConta, btnRemoverOutroUtilizador;

    public TelaPrincipal(GerenciadorUsuarios gerenciador, Usuario usuario) {
        this.gerenciador = gerenciador;
        this.usuarioLogado = usuario;

        setTitle("Simulação - Utilizador: " + usuario.getLogin());
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
        btnRemoverMinhaConta = new JButton("Remover Minha Conta");
        btnRemoverOutroUtilizador = new JButton("Remover Outro Utilizador");
        btnSair = new JButton("Sair");

        btnRemoverMinhaConta.setBackground(new Color(220, 53, 69));
        btnRemoverMinhaConta.setForeground(Color.WHITE);
        btnRemoverOutroUtilizador.setBackground(new Color(108, 117, 125));
        btnRemoverOutroUtilizador.setForeground(Color.WHITE);

        botoes.add(btnSimular);
        botoes.add(btnEstatisticas);
        botoes.add(btnRemoverMinhaConta);
        botoes.add(btnRemoverOutroUtilizador);
        botoes.add(btnSair);

        add(botoes, BorderLayout.SOUTH);

        add(new JPanel(), BorderLayout.CENTER);

        // Listeners
        btnSimular.addActionListener(e -> iniciarSimulacaoComVisualizacao());

        btnEstatisticas.addActionListener(e -> {
            EstatisticasService service = new EstatisticasService(gerenciador);
            EstatisticasSimulacao dadosEstatisticos = service.getEstatisticasAtuais();
            TelaEstatisticas telaEst = new TelaEstatisticas(dadosEstatisticos);
            telaEst.setVisible(true);
        });

        btnRemoverMinhaConta.addActionListener(e -> removerContaPropria());

        btnRemoverOutroUtilizador.addActionListener(e -> removerOutroUtilizador());

        btnSair.addActionListener(e -> {
            new TelaLogin(gerenciador).setVisible(true);
            this.dispose();
        });
    }

    private void removerContaPropria() {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Tem a certeza que deseja remover permanentemente a sua conta?\nEsta ação não pode ser desfeita.",
                "Confirmar Remoção de Conta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            boolean removido = gerenciador.removerUsuario(usuarioLogado.getLogin());
            if (removido) {
                JOptionPane.showMessageDialog(this, "Conta removida com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                new TelaLogin(gerenciador).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Ocorreu um erro ao remover a sua conta.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerOutroUtilizador() {
        String loginParaRemover = JOptionPane.showInputDialog(
                this,
                "Digite o login do utilizador que deseja remover:",
                "Remover Outro Utilizador",
                JOptionPane.PLAIN_MESSAGE
        );

        if (loginParaRemover != null && !loginParaRemover.trim().isEmpty()) {
            if (loginParaRemover.equals(usuarioLogado.getLogin())) {
                JOptionPane.showMessageDialog(this, "Para remover a sua própria conta, utilize o botão 'Remover Minha Conta'.", "Ação Inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Tem a certeza que deseja remover permanentemente o utilizador '" + loginParaRemover + "'?",
                    "Confirmar Remoção",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (resposta == JOptionPane.YES_OPTION) {
                boolean removido = gerenciador.removerUsuario(loginParaRemover);
                if (removido) {
                    JOptionPane.showMessageDialog(this, "Utilizador '" + loginParaRemover + "' removido com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Utilizador não encontrado ou ocorreu um erro ao remover.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
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

                gerenciador.registrarResultadoSimulacao(usuarioLogado, sucesso[0]);

                telaSimulacao.dispose();
                this.toFront();
            });
        }).start();
    }
}
