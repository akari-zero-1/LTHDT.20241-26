package src.ecosystem.organism;
import java.util.List;
import java.util.Map;
//import java.util.Random;

public class Herbivore extends Animal {
    // Các thông số của Herbivore
    private static int moveSpeed = 2;
    private static int visionRange = 4;
    private static int energyThresholdForReproduction = 150;
    private static int energyDecay = 3;

    public Herbivore(int energy, int xPos, int yPos) {
        super(energy, xPos, yPos);
    }
    @Override
    public int getMoveSpeed() {
        return moveSpeed;
    }
    @Override
    public int getVisionRange() {
        return visionRange;
    }
    @Override
    public void reproduce(Organism[][] map) {
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
                        Herbivore offspring = new Herbivore(energy_new, newX, newY);
                        map[newX][newY]=offspring;
                        this.energy = this.energy - energy_new;// nang luong cua me giam di 1 nua
                        break;
                    }
                }
            }
        }
    }
    public void eat_Plant(Organism[][] map, int plantX, int plantY) {
        Plant plant = (Plant) map[plantX][plantY];
        int energy_current =this.getEnergy() + (int) (plant.getEnergy() * 0.1);
        this.setEnergy(energy_current); // Tăng năng lượng
        plant.setEnergy(0);
        map[plantX][plantY] = null; // Xóa cây khỏi map
        this.setxPos(plantX);
        this.setyPos(plantY);
        map[plantX][plantY] = this;

    }
    public void act(Organism[][] map) {

        Map<String, List<int[]>> detected = detect(map);
        List<int[]> detectedCarnivores = detected.get("DetectedCarnivores");
        List<int[]> validMoves = detected.get("ValidMoves");
        List<int[]> detectedPlants = detected.get("DetectedPlants");

        if (!detectedCarnivores.isEmpty()) {
            int[] escapePosition = findEscapePosition(detectedCarnivores, validMoves);
            if (escapePosition != null) {
                moveToPosition(escapePosition[0], escapePosition[1], map,validMoves);
                return;
            }
        }
        // sinh san neu du dieu kien
        if (this.getEnergy() >= energyThresholdForReproduction) {
            reproduce(map);
            return;
        }
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int neighborX = this.getxPos() + dx;
                int neighborY = this.getyPos() + dy;
                if (neighborX >= 0 && neighborX < map.length && neighborY >= 0 && neighborY < map[0].length && map[neighborX][neighborY] instanceof Plant) {
                    eat_Plant(map, neighborX, neighborY);
                    ateFood = 1;
                    return;

                }
            }
        }
        if (!detectedPlants.isEmpty()) {
            int[] plantPosition = detectedPlants.get(0);
            moveToPosition(plantPosition[0], plantPosition[1], map,validMoves);
            return;
        }
        moveRandomly(map,validMoves);
        if (ateFood ==0) {
            this.setEnergy(this.energy -energyDecay);
        }

    }

    // Tìm vị trí để chạy trốn khỏi động vật ăn thịt
    private int[] findEscapePosition(List<int[]> detectedCarnivores, List<int[]> validMoves) {
        int closestDistance = Integer.MAX_VALUE;
        int[] escapePosition = null;
        for (int[] carnivorePos : detectedCarnivores) {
            for(int[] Move : validMoves){
                int distance = (int) Math.sqrt(Math.pow(Move[0] -carnivorePos[0], 2) + Math.pow(Move[1] -carnivorePos[1], 2));
                if (distance < closestDistance) {
                    closestDistance = distance;
                    escapePosition = findFartherPosition(validMoves, carnivorePos[0], carnivorePos[1]);
                }
            }
        }
        return escapePosition;
    }
    // chạy trốn
    private int[] findFartherPosition(List<int[]> validMoves, int targetX, int targetY) {
        int[] bestPosition = null;
        int maxDistance = -1;
        for (int[] move : validMoves) {
            int newX = move[0];
            int newY = move[1];
            int distance = (int) Math.sqrt(Math.pow(newX - targetX, 2) + Math.pow(newY - targetY, 2));
            if (distance > maxDistance) {
                maxDistance = distance;
                bestPosition = new int[] { newX, newY };
            }
        }
        return bestPosition;
    }

    private void moveToPosition(int targetX, int targetY,Organism[][] map , List<int[]> validMoves) {
        int isValidMove = 0;
        for(int[] pos : validMoves){
            if (pos[0]==targetX&& pos[1]==targetY) {
                isValidMove =1;
                break;
            }
        }
        if (isValidMove ==1) {
            map[this.getxPos()][this.getyPos()] = null;
            this.setxPos(targetX);
            this.setyPos(targetY);
            map[targetX][targetY] = this;

        }
    }
    // Di chuyển ngẫu nhiên nếu không có hành động ưu tiên nào
    private void moveRandomly(Organism[][] map, List<int[]> validMoves) {
        if (validMoves.isEmpty()) {
            return;
        }
        for (int[] move : validMoves) {
            moveToPosition(move[0], move[1],map,validMoves);
            break;
        }
    }
    private void lose_energy(Organism[][] map){
        this.energy = -energyDecay;
        if (this.energy<=0) {
            map[this.getxPos()][this.getyPos()] = null;
        }
    }

    // Getter và Setter cho các thuộc tính của Herbivore
    public int getEnergy() {
        return super.getEnergy();
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
}