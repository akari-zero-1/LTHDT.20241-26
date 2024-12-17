package src.ecosystem.organism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

public abstract class Animal extends Organism {
    public boolean isAlive = true;

    public Animal(int energy, int xPos, int yPos) {
        super(energy, xPos, yPos);
    }

    public Animal(int energy) {
        super(energy);
    }

    public abstract int getMoveSpeed();

    public abstract int getVisionRange();

    public abstract int getEnergyThresholdForReproduction();

    private boolean isValidPosition(int x, int y, int gridWidth, int gridHeight, Organism[][] map) {
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
    }

    public Map<String, List<int[]>> detect(Organism[][] map) {
        int gridWidth = map.length;
        int gridHeight = map[0].length;
        int detectionRange = getVisionRange();
        int moveSpeed = getMoveSpeed();
        int currentX = this.getxPos();
        int currentY = this.getyPos();

        List<int[]> validMoves = new ArrayList<>();
        List<int[]> detectedPlantList = new ArrayList<>();
        List<int[]> detectedHerbivorList = new ArrayList<>();
        List<int[]> detectedCarnivorList = new ArrayList<>();

        // Duyệt qua tất cả các ô trong detectionRange
        for (int radius = 1; radius <= detectionRange; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (Math.abs(dx) == radius || Math.abs(dy) == radius) {
                        int newX = currentX + dx;
                        int newY = currentY + dy;

                        // Kiểm tra xem vị trí có hợp lệ không
                        if (isValidPosition(newX, newY, gridWidth, gridHeight, map)) {
                            // System.out.println(newX+" "+newY);
                            if (map[newX][newY] == null && -moveSpeed <= dx && dx <= moveSpeed && -moveSpeed <= dy
                                    && dy <= moveSpeed) {
                                validMoves.add(new int[] { newX, newY });
                            } else {
                                Organism target = map[newX][newY];
                                // System.out.println(target);
                                if (target != null) {
                                    if (target instanceof Plant) {
                                        detectedPlantList.add(new int[] { newX, newY });
                                    } else if (target instanceof Herbivore) {
                                        detectedHerbivorList.add(new int[] { newX, newY });
                                    } else if (target instanceof Carnivore) {
                                        detectedCarnivorList.add(new int[] { newX, newY });
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Map<String, List<int[]>> result = new HashMap<>();
        result.put("ValidMoves", validMoves);
        result.put("DetectedPlants", detectedPlantList);
        result.put("DetectedHerbivores", detectedHerbivorList);
        result.put("DetectedCarnivores", detectedCarnivorList);

        return result;
    }

    public void moveTo(int locationX, int locationY, Organism[][] map) {
        int currentX = this.getxPos();
        int currentY = this.getyPos();

        // Check if the target position is valid
        if (map[locationX][locationY] == null) {
            // Move to the new position
            map[locationX][locationY] = this;
            map[currentX][currentY] = null;
            this.setxPos(locationX);
            this.setyPos(locationY);
        }
    }

    public void eat(Organism target, Organism[][] map, List<Organism> organisms) {
        if (target != null) {
            // Gain energy from the target
            this.setEnergy(this.getEnergy() + target.getEnergy());
            target.setAlive(false);
            map[target.getxPos()][target.getyPos()] = null;
        }
    }

    public int[] findClosestLocation(Organism target, List<int[]> validMoves) {
        int targetX = target.getxPos();
        int targetY = target.getyPos();

        int[] closestLocation = null;
        double minDistance = Double.MAX_VALUE;

        for (int[] move : validMoves) {
            int moveX = move[0];
            int moveY = move[1];

            // Calculate the Euclidean distance
            double distance = Math.sqrt(Math.pow(targetX - moveX, 2) + Math.pow(targetY - moveY, 2));

            if (distance < minDistance) {
                minDistance = distance;
                closestLocation = move;
            }
        }

        return closestLocation;
    }

    public int[] findFarthestLocation(Organism target, List<int[]> validMoves) {
        int targetX = target.getxPos();
        int targetY = target.getyPos();

        int[] farthestLocation = null;
        double maxDistance = Double.MIN_VALUE;

        for (int[] move : validMoves) {
            int moveX = move[0];
            int moveY = move[1];

            // Calculate the Euclidean distance
            double distance = Math.sqrt(Math.pow(targetX - moveX, 2) + Math.pow(targetY - moveY, 2));

            if (distance > maxDistance) {
                maxDistance = distance;
                farthestLocation = move;
            }
        }

        return farthestLocation;
    }

    public void reproduce(Organism[][] map) {
        int energyThresholdForReproduction = this.getEnergyThresholdForReproduction();

        if (this.getEnergy() >= energyThresholdForReproduction) {
            int gridWidth = map.length;
            int gridHeight = map[0].length;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0)
                        continue;
                    int newX = this.xPos + dx;
                    int newY = this.yPos + dy;
                    if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight && map[newX][newY] == null) {
                        int energy_new = this.energy / 2;
                        this.energy -= energy_new;
                        Organism offspring = createOffspring(energy_new, newX, newY);
                        //System.out.println("Parent: " + this);
                        //System.out.println("Offspring: " + offspring);
                        map[newX][newY] = offspring;
                        this.energy -= energy_new;
                        return;
                    }
                }
            }
        }
    }

    private Organism createOffspring(int energy, int xPos, int yPos) {
        if (this instanceof Herbivore) {
            return new Herbivore(energy, xPos, yPos);
        } else if (this instanceof Carnivore) {
            return new Carnivore(energy, xPos, yPos);
        } else {
            return null;
        }
    }

    public void act(Organism[][] map, List<Organism> organisms) {

    }

}
