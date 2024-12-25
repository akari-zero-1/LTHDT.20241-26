package src.ecosystem.organism;

import java.util.List;

public class Plant extends Organism {
    public static int energyGain = 10;
    public static int defaultEnergy = 300;

    public Plant(int energy, int x, int y) {
        super(energy, x, y);
    }

    public Plant(int x, int y) {
        super(x, y);
        this.energy = defaultEnergy;
    }

    public void act(Organism[][] map, List<Organism> organisms) {
        grow();
    }

    private void grow() {
        setEnergy(getEnergy() + 1);
    }

    public static void setEnergyGain(int energyGain) {
        Plant.energyGain = energyGain;
    }

    public static int getEnergyGain() {
        return energyGain;
    }

}