public class Creature {
    private int id;
    private double position;
    private double gold;

    public Creature(int id) {
        this.id = id;
        this.gold = 1_000_000;
        this.position = 0;
    }

    public int getId() {
        return id;
    }

    public double getPosition() {
        return position;
    }

    public double getGold() {
        return gold;
    }

    public void move() {
        double r = (Math.random() * 2 - 1); // Aleatório entre -1 e 1
        position += r * gold;
    }

    public void addGold(double amount) {
        this.gold += amount;
    }

    public void removeGold(double amount) {
        this.gold -= amount;
    }

    public double distanceTo(Creature other) {
        return Math.abs(this.position - other.position);
    }
}
