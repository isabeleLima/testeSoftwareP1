package com.example.testessoftware;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TelaPrincipal extends JFrame {
    private GerenciadorUsuarios gerenciador;
    private Usuario usuarioLogado;

    private JLabel lblBemVindo;
    private JLabel lblFotoPerfil;
    private JLabel lblBemVindoFoto;

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
        setLayout(new BorderLayout(10, 10));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblBemVindo = new JLabel("Bem-vindo, " + usuarioLogado.getLogin());
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 18));
        topo.add(lblBemVindo);
        add(topo, BorderLayout.NORTH);

        JPanel painelPerfil = new JPanel();
        painelPerfil.setLayout(new BoxLayout(painelPerfil, BoxLayout.Y_AXIS));
        painelPerfil.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelPerfil.setOpaque(false);
        painelPerfil.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblFotoPerfil = new JLabel();
        lblFotoPerfil.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon iconeFoto = carregarFotoPerfil(usuarioLogado.getAvatar());
        lblFotoPerfil.setIcon(iconeFoto);

        lblBemVindoFoto = new JLabel("Bem-vindo");
        lblBemVindoFoto.setFont(new Font("Arial", Font.BOLD, 16));
        lblBemVindoFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBemVindoFoto.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        painelPerfil.add(lblFotoPerfil);
        painelPerfil.add(lblBemVindoFoto);

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

        if (!"admin".equalsIgnoreCase(usuarioLogado.getLogin())) {
            btnRemoverOutroUtilizador.setVisible(false);
        }

        botoes.add(btnSimular);
        botoes.add(btnEstatisticas);
        botoes.add(btnRemoverMinhaConta);
        botoes.add(btnRemoverOutroUtilizador);
        botoes.add(btnSair);

        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BorderLayout());
        painelCentro.add(painelPerfil, BorderLayout.NORTH);
        painelCentro.add(botoes, BorderLayout.SOUTH);

        add(painelCentro, BorderLayout.SOUTH);

        add(new JPanel(), BorderLayout.CENTER);

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

    private ImageIcon carregarFotoPerfil(String caminhoAvatar) {
        try {
            File arquivo = new File(caminhoAvatar);
            if (!arquivo.exists()) {
                arquivo = new File("default_avatar.png");
                if (!arquivo.exists()) return null;
            }
            BufferedImage img = ImageIO.read(arquivo);
            Image imgRed = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(imgRed);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
        JTextField campoCriaturas = new JTextField();
        JTextField campoRepeticoes = new JTextField();
        Object[] mensagem = {
                "Quantidade de criaturas (máx. 20):", campoCriaturas,
                "Quantidade de repetições (máx. 1000):", campoRepeticoes
        };

        int opcao = JOptionPane.showConfirmDialog(
                this,
                mensagem,
                "Configurar Simulação",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcao != JOptionPane.OK_OPTION) return;

        int criaturas, repeticoes;
        try {
            criaturas = Integer.parseInt(campoCriaturas.getText().trim());
            repeticoes = Integer.parseInt(campoRepeticoes.getText().trim());
            if (criaturas > 20 || repeticoes > 1000 || criaturas < 1 || repeticoes < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores inválidos. Máximo: 20 criaturas e 1000 repetições.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Simulacao simulacao = new Simulacao(criaturas);
        SimulacaoPainel painelVisual = new SimulacaoPainel();
        painelVisual.setEntidades(simulacao.getEntidades());

        JLabel lblRepeticao = new JLabel("Repetição: 0 de " + repeticoes);
        lblRepeticao.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.add(lblRepeticao, BorderLayout.NORTH);
        painelPrincipal.add(painelVisual, BorderLayout.CENTER);

        final JFrame telaSimulacao = new JFrame("Visualização da Simulação");
        telaSimulacao.setSize(800, 500);
        telaSimulacao.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        telaSimulacao.setLocationRelativeTo(this);
        telaSimulacao.add(painelPrincipal);
        telaSimulacao.setVisible(true);

        new Thread(() -> {
            final boolean[] sucesso = {false};

            for (int i = 0; i < repeticoes; i++) {
                final int atual = i + 1;
                simulacao.iteracao();
                painelVisual.setEntidades(simulacao.getEntidades());
                painelVisual.repaint();
                lblRepeticao.setText("Repetição: " + atual + " de " + repeticoes);

                if (simulacao.isBemSucedida()) {
                    sucesso[0] = true;
                    break;
                }

                try {
                    Thread.sleep(300);
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
