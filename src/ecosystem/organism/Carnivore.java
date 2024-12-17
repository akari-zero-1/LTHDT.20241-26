package src.ecosystem.organism;

import java.util.List;
import java.util.Map;

public class Carnivore extends Animal {
    // Các thông số của Carnivore
    private static int moveSpeed = 1;
    private static int visionRange = 6;
    private static int energyThresholdForReproduction = 200;
    private static int energyDecay = 4;

    public Carnivore(int energy, int xPos, int yPos) {
        super(energy, xPos, yPos);
    }

    public Carnivore(int energy) {
        super(energy);
    }

    public void act(Organism[][] map, List<Organism> organisms) {
        Map<String, List<int[]>> detected = detect(map);
        List<int[]> detectedHerbivores = detected.get("DetectedHerbivores");
        List<int[]> validMoves = detected.get("ValidMoves");
        //System.out.println("Carnivore"+detected);

        // Săn mồi
        if (!detectedHerbivores.isEmpty()) {
            int[] preyPos = detectedHerbivores.get(0); // Lấy tọa độ của con mồi đầu tiên
            int[] closestMove = findClosestLocation(map[preyPos[0]][preyPos[1]], validMoves);

            if (closestMove != null) {
                moveTo(closestMove[0], closestMove[1], map);
                System.out.println("Carnivore - Moving to " + closestMove[0] + ", " + closestMove[1]);

                // Nếu đến được vị trí con mồi, ăn con mồi
                if (Math.abs(this.getxPos() - preyPos[0]) <= 1 && Math.abs(this.getyPos() - preyPos[1]) <= 1) {
                    eat(map[preyPos[0]][preyPos[1]], map, organisms);
                    System.out.println("Carnivore - Eating Herbivore at " + preyPos[0] + ", " + preyPos[1]);
                    return;
                }                
            }
        }

        // Sinh sản
        if (this.getEnergy() >= energyThresholdForReproduction) {
            System.err.println("Reproduce");
            reproduce(map);
            return;
        }

        // Di chuyển ngẫu nhiên
        if (!validMoves.isEmpty()) {
            int[] randomMove = validMoves.get((int) (Math.random() * validMoves.size()));
            moveTo(randomMove[0], randomMove[1], map);
            System.out.println("Carnivore Random Move to " + randomMove[0] + ", " + randomMove[1]);
        }

        // Mất năng lượng do hoạt động
        loseEnergy();
    }

    private void loseEnergy() {
        this.energy -= energyDecay; // Mỗi lượt, năng lượng giảm đi một giá trị cố định
    }

    // Getter và Setter cho các thuộc tính của Carnivore
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

    @Override
    public int getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    public int getVisionRange() {
        return visionRange;
    }

    public void setEnergyThresholdForReproduction(int energyThresholdForReproduction) {
        Carnivore.energyThresholdForReproduction = energyThresholdForReproduction;
    }

    @Override
    public int getEnergyThresholdForReproduction() {
        return energyThresholdForReproduction;
    }
}
