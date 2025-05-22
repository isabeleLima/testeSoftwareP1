package com.example.testessoftware;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulacaoPainel extends JPanel {
    private List<Criatura> criaturas;
    private final Color[] cores = {
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW
    };

    public void setCriaturas(List<Criatura> criaturas) {
        this.criaturas = criaturas;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (criaturas == null) return;

        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        g2.setFont(new Font("Arial", Font.BOLD, 14));

        // Exibe moedas no topo com cor correspondente
        int xTexto = 10;
        int yTexto = 20;
        for (int i = 0; i < criaturas.size(); i++) {
            Criatura c = criaturas.get(i);
            g2.setColor(cores[i % cores.length]);
            String texto = "C" + (i + 1) + "=" + (int) c.getMoedas();
            g2.drawString(texto, xTexto, yTexto);
            xTexto += g2.getFontMetrics().stringWidth(texto + "  ");
        }

        // Desenha criaturas com tamanho proporcional às moedas
        for (int i = 0; i < criaturas.size(); i++) {
            Criatura c = criaturas.get(i);
            g2.setColor(cores[i % cores.length]);

            int x = (int) (width / 2 + c.getPosition() / 10000); // Escala da posição
            int y = height / 2;

            int base = 10;
            int max = 60;
            int tamanho = (int) (base + c.getMoedas() / 100_000);
            tamanho = Math.min(max, tamanho); // Limita o tamanho máximo

            g2.fillOval(x - tamanho / 2, y - tamanho / 2, tamanho, tamanho);
        }
    }
}
