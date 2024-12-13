package src.ecosystem.organism;

import java.util.*;

public class Carnivore extends Animal {
    private static int moveSpeed = 3;
    private static int visionRange = 4;
    private static int energyThresholdReproduction = 100;
    private static int energyDecay = 3;


    public Carnivore(int xPos, int yPos, int energy) {
        super(xPos, yPos, energy);
    }

    @Override
    public int getMoveSpeed() {
        return moveSpeed; // Tốc độ di chuyển của Carnivore
    }

    @Override
    public int getVisionRange() {
        return visionRange;
    }

    // Hành động của Carnivore theo thứ tự ưu tiên
    public void act(Organism[][] map) {
        // 1. Dò tìm môi trường xung quanh để lấy thông tin về con mồi và ô trống
        Map<String, List<int[]>> detections = super.detect(map); // Use inherited detect method

        // 2. Sinh sản nếu năng lượng đủ
        if (this.energy > energyThresholdReproduction) {
            reproduce(map);
            return;
        }

        // 3. Ăn nếu có Herbivore ở vị trí hiện tại
        for (int[] pos : detections.get("DetectedHerbivores")) {
            if (pos[0] == this.getxPos() && pos[1] == this.getyPos()) {
                consumeHerbivore(map, (Herbivore) map[pos[0]][pos[1]]); // Pass map as parameter
                return;
            }
        }

        // 4. Truy đuổi Herbivore nếu có con mồi trong tầm nhìn
        if (!detections.get("DetectedHerbivores").isEmpty()) {
            int[] herbivorePos = detections.get("DetectedHerbivores").get(0); // Lấy con mồi đầu tiên
            chaseHerbivore(map, herbivorePos[0], herbivorePos[1]);
            return;
        }

        // 5. Di chuyển ngẫu nhiên nếu không tìm thấy con mồi
        if (!detections.get("ValidMoves").isEmpty()) {
            randomMove(map, detections.get("ValidMoves"));
            return;
        }

        // 6. Giảm năng lượng do không thực hiện được hành động nào
        loseEnergy();
    }


    @Override
    public void reproduce(Organism[][] map) {
        if (this.getEnergy() >= energyThresholdReproduction) {
            int gridWidth = map.length;
            int gridHeight = map[0].length;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0)
                        continue;
                    int newX = this.getxPos() + dx;
                    int newY = this.getyPos() + dy;
                    if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight && map[newX][newY] == null) {
                        int energy_new = this.energy / 2;
                        Herbivore offspring = new Herbivore(energy_new, newX, newY);
                        map[newX][newY] = offspring;
                        this.energy = this.energy - energy_new; // Năng lượng của mẹ giảm đi một nửa
                        break;
                    }
                }
            }
        }
    }

    // Ăn Herbivore tại vị trí hiện tại
        private void consumeHerbivore(Organism[][] map, Herbivore herbivore) {
            this.energy += herbivore.getEnergy(); // Nhận năng lượng từ Herbivore
            // Remove Herbivore from the map by setting its position to null
            map[herbivore.getxPos()][herbivore.getyPos()] = null; // Remove Herbivore from the map
        }


    // Truy đuổi Herbivore
    private void chaseHerbivore(Organism[][] map, int targetX, int targetY) {
        if (isInBounds(targetX, targetY, map) && map[targetX][targetY] instanceof Herbivore) {
            map[this.xPos][this.yPos] = null; // Xóa vị trí cũ
            this.xPos = targetX;
            this.yPos = targetY;
            consumeHerbivore(map, (Herbivore) map[targetX][targetY]); // Pass map to consumeHerbivore
            map[targetX][targetY] = this; // Di chuyển đến vị trí mới
        }
    }


    // Di chuyển ngẫu nhiên đến một vị trí trống lân cận
    private void randomMove(Organism[][] map, List<int[]> validMoves) {
        if (!validMoves.isEmpty()) {
            int[] pos = validMoves.get(new Random().nextInt(validMoves.size())); // Chọn ngẫu nhiên vị trí
            map[this.xPos][this.yPos] = null; // Xóa vị trí hiện tại
            this.xPos = pos[0];
            this.yPos = pos[1];
            map[this.xPos][this.yPos] = this; // Đặt vị trí mới
        }
    }

    // Kiểm tra xem vị trí (x, y) có nằm trong bản đồ không
    private boolean isInBounds(int x, int y, Organism[][] map) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }

    // Giảm năng lượng nếu không thực hiện hành động nào
    private void loseEnergy() {
        this.energy -= energyDecay;
        if (this.energy <= 0) {
            die();
        }
    }

    // Phương thức để đánh dấu rằng sinh vật đã chết
    private void die() {
        // Có thể được mở rộng nếu cần
    }
}
