package com.example.testessoftware;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GerenciadorUsuarios gerenciador = new GerenciadorUsuarios();
            TelaLogin telaLogin = new TelaLogin(gerenciador);
            telaLogin.setVisible(true);
        });
    }
}

