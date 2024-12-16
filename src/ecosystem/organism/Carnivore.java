package src.ecosystem.organism;

import java.util.*;

public class Carnivore extends Animal {
    private static final int visionRange = 4;
    private static final int energyThresholdForReproduction = 100; // Ngưỡng năng lượng để sinh sản
    private static final int energyLossPerTurn = 2; // Năng lượng mất mỗi lần hành động
    private static final int moveSpeed = 3; // Tốc độ di chuyển của Carnivore

    public Carnivore(int xPos, int yPos, int energy) {
        super(xPos, yPos, energy);
    }

    @Override
    public int getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    public int getVisionRange() {
        return visionRange;
    }

    // Hành động của Carnivore theo thứ tự ưu tiên
    public void act(Organism[][] map) {
        Map<String, List<int[]>> detections = detect(map);

        if (this.energy > energyThresholdForReproduction) {
            reproduce(map);
            return;
        }

        for (int[] pos : detections.get("DetectedHerbivores")) {
            if (pos[0] == this.getxPos() && pos[1] == this.getyPos()) {
                consumeHerbivore((Herbivore) map[pos[0]][pos[1]]);
                return;
            }
        }

        if (!detections.get("DetectedHerbivores").isEmpty()) {
            int[] herbivorePos = detections.get("DetectedHerbivores").get(0);
            chaseHerbivore(map, herbivorePos[0], herbivorePos[1]);
            return;
        }

        if (!detections.get("ValidMoves").isEmpty()) {
            randomMove(map, detections.get("ValidMoves"));
            return;
        }

        loseEnergy();
    }

    public Map<String, List<int[]>> detect(Organism[][] map) {
        Map<String, List<int[]>> detectionResults = new HashMap<>();
        detectionResults.put("DetectedHerbivores", new ArrayList<>());
        detectionResults.put("ValidMoves", new ArrayList<>());

        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};

        for (int i = 0; i < 4; i++) {
            int newX = this.xPos + dx[i];
            int newY = this.yPos + dy[i];
            if (isInBounds(newX, newY, map)) {
                if (map[newX][newY] instanceof Herbivore) {
                    detectionResults.get("DetectedHerbivores").add(new int[]{newX, newY});
                } else if (map[newX][newY] == null) {
                    detectionResults.get("ValidMoves").add(new int[]{newX, newY});
                }
            }
        }
        return detectionResults;
    }

    @Override
    public void reproduce(Organism[][] map) {
        if (this.getEnergy() >= energyThresholdForReproduction) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    int newX = this.xPos + dx;
                    int newY = this.yPos + dy;
                    if (isInBounds(newX, newY, map) && map[newX][newY] == null) {
                        int energyNew = this.energy / 2;
                        Herbivore offspring = new Herbivore(newX, newY, energyNew);
                        map[newX][newY] = offspring;
                        this.energy -= energyNew;
                        break;
                    }
                }
            }
        }
    }

    private void consumeHerbivore(Herbivore herbivore) {
        this.energy += herbivore.getEnergy();
    }

    private void chaseHerbivore(Organism[][] map, int targetX, int targetY) {
        if (isInBounds(targetX, targetY, map) && map[targetX][targetY] instanceof Herbivore) {
            map[this.xPos][this.yPos] = null;
            this.xPos = targetX;
            this.yPos = targetY;
            consumeHerbivore((Herbivore) map[targetX][targetY]);
            map[targetX][targetY] = this;
        }
    }

    private void randomMove(Organism[][] map, List<int[]> validMoves) {
        if (!validMoves.isEmpty()) {
            int[] pos = validMoves.get(new Random().nextInt(validMoves.size()));
            map[this.xPos][this.yPos] = null;
            this.xPos = pos[0];
            this.yPos = pos[1];
            map[this.xPos][this.yPos] = this;
        }
    }

    private boolean isInBounds(int x, int y, Organism[][] map) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }

    private void loseEnergy() {
        this.energy -= energyLossPerTurn;
        if (this.energy <= 0) {
            die();
        }
    }

    private void die() {
    }
}
