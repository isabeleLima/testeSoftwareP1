package com.example.testessoftware;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulacaoPainel extends JPanel {
    private List<Criatura> entidades;

    public SimulacaoPainel() {
        setPreferredSize(new Dimension(800, 500));
    }

    public void setEntidades(List<Criatura> entidades) {
        this.entidades = entidades;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (entidades == null) return;

        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        g2.setFont(new Font("Arial", Font.BOLD, 14));

        // --- LEGENDA ---
        int xTexto = 10;
        int yTexto = 20;
        int espacamento = 120;

        // Percorre a lista que AGORA CONTÉM O GUARDIÃO
        for (int i = 0; i < entidades.size(); i++) {
            Criatura c = entidades.get(i);

            if (c instanceof Guardiao) {
                g2.setColor(Color.GREEN);
                g2.drawString("Guardião: " + (int) c.getMoedas(), xTexto, yTexto);
            } else if (c instanceof Cluster) {
                g2.setColor(Color.RED);
                g2.drawString("Cluster: " + (int) c.getMoedas(), xTexto, yTexto);
            } else {
                g2.setColor(Color.BLUE);
                g2.drawString("C" + c.getId() + ": " + (int) c.getMoedas(), xTexto, yTexto);
            }
            xTexto += espacamento;
        }

        // --- DESENHO DAS ENTIDADES ---
        for (int i = 0; i < entidades.size(); i++) {
            Criatura c = entidades.get(i);

            // Define a cor baseada no tipo de entidade
            if (c instanceof Guardiao) {
                g2.setColor(Color.GREEN);
            } else if (c instanceof Cluster) {
                g2.setColor(Color.RED);
            } else {
                g2.setColor(Color.BLUE);
            }

            // Lógica de posição e tamanho (permanece a mesma)
            double posicaoArredondada = Math.round(c.getPosicao() / 1000.0) * 1000.0;
            int x = (int) (width / 2 + c.getPosicao() / 20000.0);

            // ---------------------------------------------

            int y = height / 2;
            int base = 10;
            int max = 60;
            int tamanho = Math.min(max, base + (int) (c.getMoedas() / 100_000));
            g2.fillOval(x - tamanho / 2, y - tamanho / 2, tamanho, tamanho);

            if (c instanceof Cluster) {
                g2.setColor(Color.WHITE);
                g2.drawOval(x - tamanho / 2, y - tamanho / 2, tamanho, tamanho);
            }
        }
    }
}
