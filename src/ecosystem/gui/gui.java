package src.ecosystem.gui;

import src.ecosystem.environment.Environment;
import src.ecosystem.organism.Animal;
import src.ecosystem.organism.Organism;
import src.ecosystem.organism.Plant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcosystemGUI extends JFrame {
    private Environment environment; // Môi trường mô phỏng
    private JPanel gridPanel; // Bảng lưới hiển thị môi trường
    private Timer timer; // Bộ hẹn giờ để chạy mô phỏng tự động

    private static final int CELL_SIZE = 30; // Kích thước ô trên lưới

    public EcosystemGUI(Environment environment) {
        this.environment = environment;
        setTitle("Ecosystem Simulation");
        setSize(environment.getWidth() * CELL_SIZE, environment.getHeight() * CELL_SIZE + 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tạo giao diện
        initGUI();
        startSimulation();
    }

    private void initGUI() {
        // Panel hiển thị lưới
        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
            }
        };
        gridPanel.setPreferredSize(new Dimension(environment.getWidth() * CELL_SIZE, environment.getHeight() * CELL_SIZE));
        add(gridPanel, BorderLayout.CENTER);

        // Nút điều khiển
        JPanel controlPanel = new JPanel();
        JButton stepButton = new JButton("Next Step");
        stepButton.addActionListener(e -> {
            environment.simulateStep();
            gridPanel.repaint();
        });
        controlPanel.add(stepButton);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> startSimulation());
        controlPanel.add(startButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopSimulation());
        controlPanel.add(stopButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void drawGrid(Graphics g) {
        for (int x = 0; x < environment.getWidth(); x++) {
            for (int y = 0; y < environment.getHeight(); y++) {
                // Vẽ ô vuông
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                // Vẽ sinh vật
                Organism organism = environment.getOrganismAt(x, y);
                if (organism != null) {
                    if (organism instanceof Plant) {
                        g.setColor(Color.GREEN);
                    } else if (organism instanceof Animal) {
                        g.setColor(Color.RED);
                    }
                    g.fillRect(x * CELL_SIZE + 2, y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                }
            }
        }
    }

    private void startSimulation() {
        if (timer == null) {
            timer = new Timer(500, e -> {
                environment.simulateStep();
                gridPanel.repaint();
            });
        }
        timer.start();
    }

    private void stopSimulation() {
        if (timer != null) {
            timer.stop();
        }
    }

    public static void main(String[] args) {
        Environment environment = new Environment(20, 15);
        environment.populateRandomly(30, 10);

        SwingUtilities.invokeLater(() -> {
            EcosystemGUI gui = new EcosystemGUI(environment);
            gui.setVisible(true);
        });
    }
}
