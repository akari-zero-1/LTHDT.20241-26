package src.ecosystem.test;

import src.ecosystem.environment.Environment;
import src.ecosystem.organism.Carnivore;
import src.ecosystem.organism.Herbivore;
import src.ecosystem.organism.Plant;

import java.util.Random;

public class TestCarnivore {
    public static void main(String[] args) {
        // 1. Tạo môi trường 10x10
        Environment environment = new Environment(10, 10);

        // 2. Thêm một số Plant vào môi trường
        int numPlants = 10; // Số lượng cây cần thêm
        Random random = new Random();
        for (int i = 0; i < numPlants; i++) {
            int x = random.nextInt(10); // Xác định vị trí ngẫu nhiên (0-9)
            int y = random.nextInt(10);
            Plant plant = new Plant(50, x, y); // Tạo cây với năng lượng 50
            environment.addOrganism(plant, x, y);
        }

        // 3. Thêm một số Herbivore vào môi trường
        Herbivore herbivore1 = new Herbivore(100, 2, 2);
        Herbivore herbivore2 = new Herbivore(100, 7, 7);
        Herbivore herbivore3 = new Herbivore(100, 5, 3);

        environment.addOrganism(herbivore1, 2, 2);
        environment.addOrganism(herbivore2, 7, 7);
        environment.addOrganism(herbivore3, 5, 3);

        // 4. Thêm Carnivore vào môi trường
        Carnivore carnivore = new Carnivore(100, 0, 0);
        environment.addOrganism(carnivore, 0, 0);

        // 5. In trạng thái ban đầu
        System.out.println("🌱 Trạng thái ban đầu:");
        environment.displayGrid();
        environment.countOrganisms();

        // 6. Mô phỏng 20 bước
        for (int i = 0; i < 20; i++) {
            System.out.println("\nBước " + (i + 1) + ":");
            environment.simulateStep();
            environment.displayGrid();

            // Đếm và hiển thị số lượng sinh vật trong môi trường
            environment.countOrganisms();

            // Kiểm tra xem Carnivore đã ăn Herbivore hay chưa
            if (carnivore.getEnergy() > 100) {
                System.out.println("✅ Carnivore đã ăn một Herbivore và hiện có năng lượng: " + carnivore.getEnergy());
            }

            // Kiểm tra nếu Carnivore đã tái tạo
            if (carnivore.getEnergy() > 150) {
                System.out.println("🌱 Carnivore đã vượt ngưỡng năng lượng và có thể sinh sản.");
            }
        }
    }
}
