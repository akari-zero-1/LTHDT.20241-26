package src.ecosystem.organism;


import java.util.*;

public class Carnivore extends Organism {
    private static final int ENERGY_THRESHOLD_FOR_REPRODUCTION = 100; // Ngưỡng năng lượng để sinh sản
    private static final int ENERGY_LOSS_PER_TURN = 2; // Năng lượng mất mỗi lần hành động

    public Carnivore(int xPos, int yPos, int energy) {
        super(xPos, yPos, energy);
    }

    // Hành động của Carnivore theo thứ tự ưu tiên
    public void act(Organism[][] map) {
        // 1. Dò tìm môi trường xung quanh để lấy thông tin về con mồi và ô trống
        Map<String, List<int[]>> detections = detect(map);

        // 2. Sinh sản nếu năng lượng đủ
        if (this.energy > ENERGY_THRESHOLD_FOR_REPRODUCTION) {
            reproduce(map);
            return;
        }

        // 3. Ăn nếu có Herbivore ở vị trí hiện tại
        for (int[] pos : detections.get("DetectedHerbivores")) {
            if (pos[0] == this.getxPos() && pos[1] == this.getyPos()) {
                consumeHerbivore((Herbivore) map[pos[0]][pos[1]]);
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

    // Phát hiện các Herbivore và ô trống xung quanh
    private Map<String, List<int[]>> detect(Organism[][] map) {
        Map<String, List<int[]>> detectionResults = new HashMap<>();
        detectionResults.put("DetectedHerbivores", new ArrayList<>());
        detectionResults.put("ValidMoves", new ArrayList<>());

        int[] dx = {-1, 0, 1, 0}; // Hướng di chuyển theo trục x (trên, phải, dưới, trái)
        int[] dy = {0, 1, 0, -1}; // Hướng di chuyển theo trục y

        for (int i = 0; i < 4; i++) {
            int newX = this.xPos + dx[i];
            int newY = this.yPos + dy[i];
            if (isInBounds(newX, newY, map)) {
                if (map[newX][newY] instanceof Herbivore) {
                    // Nếu phát hiện Herbivore, thêm vị trí của nó vào danh sách
                    detectionResults.get("DetectedHerbivores").add(new int[]{newX, newY});
                } else if (map[newX][newY] == null) {
                    // Nếu là ô trống, thêm vào danh sách các ô trống có thể di chuyển
                    detectionResults.get("ValidMoves").add(new int[]{newX, newY});
                }
            }
        }
        return detectionResults;
    }

    // Sinh sản Carnivore mới nếu năng lượng đủ
    private void reproduce(Organism[][] map) {
        List<int[]> validMoves = detect(map).get("ValidMoves");
        if (!validMoves.isEmpty()) {
            int[] pos = validMoves.get(0); // Chọn vị trí trống đầu tiên
            map[pos[0]][pos[1]] = new Carnivore(pos[0], pos[1], this.energy / 2); // Chia sẻ năng lượng
            this.energy /= 2; // Giảm năng lượng sau khi sinh sản
        }
    }

    // Ăn Herbivore tại vị trí hiện tại
    private void consumeHerbivore(Herbivore herbivore) {
        this.energy += herbivore.getEnergy(); // Nhận năng lượng từ Herbivore
        //herbivore.die(); // Herbivore bị loại bỏ khỏi bản đồ
    }

    // Truy đuổi Herbivore
    private void chaseHerbivore(Organism[][] map, int targetX, int targetY) {
        if (isInBounds(targetX, targetY, map) && map[targetX][targetY] instanceof Herbivore) {
            map[this.xPos][this.yPos] = null; // Xóa vị trí cũ
            this.xPos = targetX;
            this.yPos = targetY;
            consumeHerbivore((Herbivore) map[targetX][targetY]); // Ăn con mồi
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
        this.energy -= ENERGY_LOSS_PER_TURN;
        if (this.energy <= 0) {
            die();
        }
    }

    // Phương thức để đánh dấu rằng sinh vật đã chết
    private void die() {
        // Có thể được mở rộng nếu cần
    }
}
