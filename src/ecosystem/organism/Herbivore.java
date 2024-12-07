package src.ecosystem.organism;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Herbivore extends Animal {
    private static final int MOVE_SPEED = 1; // Movement speed for Herbivore
    private static final int ENERGY_GAIN_FROM_PLANT = 20; // Energy gained by eating a plant

    public Herbivore(int energy, int xPos, int yPos) {
        super(energy, xPos, yPos);
    }

    @Override
    public int getMoveSpeed() {
        return MOVE_SPEED;
    }

    @Override
    public void move(Organism[][] map) {
        int gridWidth = map.length;
        int gridHeight = map[0].length;

        List<int[]> plantPositions = new ArrayList<>();
        List<int[]> emptyPositions = new ArrayList<>();

        // Scan for plants and valid empty positions within move range
        for (int dx = -MOVE_SPEED; dx <= MOVE_SPEED; dx++) {
            for (int dy = -MOVE_SPEED; dy <= MOVE_SPEED; dy++) {
                int newX = this.xPos + dx;
                int newY = this.yPos + dy;

                if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
                    if (map[newX][newY] instanceof Plant) {
                        plantPositions.add(new int[]{newX, newY}); // Add plant position
                    } else if (map[newX][newY] == null) {
                        emptyPositions.add(new int[]{newX, newY}); // Add empty cell
                    }
                }
            }
        }

        Random random = new Random();

        // Prioritize eating plants
        if (!plantPositions.isEmpty()) {
            int[] target = plantPositions.get(random.nextInt(plantPositions.size()));

            // Move to the plant's position and consume it
            map[this.xPos][this.yPos] = null;
            this.xPos = target[0];
            this.yPos = target[1];
            this.energy += ENERGY_GAIN_FROM_PLANT;
            map[this.xPos][this.yPos] = this;
        } else if (!emptyPositions.isEmpty()) {
            // Move to a random empty position if no plants are available
            int[] target = emptyPositions.get(random.nextInt(emptyPositions.size()));

            map[this.xPos][this.yPos] = null;
            this.xPos = target[0];
            this.yPos = target[1];
            map[this.xPos][this.yPos] = this;
        }

        // Energy decay after action
        this.energy -= energyDecay;

        // Check for death (handled by environment clean-up)
        if (this.energy <= 0) {
            map[this.xPos][this.yPos] = null;
        }
    }
}
