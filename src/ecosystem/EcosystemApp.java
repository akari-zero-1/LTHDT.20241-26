package src.ecosystem;

import src.ecosystem.environment.Environment;
import src.ecosystem.gui.EcosystemGUI;
import src.ecosystem.organism.*;

import javax.swing.*;
import java.util.Random;

public class EcosystemApp {
    public static void main(String[] args) {
        // Tạo môi trường
        int width = 10; // Kích thước lưới
        int height = 10;
        Environment environment = new Environment(width, height);

        // Thêm sinh vật ngẫu nhiên vào môi trường
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (random.nextBoolean()) {
                environment.addOrganism(new Plant(10), x, y);
            } else {
                environment.addOrganism(new Sheep(20), x, y);
            }
        }

        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            environment.addOrganism(new Wolf(30), x, y);
        }

        // Tạo GUI
        JFrame frame = new JFrame("Ecosystem Simulator");
        EcosystemGUI gui = new EcosystemGUI(environment);
        frame.add(gui);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Vòng lặp cập nhật giao diện
        new Timer(500, e -> {
            environment.update(); // Cập nhật trạng thái
            gui.refresh();        // Làm mới giao diện
        }).start();
    }
}
