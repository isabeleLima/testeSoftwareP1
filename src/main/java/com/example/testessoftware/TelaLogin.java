package com.example.testessoftware;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaLogin extends JFrame {
    private GerenciadorUsuarios gerenciador;
    private JTextField loginField;
    private JPasswordField senhaField;
    private JButton btnLogin, btnCadastro;

    public TelaLogin(GerenciadorUsuarios gerenciador) {
        this.gerenciador = gerenciador;
        setTitle("Login / Cadastro");
        setSize(300, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel painel = new JPanel(new GridLayout(3, 2, 5,5));
        painel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        painel.add(new JLabel("Login:"));
        loginField = new JTextField();
        painel.add(loginField);

        painel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        painel.add(senhaField);

        btnLogin = new JButton("Entrar");
        btnCadastro = new JButton("Cadastrar");

        painel.add(btnLogin);
        painel.add(btnCadastro);

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

        btnCadastro.addActionListener(e -> {
            String login = loginField.getText().trim();
            String senha = new String(senhaField.getPassword());
            if (login.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe login e senha", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (gerenciador.adicionarUsuario(login, senha, "default_avatar.png")) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado! Agora faça login.");
            } else {
                JOptionPane.showMessageDialog(this, "Usuário já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
