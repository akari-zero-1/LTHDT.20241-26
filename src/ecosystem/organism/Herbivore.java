package src.ecosystem.organism;


import java.util.List;
import java.util.Map;
import java.util.Random;

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
        int ateFood =0;
        
        Map<String, List<int[]>> detected = detect(map);

        // Kiểm tra nếu có động vật ăn thịt gần
        List<int[]> detectedCarnivores = detected.get("DetectedCarnivores");
        if (!detectedCarnivores.isEmpty()) {
            int[] escapePosition = findEscapePosition(detectedCarnivores, map);
            if (escapePosition != null) {
                moveToPosition(escapePosition[0], escapePosition[1], map);
                return;  
            }
        }

        // Kiểm tra nếu năng lượng đủ để sinh sản
        if (this.getEnergy() >= energyThresholdForReproduction) {
            reproduce(map);
            return;  
        }

        // Kiểm tra nếu có cây gần để ăn
        List<int[]> detectedPlants = detected.get("DetectedPlants");
        if (!detectedPlants.isEmpty()) {
            int[] plantPosition = detectedPlants.get(0);  
            eat_Plant(map, plantPosition[0], plantPosition[1]);
            ateFood =1;
            return;  
        }

       
        if (!detectedPlants.isEmpty()) {
            int[] plantPosition = detectedPlants.get(0);
            moveToPosition(plantPosition[0], plantPosition[1], map);
            return; 
        }

        moveRandomly(map);
        // nếu không ăn thì bị giảm năng lượng
        if (ateFood ==0) {
            this.setEnergy(this.energy -energyDecay);  
        }
    }

    // Tìm vị trí để chạy trốn khỏi động vật ăn thịt
    private int[] findEscapePosition(List<int[]> detectedCarnivores, Organism[][] map) {
        int closestDistance = Integer.MAX_VALUE;
        int[] escapePosition = null;
        for (int[] carnivorePos : detectedCarnivores) {
            int distance = calculateDistance(carnivorePos[0], carnivorePos[1]);
            if (distance < closestDistance) {
                closestDistance = distance;
                escapePosition = findFartherPosition(map, carnivorePos[0], carnivorePos[1]);
            }
        }
        return escapePosition;
    }

    // Tính khoảng cách 
    private int calculateDistance(int targetX, int targetY) {
        return (int) Math.sqrt(Math.pow(this.getxPos() - targetX, 2) + Math.pow(this.getyPos() - targetY, 2));
    }

    // chạy trốn
    private int[] findFartherPosition(Organism[][] map, int targetX, int targetY) {
        int gridWidth = map.length;
        int gridHeight = map[0].length;
        int[] bestPosition = null;
        int maxDistance = -1;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                int newX = this.getxPos() + dx;
                int newY = this.getyPos() + dy;
                if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight && map[newX][newY] == null) {
                    int distance = calculateDistance(targetX, targetY);
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        bestPosition = new int[] { newX, newY };
                    }
                }
            }
        }
        return bestPosition;
    }


    private void moveToPosition(int targetX, int targetY, Organism[][] map) {
        map[this.getxPos()][this.getyPos()] = null; 
        this.setxPos(targetX);
        this.setyPos(targetY);
        map[targetX][targetY] = this;  
    }

    // Di chuyển ngẫu nhiên nếu không có hành động ưu tiên nào
    private void moveRandomly(Organism[][] map) {
        Random rand = new Random();
        int newX = rand.nextInt(map.length);
        int newY = rand.nextInt(map[0].length);
        while (map[newX][newY] != null) {
            newX = rand.nextInt(map.length);
            newY = rand.nextInt(map[0].length);
        }
        moveToPosition(newX, newY, map);
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
