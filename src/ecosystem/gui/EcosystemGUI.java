package src.ecosystem.gui;

import src.ecosystem.environment.*;
import src.ecosystem.organism.*;

import javax.swing.*;
import java.awt.*;

public class EcosystemGUI extends JPanel {
    private static final int CELL_SIZE = 64; // Kích thước mỗi ô lưới
    private final Environment environment;

    // Hình ảnh đại diện
    private Image grassImage, emptyImage, sheepImage, wolfImage;

    public EcosystemGUI(Environment environment) {
        this.environment = environment;
        loadImages(); // Tải hình ảnh từ thư mục resources
        setPreferredSize(new Dimension(environment.getWidth() * CELL_SIZE, environment.getHeight() * CELL_SIZE));
    }

    private void loadImages() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        grassImage = toolkit.getImage("resources/grass.png");
        emptyImage = toolkit.getImage("resources/empty.png");
        sheepImage = toolkit.getImage("resources/sheep.png");
        wolfImage = toolkit.getImage("resources/wolf.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g); // Vẽ hệ sinh thái
    }

    private void drawGrid(Graphics g) {
        for (int x = 0; x < environment.getWidth(); x++) {
            for (int y = 0; y < environment.getHeight(); y++) {
                Organism organism = environment.getOrganismAt(x, y);

                // Xác định hình ảnh cần vẽ
                if (organism == null) {
                    g.drawImage(emptyImage, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE, this);
                } else if (organism instanceof Plant) {
                    g.drawImage(grassImage, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE, this);
                } else if (organism instanceof Animal) {
                    if (organism.getClass().getSimpleName().equals("Sheep")) {
                        g.drawImage(sheepImage, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE, this);
                    } else if (organism.getClass().getSimpleName().equals("Wolf")) {
                        g.drawImage(wolfImage, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE, this);
                    }
                }
            }
        }
    }

    public void refresh() {
        repaint(); // Cập nhật lại toàn bộ giao diện
    }
}
