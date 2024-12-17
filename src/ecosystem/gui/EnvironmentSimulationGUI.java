package src.ecosystem.gui;
import src.ecosystem.environment.Environment;
import src.ecosystem.organism.Carnivore;
import src.ecosystem.organism.Herbivore;
import src.ecosystem.organism.Plant;
import src.ecosystem.organism.Organism;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnvironmentSimulationGUI extends JFrame {
    private JTextField gridWidthField;
    private JTextField gridHeightField;
    private JTextField plantCountField;
    private JTextField herbivoreCountField;
    private JTextField carnivoreCountField;
    private JButton startButton;
    private SimulationPanel simulationPanel;
    private Environment environment;

    public EnvironmentSimulationGUI() {
        setTitle("Environment Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Top panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 6));
        inputPanel.add(new JLabel("Grid Width:"));
        gridWidthField = new JTextField("10");
        inputPanel.add(gridWidthField);

        inputPanel.add(new JLabel("Grid Height:"));
        gridHeightField = new JTextField("10");
        inputPanel.add(gridHeightField);

        inputPanel.add(new JLabel("Plants:"));
        plantCountField = new JTextField("5");
        inputPanel.add(plantCountField);

        inputPanel.add(new JLabel("Herbivores:"));
        herbivoreCountField = new JTextField("3");
        inputPanel.add(herbivoreCountField);

        inputPanel.add(new JLabel("Carnivores:"));
        carnivoreCountField = new JTextField("2");
        inputPanel.add(carnivoreCountField);

        startButton = new JButton("Start Simulation");
        inputPanel.add(startButton);

        add(inputPanel, BorderLayout.NORTH);

        // Simulation panel for grid display
        simulationPanel = new SimulationPanel();
        add(simulationPanel, BorderLayout.CENTER);

        // Action listener for start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });
    }

    private void startSimulation() {
        try {
            int gridWidth = Integer.parseInt(gridWidthField.getText());
            int gridHeight = Integer.parseInt(gridHeightField.getText());
            int plantCount = Integer.parseInt(plantCountField.getText());
            int herbivoreCount = Integer.parseInt(herbivoreCountField.getText());
            int carnivoreCount = Integer.parseInt(carnivoreCountField.getText());

            environment = new Environment(gridWidth, gridHeight);
            environment.populateRandomly(plantCount, herbivoreCount, carnivoreCount, 20);
            simulationPanel.setEnvironment(environment);
            simulationPanel.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EnvironmentSimulationGUI gui = new EnvironmentSimulationGUI();
            gui.setVisible(true);
        });
    }
}

class SimulationPanel extends JPanel {
    private Environment environment;

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (environment == null) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Tính tỷ lệ sao cho lưới vừa khít cả chiều rộng và chiều cao
        int cellWidth = panelWidth / environment.getWidth();
        int cellHeight = panelHeight / environment.getHeight();

        // Lấy tỷ lệ nhỏ hơn để đảm bảo hình vuông
        int cellSize = Math.min(cellWidth, cellHeight);

        // Tính offset để căn lưới vào giữa màn hình nếu còn dư khoảng trống
        int offsetX = (panelWidth - cellSize * environment.getWidth()) / 2;
        int offsetY = (panelHeight - cellSize * environment.getHeight()) / 2;

        // Draw grid and organisms
        for (int x = 0; x < environment.getWidth(); x++) {
            for (int y = 0; y < environment.getHeight(); y++) {
                Organism organism = environment.getOrganismAt(x, y);
                if (organism instanceof Plant) {
                    g.setColor(Color.GREEN);
                } else if (organism instanceof Herbivore) {
                    g.setColor(Color.BLUE);
                } else if (organism instanceof Carnivore) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }

                // Vẽ từng ô lưới
                int drawX = offsetX + x * cellSize;
                int drawY = offsetY + y * cellSize;
                g.fillRect(drawX, drawY, cellSize, cellSize);

                // Vẽ viền lưới
                g.setColor(Color.BLACK);
                g.drawRect(drawX, drawY, cellSize, cellSize);
            }
        }
    }
}