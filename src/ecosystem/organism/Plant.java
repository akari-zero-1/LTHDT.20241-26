package src.ecosystem.organism;

public class Plant extends Organism {
    public static int energyGain = 10;
    public static int reproductionRange = 2;
    public static int defaultEnergy = 300;

    public Plant(int energy, int x, int y) {
        super(energy, x, y);
    }

    public Plant(int x, int y) {
        super(x, y);
        this.energy=defaultEnergy;
    }

    public void act() {
        grow();
    }

    private void grow() {
        setEnergy(getEnergy() + 1);
    }

    public static void setEnergyGain(int energyGain) {
        Plant.energyGain = energyGain;
    }

    public static void setReproductionRange(int reproductionRange) {
        Plant.reproductionRange = reproductionRange;
    }

    public static int getEnergyGain() {
        return energyGain;
    }

    public static int getReproductionRange() {
        return reproductionRange;
    }
}