package src.ecosystem.organism;
    import src.ecosystem.environment.Environment;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;


    

public class Herbivore extends Animal {
    public Herbivore(int energy, int xPos, int yPos ){
        super(energy, xPos, yPos);
    }

    // ear_plant
    public void eat_Plant (Organism [][]map , int currentX , int currentY, int plantX, int plantY) {
        if (map[plantX][plantY] instanceof Plant) {// kiem tra xem o muc tiep co phai la plant khong
            Plant plant =(Plant) map[plantX][plantY];
            this.energy += (int)(plant.getEnergy()*0.1); // lay 10% nang luong cua plant

        }    
        map[currentX][currentY] = null;// herbivore bi loai ra khoi map

        map[plantX][plantY] =null; // plant bi loai ra khoi map

        map[plantX][plantY] = new Herbivore(this.energy, plantX, plantY); // cap nhat vi tri moi cua herbivore
        
    }
// move
public void move_herbivore ( Organism[][] map,Environment environment){
    int gridWidth = map.length;
    int gridHeight = map[0].length;
    int randomMoves = 0;
    
    double closes_distance = Integer.MAX_VALUE;
    int targetX = -1;
    int targetY = -1;
    while (randomMoves<3) {
        
     for(int dx =-2;dx <=2;dx++){
        for(int dy =-2;dy<=2;dy++){
            int newX =this.xPos +dx;
            int newY =this.yPos +dy;
            if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
                double current_distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                if(map [newX][newY] instanceof Plant){
                    if (current_distance<closes_distance) {
                        closes_distance =current_distance;
                        targetX =newX;
                        targetY =newY;
                    }
                }
                
            }
        }
     }
     if (targetX != -1 && targetY !=-1){
        eat_Plant(map, this.xPos, this.yPos, targetX, targetY);
        // ăn xong kiểm tra đã đủ đk để sinh sản chưa
       // reproduce(map, 100); 
        return;
     }else{
        // neu trong pham vi khong co plant nao thi 
        List<int[]> availPositions = new ArrayList<>();
        for(int dx =-1;dx<=1;dx++){
            for(int dy =-1;dy<=1;dy++){
            if (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) == 1) { 
                int newX = this.xPos + dx;
                int newY = this.yPos + dy;
                if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
                    availPositions.add(new int[] {newX, newY});
                }
            }
               
                
            }
        }
        if (!availPositions.isEmpty()) {
            Random rand = new Random();
            int[] randomPosition = availPositions.get(rand.nextInt(availPositions.size()));
            moveRandomly(map, randomPosition[0], randomPosition[1]);
                    }
                    randomMoves++; 

            
            if (randomMoves >= 3) {
                this.energy =0;
                environment.cleanUpDeadOrganisms();
                return;
            }
     } 
    }
}         
 
private void moveRandomly(Organism[][] map, int targetX, int targetY) {
    int gridWidth = map.length;
    int gridHeight = map[0].length;

    List<int[]> availablePositions = new ArrayList<>();
    for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
            if (Math.sqrt(dx * dx + dy * dy) == 1) { 
                int newX = this.xPos + dx;
                int newY = this.yPos + dy;
                if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight && map[newX][newY] == null) {
                    availablePositions.add(new int[] { newX, newY });
                }
            }
        }
    }

    if (!availablePositions.isEmpty()) {
        Random rand = new Random();
        int[] randomPosition = availablePositions.get(rand.nextInt(availablePositions.size()));
        map[this.xPos][this.yPos] = null; // Xóa vị trí hiện tại
        this.xPos = randomPosition[0];
        this.yPos = randomPosition[1];
        map[this.xPos][this.yPos] = this; // Đặt động vật tại vị trí mới

    } 
}
public void reproduce(Organism[][] map, int maxEnergy, Environment environment) {
    
    if (this.energy > maxEnergy) {
        int gridWidth = map.length;
        int gridHeight = map[0].length;

        List<int[]> emptyPositions = new ArrayList<>();

        
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; 

                int newX = this.xPos + dx;
                int newY = this.yPos + dy;

                if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight && map[newX][newY] == null) {
                    emptyPositions.add(new int[] { newX, newY });
                }
            }
        }

        
        if (!emptyPositions.isEmpty()) {
            Random rand = new Random();
            int[] chosenPosition = emptyPositions.get(rand.nextInt(emptyPositions.size()));
            this.energy /= 2;
            Herbivore offspring = new Herbivore(this.energy, chosenPosition[0], chosenPosition[1]);          
            environment.addOrganism(offspring, chosenPosition[0], chosenPosition[1]);

        }
    }
}
 



 @Override
    public int getMoveSpeed() {
        
        throw new UnsupportedOperationException();
    }
}
