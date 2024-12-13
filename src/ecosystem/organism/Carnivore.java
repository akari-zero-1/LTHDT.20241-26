package src.ecosystem.organism;

import src.ecosystem.environment.Environment;
import java.util.List;

public class Carnivore extends Animal {
    public static int moveSpeed = 2;
    public static int maxEnergy = 100;
    public static int visionRange = 4; // Phạm vi tầm nhìn của động vật ăn thịt
    public static double energyGainPercentage  = 0.1; // Phần trăm năng lượng thu được khi ăn động vật ăn cỏ
    public static int energyThresholdForReproduction  = 150; // Ngưỡng năng lượng để sinh sản
    public static int energyDecay = 3; // Năng lượng mất đi nếu không tìm thấy con mồi

    public static void setMaxEnergy(int maxEnergy) {
        Carnivore.maxEnergy = maxEnergy;
    }

    public static void setVisionRange(int visionRange) {
        Carnivore.visionRange = visionRange;
    }

    public static void setEnergyGainPercentage(double energyGainPercentage) {
        Carnivore.energyGainPercentage = energyGainPercentage;
    }

    public static void setEnergyThresholdForReproduction(int energyThresholdForReproduction) {
        Carnivore.energyThresholdForReproduction = energyThresholdForReproduction;
    }

    public static void setEnergyDecay(int energyDecay) {
        Carnivore.energyDecay = energyDecay;
    }

    public static void setMoveSpeed(int moveSpeed) {
        Carnivore.moveSpeed = moveSpeed;
    }

    public static int getMaxEnergy() {
        return maxEnergy;
    }

    public static double getEnergyGainPercentage() {
        return energyGainPercentage;
    }

    public static int getEnergyThresholdForReproduction() {
        return energyThresholdForReproduction;
    }

    public static int getEnergyDecay() {
        return energyDecay;
    }
    

    public Carnivore(int energy, int xPos, int yPos) {
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

    public void move(Organism[][] map) {
        Environment env = new Environment(map.length, map[0].length);
        List<Herbivore> herbivores = env.getAllHerbivores();

        if (herbivores.isEmpty()) {
            loseEnergy();
            super.move(map); // Di chuyển ngẫu nhiên nếu không tìm thấy Herbivore
            return;
        }

        Herbivore nearestHerbivore = null;
        double minDistance = Double.MAX_VALUE;

        // Tìm Herbivore gần nhất trong phạm vi VISION_RANGE
        for (Herbivore herbivore : herbivores) {
            double distance = calculateDistance(this.xPos, this.yPos, herbivore.getxPos(), herbivore.getyPos());
            if (distance < minDistance && distance <= visionRange) {
                nearestHerbivore = herbivore;
                minDistance = distance;
            }
        }

        if (nearestHerbivore != null) {
            int dx = Integer.compare(nearestHerbivore.getxPos(), this.xPos);
            int dy = Integer.compare(nearestHerbivore.getyPos(), this.yPos);
            int newX = this.xPos + dx;
            int newY = this.yPos + dy;

            if (isValidMove(newX, newY, map)) {
                map[this.xPos][this.yPos] = null; // Giải phóng ô cũ
                this.xPos = newX;
                this.yPos = newY;
                map[this.xPos][this.yPos] = this; // Di chuyển đến ô mới
                this.energy -= energyDecay; // Giảm năng lượng vì đã di chuyển
            }

            // Nếu Carnivore di chuyển đến vị trí của Herbivore, nó sẽ tiêu thụ Herbivore
            if (this.xPos == nearestHerbivore.getxPos() && this.yPos == nearestHerbivore.getyPos()) {
                eat(nearestHerbivore);
                map[this.xPos][this.yPos] = this; // Carnivore thay thế vị trí của Herbivore
            }
        } else {
            loseEnergy();
            super.move(map); // Di chuyển ngẫu nhiên nếu không tìm thấy Herbivore
        }

        if (this.energy > energyThresholdForReproduction) {
            reproduce(map);
        }
    }

    public void eat(Herbivore herbivore) {
        int gainedEnergy = (int) (herbivore.getEnergy() * energyGainPercentage);
        this.energy += gainedEnergy; // Tăng năng lượng của Carnivore
        herbivore.setEnergy(0); // Xoá năng lượng của Herbivore (để loại bỏ nó)
    }

    private void loseEnergy() {
        this.energy -= energyDecay; // Mất năng lượng nếu không tìm thấy thức ăn
    }

    public void reproduce(Organism[][] map) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

        for (int[] direction : directions) {
            int newX = this.xPos + direction[0];
            int newY = this.yPos + direction[1];

            if (isValidMove(newX, newY, map)) {
                Carnivore offspring = new Carnivore(70, newX, newY); // Con non có 70 năng lượng
                map[newX][newY] = offspring;
                this.energy -= 70; // Giảm năng lượng của Carnivore để sinh sản
                break; // Sinh sản xong thì dừng lại
            }
        }
    }

    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private boolean isValidMove(int x, int y, Organism[][] map) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length && map[x][y] == null;
    }

    @Override
    public String toString() {
        return "Carnivore [energy=" + energy + ", xPos=" + xPos + ", yPos=" + yPos + "]";
    }
}
