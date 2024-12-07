package src.ecosystem.organism;

import src.ecosystem.environment.Environment;
import java.util.List;

public class Herbivore extends Animal {
    private static final int VISION_RANGE = 3; // Phạm vi tầm nhìn của động vật ăn cỏ
    private static final double ENERGY_GAIN_PERCENTAGE = 0.1; // Phần trăm năng lượng thu được khi ăn nhà sản xuất
    private static final int ENERGY_THRESHOLD_FOR_REPRODUCTION = 100; // Ngưỡng năng lượng để sinh sản
    private static final int ENERGY_DECAY = 2; // Năng lượng mất đi nếu không tìm thấy nhà sản xuất

    public Herbivore(int energy, int xPos, int yPos) {
        super(energy, xPos, yPos);
    }

    @Override
    public int getMoveSpeed() {
        return 2; // Tốc độ di chuyển của Herbivore
    }

    @Override
    public void move(Organism[][] map) {
        Environment env = new Environment(map.length, map[0].length);
        List<Plant> producers = env.getAllProducers();

        if (producers.isEmpty()) {
            loseEnergy();
            super.move(map); // Di chuyển ngẫu nhiên nếu không tìm thấy Producer
            return;
        }

        Plant nearestProducer = null;
        double minDistance = Double.MAX_VALUE;

        // Tìm nhà sản xuất (Producer) gần nhất trong phạm vi
        for (Plant producer : producers) {
            double distance = calculateDistance(this.xPos, this.yPos, producer.getxPos(), producer.getyPos());
            if (distance < minDistance && distance <= VISION_RANGE) {
                nearestProducer = producer;
                minDistance = distance;
            }
        }

        if (nearestProducer != null) {
            int dx = Integer.compare(nearestProducer.getxPos(), this.xPos);
            int dy = Integer.compare(nearestProducer.getyPos(), this.yPos);
            int newX = this.xPos + dx;
            int newY = this.yPos + dy;

            if (isValidMove(newX, newY, map)) {
                map[this.xPos][this.yPos] = null; // Giải phóng ô cũ
                this.xPos = newX;
                this.yPos = newY;
                map[this.xPos][this.yPos] = this; // Di chuyển đến ô mới
                this.energy -= ENERGY_DECAY; // Giảm năng lượng vì đã di chuyển
            }

            // Nếu Herbivore di chuyển đến vị trí của Producer, thì nó sẽ tiêu thụ Producer
            if (this.xPos == nearestProducer.getxPos() && this.yPos == nearestProducer.getyPos()) {
                consumeProducer(nearestProducer);
                map[this.xPos][this.yPos] = this; // Herbivore thay thế vị trí của Producer
            }
        } else {
            loseEnergy();
            super.move(map); // Di chuyển ngẫu nhiên nếu không tìm thấy Producer
        }

        if (this.energy > ENERGY_THRESHOLD_FOR_REPRODUCTION) {
            reproduce(map);
        }
    }

    private void consumeProducer(Plant producer) {
        int gainedEnergy = (int) (producer.getEnergy() * ENERGY_GAIN_PERCENTAGE);
        this.energy += gainedEnergy; // Tăng năng lượng của Herbivore
        producer.setEnergy(0); // Xoá năng lượng của nhà sản xuất (để loại bỏ nó)
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
                Herbivore offspring = new Herbivore(50, newX, newY); // Con non có 50 năng lượng
                map[newX][newY] = offspring;
                this.energy -= 50; // Giảm năng lượng của Herbivore để sinh sản
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
        return "Herbivore [energy=" + energy + ", xPos=" + xPos + ", yPos=" + yPos + "]";
    }
}
