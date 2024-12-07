package src.ecosystem.organism;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Carnivore extends Animal {
    private static final int MOVE_SPEED = 2; // Speed of movement for Carnivore
    private static final int ENERGY_GAIN_FROM_PREY = 50; // Energy gained by consuming prey

    public Carnivore(int energy, int xPos, int yPos) {
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

        List<int[]> preyPositions = new ArrayList<>();
        List<int[]> emptyPositions = new ArrayList<>();

        // Scan for prey and valid empty positions within move range
        for (int dx = -MOVE_SPEED; dx <= MOVE_SPEED; dx++) {
            for (int dy = -MOVE_SPEED; dy <= MOVE_SPEED; dy++) {
                int newX = this.xPos + dx;
                int newY = this.yPos + dy;

                if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
                    if (map[newX][newY] instanceof Herbivore) {
                        preyPositions.add(new int[]{newX, newY}); // Add herbivore position
                    } else if (map[newX][newY] == null) {
                        emptyPositions.add(new int[]{newX, newY}); // Add empty cell
                    }
                }
            }
        }

        Random random = new Random();

        // Prioritize hunting prey
        if (!preyPositions.isEmpty()) {
            int[] target = preyPositions.get(random.nextInt(preyPositions.size()));

            // Move to prey's position and consume
            map[this.xPos][this.yPos] = null;
            this.xPos = target[0];
            this.yPos = target[1];
            this.energy += ENERGY_GAIN_FROM_PREY;
            map[this.xPos][this.yPos] = this;
        } else if (!emptyPositions.isEmpty()) {
            // Move to a random empty position if no prey is available
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
