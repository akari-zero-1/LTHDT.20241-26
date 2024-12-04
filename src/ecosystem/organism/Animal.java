package src.ecosystem.organism;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Animal extends Organism {
    public static int energyDecay = 10;

    public Animal(int energy, int xPos, int yPos) {
        super(energy, xPos, yPos);
    }

    public abstract int getMoveSpeed();

    public void move(Organism[][] map) {
        int gridWidth = map.length;
        int gridHeight = map[0].length;
        int moveSpeed = getMoveSpeed();

        List<int[]> validMoves = new ArrayList<>();

        //Duyệt qua các ô trong khoảng moveSpeed
        for (int dx = -moveSpeed; dx <= moveSpeed; dx++) {
            for (int dy = -moveSpeed; dy <= moveSpeed; dy++) {
                int newX = this.xPos + dx;
                int newY = this.yPos + dy;

                // Kiểm tra ô có nằm trong lưới và hợp lệ
                if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
                    if (map[newX][newY] == null) { // Ô trống
                        validMoves.add(new int[] { newX, newY });
                    }
                }
            }
        }

        if (!validMoves.isEmpty()) {
            Random random = new Random();
            int[] selectedMove = validMoves.get(random.nextInt(validMoves.size()));

            // Di chuyển đến ô mới
            map[this.xPos][this.yPos] = null;
            this.xPos = selectedMove[0];
            this.yPos = selectedMove[1];
            map[this.xPos][this.yPos] = this;
            this.energy -= energyDecay;
        }

    }
}
