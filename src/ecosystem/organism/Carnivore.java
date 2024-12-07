package src.ecosystem.organism;

import src.ecosystem.environment.Environment;
import java.util.List;

public class Carnivore extends Animal {
    private static final int VISION_RANGE = 4; // Phạm vi tầm nhìn của động vật ăn thịt
    private static final double ENERGY_GAIN_PERCENTAGE = 0.1; // Phần trăm năng lượng thu được khi ăn động vật ăn cỏ
    private static final int ENERGY_THRESHOLD_FOR_REPRODUCTION = 150; // Ngưỡng năng lượng để sinh sản
    private static final int ENERGY_DECAY = 3; // Năng lượng mất đi nếu không tìm thấy con mồi

    public Carnivore(int energy, int xPos, int yPos) {
        super(energy, xPos, yPos);
    }

    @Override
    public int getMoveSpeed() {
        return 3; // Tốc độ di chuyển của Carnivore
    }

    @Override
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
            if (distance < minDistance && distance <= VISION_RANGE) {
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
                this.energy -= ENERGY_DECAY; // Giảm năng lượng vì đã di chuyển
            }

            // Nếu Carnivore di chuyển đến vị trí của Herbivore, nó sẽ tiêu thụ Herbivore
            if (this.xPos == nearestHerbivore.getxPos() && this.yPos == nearestHerbivore.getyPos()) {
                consumeHerbivore(nearestHerbivore);
                map[this.xPos][this.yPos] = this; // Carnivore thay thế vị trí của Herbivore
            }
        } else {
            loseEnergy();
            super.move(map); // Di chuyển ngẫu nhiên nếu không tìm thấy Herbivore
        }

        if (this.energy > ENERGY_THRESHOLD_FOR_REPRODUCTION) {
            reproduce(map);
        }
    }

    private void consumeHerbivore(Herbivore herbivore) {
        int gainedEnergy = (int) (herbivore.getEnergy() * ENERGY_GAIN_PERCENTAGE);
        this.energy += gainedEnergy; // Tăng năng lượng của Carnivore
        herbivore.setEnergy(0); // Xoá năng lượng của Herbivore (để loại bỏ nó)
    }

    private void loseEnergy() {
        this.energy -= ENERGY_DECAY; // Mất năng lượng nếu không tìm thấy thức ăn
    }

    private void reproduce(Organism[][] map) {
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
