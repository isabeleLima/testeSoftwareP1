package com.example.testessoftware;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        int n = 5; // Número de criaturas

        Simulacao simulacao = new Simulacao(n);

        JFrame frame = new JFrame("Simulação de Criaturas Saltitantes");
        SimulacaoPainel panel = new SimulacaoPainel();
        panel.setCriaturas(simulacao.getCreatures());
        frame.add(panel);
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulacao.iteracao();
                panel.repaint();
            }
        });

        timer.start();
    }
}
