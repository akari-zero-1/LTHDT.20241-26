package src.ecosystem.environment;

import src.ecosystem.organism.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {
    private int width; // Chiều rộng lưới
    private int height; // Chiều cao lưới
    private double plantSpawnRate; // tỉ lệ sinh cỏ

    private Organism[][] grid; // Lưới chứa sinh vật
    private List<Organism> organisms; // Danh sách tất cả sinh vật
    private int timeStep; // Số bước thời gian đã trôi qua

    private int plantCount;
    private int herbivoreCount;
    private int carnivoreCount;

    public Environment(int width, int height, double plantSpawnRate) {
        this.width = width;
        this.height = height;
        this.plantSpawnRate = plantSpawnRate;
        this.grid = new Organism[width][height];
        this.organisms = new ArrayList<>();
        this.timeStep = 0;

        this.plantCount = 0;
        this.herbivoreCount = 0;
        this.carnivoreCount = 0;
    }

    public int getPlantCount() {
        return plantCount;
    }

    public int getHerbivoreCount() {
        return herbivoreCount;
    }

    public int getCarnivoreCount() {
        return carnivoreCount;
    }

    public void addOrganism(Organism organism, int x, int y) {
        if (isValidCell(x, y) && grid[x][y] == null) {
            grid[x][y] = organism;
            organism.setxPos(x);
            organism.setyPos(y);
            organisms.add(organism);

            if (organism instanceof Plant) {
                plantCount++;
            } else if (organism instanceof Herbivore) {
                herbivoreCount++;
            } else if (organism instanceof Carnivore) {
                carnivoreCount++;
            }
        }
    }

    public void simulateStep() {
        timeStep++;
        System.out.println("Time Step: " + timeStep);
        this.organisms = new ArrayList<>();
        List<Organism> actedOrganisms = new ArrayList<>();
        // Iterate over the 2D grid
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                Organism organism = grid[x][y];
                if (organism != null && organism.isAlive() && !actedOrganisms.contains(organism)) {
                    organism.act(grid, organisms);
                    actedOrganisms.add(organism);
                } else if (organism == null) {
                    if (Math.random() < plantSpawnRate) {
                        Plant newPlant = new Plant(x, y);
                        grid[x][y] = newPlant;
                        organisms.add(newPlant);
                    }
                }

                // Check and remove dead organisms
                if (organism != null && !organism.isAlive()) {
                    grid[x][y] = null; // Remove dead organism from the grid
                }
            }
        }

        countOrganisms();
        System.out.println("-----------------------------------");
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
        System.out.println("Plant: " + plantCount + "\nHerbivor: " + herbivoreCount + "\nCarnivore: " + carnivoreCount);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[j][i] == null) {
                    System.out.print(". ");
                } else if (grid[j][i] instanceof Plant) {
                    System.out.print("P ");
                } else if (grid[j][i] instanceof Herbivore) {
                    System.out.print("H "); // Hiển thị Herbivore là "H"
                } else if (grid[j][i] instanceof Carnivore) {
                    System.out.print("C "); // Hiển thị Carnivore là "C"
                } else {
                    System.out.print("? "); // Dự phòng nếu có lớp mới không được xác định
                }
            }
            System.out.println();
        }
    }

    public void populateRandomly(int numPlants, int numCarnivore, int numHerbivore) {
        Random random = new Random();

        // Thêm cây ngẫu nhiên
        for (int i = 0; i < numPlants; i++) {
            int x, y;
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
            } while (grid[x][y] != null);

            addOrganism(new Plant(x, y), x, y);
        }

        // Thêm động vật ăn thịt
        for (int i = 0; i < numCarnivore; i++) {
            int x, y;
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
            } while (grid[x][y] != null);

            addOrganism(new Carnivore(x, y), x, y);
        }

        for (int i = 0; i < numHerbivore; i++) {
            int x, y;
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
            } while (grid[x][y] != null);

            addOrganism(new Herbivore(x, y), x, y);
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

    public void countOrganisms() {
        int plantCount = 0;
        int herbivoreCount = 0;
        int carnivoreCount = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[j][i] instanceof Plant) {
                    plantCount++;
                } else if (grid[j][i] instanceof Herbivore) {
                    herbivoreCount++;
                } else if (grid[j][i] instanceof Carnivore) {
                    carnivoreCount++;
                }
            }
        }

        System.out.printf("Numbers of organism - Plants: %d, Herbivores: %d, Carnivores: %d%n", plantCount,
                herbivoreCount, carnivoreCount);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Organism[][] getGrid() {
        return grid;
    }

    public List<Organism> getOrganisms() {
        return organisms;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setGrid(Organism[][] grid) {
        this.grid = grid;
    }

    public void setOrganisms(List<Organism> organisms) {
        this.organisms = organisms;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }

    @Override
    public String toString() {
        return "Environment [width=" + width + ", height=" + height + ", plantSpawnRate=" + plantSpawnRate
                + ", timeStep=" + timeStep + ", plantCount="
                + plantCount + ", herbivoreCount=" + herbivoreCount + ", carnivoreCount=" + carnivoreCount + "]";
    }

}
