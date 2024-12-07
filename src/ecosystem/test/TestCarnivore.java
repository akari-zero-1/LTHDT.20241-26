package src.ecosystem.test;

import src.ecosystem.environment.Environment;
import src.ecosystem.organism.Carnivore;
import src.ecosystem.organism.Herbivore;

public class TestCarnivore {
    public static void main(String[] args) {
        // 1. Tạo môi trường 10x10
        Environment environment = new Environment(10, 10);

        // 2. Thêm một số Herbivore vào môi trường
        Herbivore herbivore1 = new Herbivore(100, 2, 2);
        Herbivore herbivore2 = new Herbivore(100, 7, 7);
        Herbivore herbivore3 = new Herbivore(100, 5, 3);

        environment.addOrganism(herbivore1, 2, 2);
        environment.addOrganism(herbivore2, 7, 7);
        environment.addOrganism(herbivore3, 5, 3);

        // 3. Thêm Carnivore vào môi trường
        Carnivore carnivore = new Carnivore(100, 0, 0);
        environment.addOrganism(carnivore, 0, 0);

        // 4. In trạng thái ban đầu
        System.out.println("Trạng thái ban đầu:");
        environment.displayGrid();

        // 5. Mô phỏng 20 bước
        for (int i = 0; i < 20; i++) {
            System.out.println("\nBước " + (i + 1) + ":");
            environment.simulateStep();
            environment.displayGrid();

            // Kiểm tra xem Carnivore đã ăn Herbivore hay chưa
            if (carnivore.getEnergy() > 100) {
                System.out.println("Carnivore đã ăn một Herbivore và hiện có năng lượng: " + carnivore.getEnergy());
            }

            // Kiểm tra nếu Carnivore đã tái tạo
            if (carnivore.getEnergy() > 150) {
                System.out.println("Carnivore đã vượt ngưỡng năng lượng và có thể sinh sản.");
            }
        }
    }
}
