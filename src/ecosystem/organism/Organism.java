package src.ecosystem.organism;

public class Organism {
    private int energy;
    private int xPos;
    private int yPos;

    public Organism(int energy, int xPos, int yPos) {
        this.energy = energy;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    @Override
    public String toString() {
        return "Organism [energy=" + energy + ", xPos=" + xPos + ", yPos=" + yPos + "]";
    }
}
