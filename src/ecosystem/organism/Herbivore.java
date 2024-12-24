package src.ecosystem.organism;

import java.util.List;
import java.util.Map;

public class Herbivore extends Animal {
    // Các thông số của Herbivore
    public static int moveSpeed = 2;
    public static int visionRange = 3;
    public static int energyThresholdForReproduction = 250;
    public static int energyDecay = 10;
    public static int defaultEnergy = 200;

    public Herbivore(int energy, int xPos, int yPos) {
        super(energy, xPos, yPos);
    }

    public Herbivore(int xPos, int yPos) {
        super(xPos, yPos);
        this.energy = defaultEnergy;
    }

    public void act(Organism[][] map, List<Organism> organisms) {
        Map<String, List<int[]>> detected = detect(map);
        List<int[]> detectedCarnivores = detected.get("DetectedCarnivores");
        List<int[]> validMoves = detected.get("ValidMoves");
        List<int[]> detectedPlants = detected.get("DetectedPlants");
        // System.out.println("Herbivore"+detected);
        // Chạy trốn
        // System.out.println(this);
        if (!detectedCarnivores.isEmpty()) {
            int[] carnivorePos = detectedCarnivores.get(0);
            int[] farthestMove = findFarthestLocation(map[carnivorePos[0]][carnivorePos[1]], validMoves);

            if (farthestMove != null) {
                moveTo(farthestMove[0], farthestMove[1], map);
                // System.out.println("Herbivore - Moving to " + farthestMove[0] + ", " +
                // farthestMove[1]);
                return;
            }
        }

        // Sinh sản
        if (this.getEnergy() >= energyThresholdForReproduction) {
            // System.err.println("Reproduce");
            reproduce(map);
            return;
        }

        // Ăn cây ở cạnh
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int neighborX = this.getxPos() + dx;
                int neighborY = this.getyPos() + dy;
                if (neighborX >= 0 && neighborX < map.length && neighborY >= 0 && neighborY < map[0].length
                        && map[neighborX][neighborY] instanceof Plant) {
                    eat(map[neighborX][neighborY], map, organisms);
                    // System.err.println("Eat Plant at " + neighborX + ", " + neighborY);
                    return;
                }
            }
        }

        // Di chuyển đến nơi gần cỏ nhất
        if (!detectedPlants.isEmpty()) {
            int[] plantPos = detectedPlants.get(0);
            int[] closestMove = findClosestLocation(map[plantPos[0]][plantPos[1]], validMoves);

            if (closestMove != null) {
                moveTo(closestMove[0], closestMove[1], map);
                // System.out.println("Herbivore - Moving to " + closestMove[0] + ", " +
                // closestMove[1]);
                return;
            }
        }

        // Di chuyển ngẫu nhiên
        if (!validMoves.isEmpty()) {
            int[] randomMove = validMoves.get((int) (Math.random() * validMoves.size()));
            moveTo(randomMove[0], randomMove[1], map);
            // System.out.println("Herbivore Random Move to " + randomMove[0] + ", " +
            // randomMove[1]);
        }

        // Mất năng lượng do hoạt động
        loseEnergy();
    }

    private void loseEnergy() {
        this.energy -= energyDecay;
        if (this.energy <= 0) {
            this.setAlive(false);
        }
    }

    // Getter và Setter cho các thuộc tính của Herbivore
    public int getEnergy() {
        return super.getEnergy();
    }

    public static void setMoveSpeed(int moveSpeed) {
        Herbivore.moveSpeed = moveSpeed;
    }

    public static void setVisionRange(int visionRange) {
        Herbivore.visionRange = visionRange;
    }

    public static void setEnergyDecay(int energyDecay) {
        Herbivore.energyDecay = energyDecay;
    }

    public void setEnergy(int energy) {
        super.setEnergy(energy);
    }

    public int getxPos() {
        return super.getxPos();
    }

    public void setxPos(int xPos) {
        super.setxPos(xPos);
    }

    public int getyPos() {
        return super.getyPos();
    }

    public void setyPos(int yPos) {
        super.setyPos(yPos);
    }

    @Override
    public int getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    public int getVisionRange() {
        return visionRange;
    }

    public static void setEnergyThresholdForReproduction(int energyThresholdForReproduction) {
        Herbivore.energyThresholdForReproduction = energyThresholdForReproduction;
    }

    @Override
    public int getEnergyThresholdForReproduction() {
        return energyThresholdForReproduction;
    }

    public static void setDefaultEnergy(int defaultEnergy) {
        Herbivore.defaultEnergy = defaultEnergy;
    }

    public static int getDefaultEnergy() {
        return defaultEnergy;
    }
}