package src.ecosystem.organism;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plant extends Organism {
    public static int energyGain = 10;
    public static int reproductionRange = 2;
    public static int reproductionThreshold = 20;

    public Plant(int energy, int x, int y) {
        super(energy, x, y);
    }

    public void act() {
        grow();
    }

    private void grow() {
        setEnergy(getEnergy() + 1);
    }

    public void reproduce(Organism[][] map) {
        if (getEnergy() >= reproductionThreshold) {
            List<int[]> validCells = new ArrayList<>();

            // Tìm các ô trong phạm vi sinh sản
            for (int dx = -reproductionRange; dx <= reproductionRange; dx++) {
                for (int dy = -reproductionRange; dy <= reproductionRange; dy++) {
                    int newX = this.xPos + dx;
                    int newY = this.yPos + dy;

                    if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[0].length) {
                        if (map[newX][newY] == null) {
                            validCells.add(new int[] { newX, newY });
                        }
                    }
                }
            }

            // Nếu có ô hợp lệ, sinh sản tại một ô ngẫu nhiên
            if (!validCells.isEmpty()) {
                Random random = new Random();
                int[] selectedCell = validCells.get(random.nextInt(validCells.size()));

                Plant newPlant = new Plant(energyGain, selectedCell[0], selectedCell[1]);
                map[selectedCell[0]][selectedCell[1]] = newPlant;

                setEnergy(getEnergy() - reproductionThreshold);
            }
        }
    }
}
