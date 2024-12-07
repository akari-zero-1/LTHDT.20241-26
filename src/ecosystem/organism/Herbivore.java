package src.ecosystem.organism;
    import src.ecosystem.environment.Environment;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;


public class Herbivore extends Animal {
    public static final int  maxEnergy =100;
    public Herbivore(int energy, int xPos, int yPos ){
        super(energy, xPos, yPos);
    }


    public void eat_Plant (Organism [][]map , int currentX , int currentY, int plantX, int plantY) {
        if (map[plantX][plantY] instanceof Plant) {// kiem tra xem o muc tiep co phai la plant khong
            Plant plant =(Plant) map[plantX][plantY];
            this.energy += (int)(plant.getEnergy()*0.1); // lay 10% nang luong cua plant
            plant.setEnergy(0);
        }    
        map[currentX][currentY] = null;// herbivore bi loai ra khoi map

        map[plantX][plantY] =null; // plant bi loai ra khoi map

        map[plantX][plantY] = new Herbivore(this.energy, plantX, plantY); // cap nhat vi tri moi cua herbivore
        
    }
@Override
public void move ( Organism[][] map){
    int gridWidth = map.length;
    int gridHeight = map[0].length;
    double closes_distance = Integer.MAX_VALUE;
    int targetX = -1;
    int targetY = -1;
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
       
        return;
     }else{
        lose_energy();
        super.move(map);
        
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
                    int energy_new= this.energy/2;
                    Herbivore offspring = new Herbivore(energy_new,newX,newY);
                    environment.addOrganism(offspring, newX,newY);
                    this.energy = this.energy -energy_new;// nang luong cua me giam di 1 nua
                    break;
                }
            }
        }

        
        if (!emptyPositions.isEmpty()) {
            Random rand = new Random();
            int[] chosenPosition = emptyPositions.get(rand.nextInt(emptyPositions.size()));
            int energy_new =this.energy/2;
            Herbivore offspring = new Herbivore(energy_new, chosenPosition[0], chosenPosition[1]); 
            this.energy = this.energy -energy_new;// nang luong cua me giam di 1 nua     
            environment.addOrganism(offspring, chosenPosition[0], chosenPosition[1]);

        }
    }
}
 
    public  void lose_energy(){
        this.energy -= 5;
    }


 @Override
    public int getMoveSpeed() {
        return 2;//
    }
}