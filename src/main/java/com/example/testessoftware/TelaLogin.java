package com.example.testessoftware;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {
    private GerenciadorUsuarios gerenciador;
    private JTextField loginField;
    private JPasswordField senhaField;
    private JButton btnLogin, btnCadastroLink;

    public TelaLogin(GerenciadorUsuarios gerenciador) {
        this.gerenciador = gerenciador;
        setTitle("Login / Cadastro");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gbc.insets = new Insets(8, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        // Label Login - linha 0
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(new JLabel("Login:"), gbc);

        // Campo Login - linha 1
        gbc.gridy = 1;
        loginField = new JTextField(20);
        painel.add(loginField, gbc);

        // Label Senha - linha 2
        gbc.gridy = 2;
        painel.add(new JLabel("Senha:"), gbc);

        // Campo Senha - linha 3
        gbc.gridy = 3;
        senhaField = new JPasswordField(20);
        painel.add(senhaField, gbc);

        // Botão Entrar - linha 4
        btnLogin = new JButton("Entrar");
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 8, 8, 8);
        painel.add(btnLogin, gbc);

        // Botão Cadastrar estilo link - linha 5
        btnCadastroLink = new JButton("<HTML><U>Cadastrar</U></HTML>");
        btnCadastroLink.setBorderPainted(false);
        btnCadastroLink.setContentAreaFilled(false);
        btnCadastroLink.setFocusPainted(false);
        btnCadastroLink.setForeground(Color.BLUE);
        btnCadastroLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 8, 8, 8);
        painel.add(btnCadastroLink, gbc);

        add(painel);

        btnLogin.addActionListener(e -> {
            String login = loginField.getText().trim();
            String senha = new String(senhaField.getPassword());
            Usuario u = gerenciador.autenticar(login, senha);
            if (u != null) {
                new TelaPrincipal(gerenciador, u).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login ou senha inválidos", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCadastroLink.addActionListener(e -> {
            TelaCadastroUsuario telaCadastro = new TelaCadastroUsuario(gerenciador);
            telaCadastro.setVisible(true);
        });
    }
}
