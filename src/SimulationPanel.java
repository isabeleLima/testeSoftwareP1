import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationPanel extends JPanel {
    private List<Creature> creatures;

    public void setCreatures(List<Creature> creatures) {
        this.creatures = creatures;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (creatures == null) return;

        Graphics2D g2 = (Graphics2D) g;
        int height = getHeight();

        for (Creature c : creatures) {
            int x = (int) (getWidth() / 2 + c.getPosition() / 10000); // Escala
            g2.fillOval(x, height / 2, 10, 10);
            g2.drawString("G: " + (int) c.getGold(), x, height / 2 - 5);
        }
    }
}
