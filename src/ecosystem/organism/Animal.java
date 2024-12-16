package src.ecosystem.organism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

public abstract class Animal extends Organism {
    public Animal(int energy, int xPos, int yPos) {
        super(energy, xPos, yPos);
    }

    public abstract int getMoveSpeed();

    public abstract int getVisionRange();

    public abstract void reproduce(Organism[][] map);

    private boolean isValidPosition(int x, int y, int gridWidth, int gridHeight, Organism[][] map) {
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight && map[x][y] == null;
    }

    public Map<String, List<int[]>> detect(Organism[][] map) {
        int gridWidth = map.length;
        int gridHeight = map[0].length;
        int detectionRange = getVisionRange();

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
                            validMoves.add(new int[] { newX, newY }); 

                            Organism target = map[newX][newY];
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
        

        Map<String, List<int[]>> result = new HashMap<>();
        result.put("ValidMoves", validMoves);
        result.put("DetectedPlants", detectedPlantList);
        result.put("DetectedHerbivores", detectedHerbivorList);
        result.put("DetectedCarnivores", detectedCarnivorList);

        return result;
    }

    public void act(Organism[][] grid) {
    }
}
