package src.ecosystem.organism;

public abstract class Organism {
    public int energy;
    public int xPos;
    public int yPos;
    public boolean isAlive = true;

    public Organism(int energy, int xPos, int yPos) {
        this.energy = energy;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public Organism(int energy) {
        this.energy = energy;

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

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    };

    @Override
    public String toString() {
        return "Organism [energy=" + energy + ", xPos=" + xPos + ", yPos=" + yPos + "]";
    }
}
