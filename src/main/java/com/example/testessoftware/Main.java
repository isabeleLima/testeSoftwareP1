package com.example.testessoftware;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        int n = 5; // Número de criaturas
        Simulation simulation = new Simulation(n);

        JFrame frame = new JFrame("Simulação de Criaturas Saltitantes");
        SimulationPanel panel = new SimulationPanel();
        panel.setCreatures(simulation.getCreatures());
        frame.add(panel);
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulation.iterate();
                panel.repaint();
            }
        });

        timer.start();
    }
}
