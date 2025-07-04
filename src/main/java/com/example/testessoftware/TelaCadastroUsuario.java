package com.example.testessoftware;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class TelaCadastroUsuario extends JFrame {
    private GerenciadorUsuarios gerenciador;
    private JTextField loginField;
    private JPasswordField senhaField;
    private JButton btnConfirmarCadastro;
    private JButton btnSelecionarAvatar;
    private JLabel labelAvatarSelecionado;

    private File arquivoAvatarSelecionado;

    public TelaCadastroUsuario(GerenciadorUsuarios gerenciador) {
        this.gerenciador = gerenciador;
        setTitle("Cadastro de Usuário");
        setSize(450, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

        // Label Login
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(new JLabel("Login:"), gbc);

        // Campo Login
        gbc.gridy = 1;
        loginField = new JTextField(20);
        painel.add(loginField, gbc);

        // Label Senha
        gbc.gridy = 2;
        painel.add(new JLabel("Senha:"), gbc);

        // Campo Senha
        gbc.gridy = 3;
        senhaField = new JPasswordField(20);
        painel.add(senhaField, gbc);

        // Label Avatar
        gbc.gridy = 4;
        painel.add(new JLabel("Avatar:"), gbc);

        // Botão Selecionar Avatar
        gbc.gridy = 5;
        btnSelecionarAvatar = new JButton("Selecionar Avatar...");
        painel.add(btnSelecionarAvatar, gbc);

        // Label para mostrar nome do arquivo selecionado
        gbc.gridy = 6;
        labelAvatarSelecionado = new JLabel("Nenhum arquivo selecionado");
        labelAvatarSelecionado.setForeground(Color.GRAY);
        painel.add(labelAvatarSelecionado, gbc);

        // Botão Cadastrar
        btnConfirmarCadastro = new JButton("Cadastrar");
        gbc.gridy = 7;
        gbc.insets = new Insets(20, 8, 8, 8);
        painel.add(btnConfirmarCadastro, gbc);

        add(painel);

        // Ação do botão Selecionar Avatar
        btnSelecionarAvatar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecione uma imagem de avatar");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Imagens (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"));

            int retorno = fileChooser.showOpenDialog(this);
            if (retorno == JFileChooser.APPROVE_OPTION) {
                arquivoAvatarSelecionado = fileChooser.getSelectedFile();
                labelAvatarSelecionado.setText(arquivoAvatarSelecionado.getName());
            }
        });

        btnConfirmarCadastro.addActionListener(e -> {
            String login = loginField.getText().trim();
            String senha = new String(senhaField.getPassword());

            if (login.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe login e senha", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Se o usuário não selecionou avatar, pode usar avatar default
            String caminhoAvatar = "default_avatar.png";

            if (arquivoAvatarSelecionado != null) {
                try {
                    // Cria pasta avatars se não existir
                    File pastaAvatars = new File("avatars");
                    if (!pastaAvatars.exists()) {
                        pastaAvatars.mkdir();
                    }

                    // Define nome do arquivo copiado (ex: avatars/login.png)
                    String extensao = getExtensaoArquivo(arquivoAvatarSelecionado.getName());
                    File destino = new File(pastaAvatars, login + extensao);

                    // Copia arquivo para destino
                    Files.copy(arquivoAvatarSelecionado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    caminhoAvatar = destino.getPath();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao salvar avatar: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (gerenciador.adicionarUsuario(login, senha, caminhoAvatar)) {
                JOptionPane.showMessageDialog(this,
                        "Usuário cadastrado com sucesso! Você pode fechar esta janela e fazer login.");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Usuário já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private String getExtensaoArquivo(String nomeArquivo) {
        int ponto = nomeArquivo.lastIndexOf(".");
        if (ponto > 0 && ponto < nomeArquivo.length() - 1) {
            return nomeArquivo.substring(ponto).toLowerCase();
        }
        return "";
    }
}
