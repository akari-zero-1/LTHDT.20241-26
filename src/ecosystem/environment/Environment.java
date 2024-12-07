package src.ecosystem.environment;
import src.ecosystem.organism.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {
    private int width; // Chiều rộng lướia
    private int height; // Chiều cao lưới
    private Organism[][] grid; // Lưới chứa sinh vật
    private List<Organism> organisms; // Danh sách tất cả sinh vật
    private int timeStep; // Số bước thời gian đã trôi qua

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Organism[width][height];
        this.organisms = new ArrayList<>();
        this.timeStep = 0;
    }

    public void addOrganism(Organism organism, int x, int y) {
        if (isValidCell(x, y) && grid[x][y] == null) {
            grid[x][y] = organism;
            organism.setxPos(x);
            organism.setyPos(y);
            organisms.add(organism);
        }
    }

    public void simulateStep() {
        timeStep++;
        List<Organism> newOrganisms = new ArrayList<>();

        // Cho tất cả sinh vật hành động
        for (Organism organism : organisms) {
            if (organism instanceof Animal) {
                ((Animal) organism).move(grid);
            } else if (organism instanceof Plant) {
                ((Plant) organism).act();
                ((Plant) organism).reproduce(grid);
            }
        }

        // Loại bỏ sinh vật đã chết
        cleanUpDeadOrganisms();
    }

    private void cleanUpDeadOrganisms() {
        List<Organism> toRemove = new ArrayList<>();
        for (Organism organism : organisms) {
            if (organism.getEnergy() <= 0) {
                grid[organism.getxPos()][organism.getyPos()] = null;
                toRemove.add(organism);
            }
        }
        organisms.removeAll(toRemove);
    }

    public Organism getOrganismAt(int x, int y) {
        if (isValidCell(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    public boolean isValidCell(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void displayGrid() {
        System.out.println("Step: " + timeStep);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[j][i] == null) {
                    System.out.print(". ");
                } else if (grid[j][i] instanceof Plant) {
                    System.out.print("P ");
                } else if (grid[j][i] instanceof Animal) {
                    System.out.print("A ");
                }
            }
            System.out.println();
        }
    }

    public void populateRandomly(int numPlants, int numAnimals) {
        Random random = new Random();

        // Thêm cây ngẫu nhiên
        for (int i = 0; i < numPlants; i++) {
            int x, y;
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
            } while (grid[x][y] != null);

            addOrganism(new Plant(20, x, y), x, y);
        }

        // Thêm động vật ngẫu nhiên
        for (int i = 0; i < numAnimals; i++) {
            int x, y;
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
            } while (grid[x][y] != null);
        }
    }
    public List<Plant> getAllProducers() {
        List<Plant> producers = new ArrayList<>();
        for (Organism organism : organisms) {
            if (organism instanceof Plant) {
                producers.add((Plant) organism);
            }
        }
        return producers;
    }

    public List<Herbivore> getAllHerbivores() {
        List<Herbivore> herbivores = new ArrayList<>();
        for (Organism organism : organisms) {
            if (organism instanceof Herbivore) {
                herbivores.add((Herbivore) organism);
            }
        }
        return herbivores;
    }

    public List<Carnivore> getAllCarnivores() {
        List<Carnivore> carnivores = new ArrayList<>();
        for (Organism organism : organisms) {
            if (organism instanceof Carnivore) {
                carnivores.add((Carnivore) organism);
            }
        }
        return carnivores;
    }
}
