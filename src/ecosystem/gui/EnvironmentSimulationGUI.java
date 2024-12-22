package src.ecosystem.gui;

import src.ecosystem.environment.Environment;
import src.ecosystem.organism.Carnivore;
import src.ecosystem.organism.Herbivore;
import src.ecosystem.organism.Plant;
import src.ecosystem.organism.Organism;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentSimulationGUI extends JFrame {
    private SimulationPanel simulationPanel;
    private Environment environment;
    private Thread simulationThread;
    private boolean isRunning = false;

    public EnvironmentSimulationGUI() {
        setTitle("Environment Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
        setLayout(new BorderLayout());

        simulationPanel = new SimulationPanel();
        add(simulationPanel, BorderLayout.CENTER);

        // Hiển thị cửa sổ chọn cấu hình
        showConfigurationDialog();
    }

    private void showConfigurationDialog() {
        JDialog configDialog = new JDialog(this, "Select Configuration", true);
        configDialog.setSize(400, 300);
        configDialog.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton balancedButton = new JButton("Balanced Ecosystem");
        JButton overpopulationButton = new JButton("Overpopulation");
        JButton extinctionButton = new JButton("Extinction");

        balancedButton.addActionListener(e -> {
            setupBalancedConfiguration();
            configDialog.dispose();
            showSetupDialog("30", "30", "0.01", "50", "30", "5", "2", "5", "200", "5", "200", "2", "5", "150", "4", "150");
        });

        overpopulationButton.addActionListener(e -> {
            setupOverpopulationConfiguration();
            configDialog.dispose();
            showSetupDialog("30", "30", "0.01", "50", "30", "20", "2", "5", "200", "5", "200", "2", "5", "150", "4", "150");
        });

        extinctionButton.addActionListener(e -> {
            setupExtinctionConfiguration();
            configDialog.dispose();
            showSetupDialog("30", "30", "0.0005", "20", "30", "5", "2", "5", "200", "5", "200", "2", "5", "150", "4", "150");
        });

        buttonPanel.add(balancedButton);
        buttonPanel.add(overpopulationButton);
        buttonPanel.add(extinctionButton);

        configDialog.add(new JLabel("Select a predefined ecosystem configuration:", SwingConstants.CENTER), BorderLayout.NORTH);
        configDialog.add(buttonPanel, BorderLayout.CENTER);

        // Add Quit and Help Buttons
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());

        JButton helpButton = new JButton("Help");
        JButton quitButton = new JButton("Quit");

        helpButton.addActionListener(e -> showHelpDialog());
        quitButton.addActionListener(e -> confirmQuit());

        actionPanel.add(helpButton);
        actionPanel.add(quitButton);

        configDialog.add(actionPanel, BorderLayout.SOUTH);
        configDialog.setVisible(true);
    }

    private void showSetupDialog(String gridWidth, String gridHeight, String plantSpawnRate, String plantCount, String herbivoreCount, String carnivoreCount, String carnivoreSpeed, String carnivoreRange, String carnivoreEnergyThreshold, String carnivoreEnergyDecay, String carnivoreDefaultEnergy, String herbivoreSpeed, String herbivoreRange, String herbivoreEnergyThreshold, String herbivoreEnergyDecay, String herbivoreDefaultEnergy) {
        JDialog setupDialog = new JDialog(this, "Simulation Setup", true);
        setupDialog.setSize(500, 600);
        setupDialog.setLayout(new BorderLayout());

        SimulationSetupPanel setupPanel = new SimulationSetupPanel();
        setupPanel.updateFields(gridWidth, gridHeight, plantSpawnRate, plantCount, herbivoreCount, carnivoreCount, carnivoreSpeed, carnivoreRange, carnivoreEnergyThreshold, carnivoreEnergyDecay, carnivoreDefaultEnergy, herbivoreSpeed, herbivoreRange, herbivoreEnergyThreshold, herbivoreEnergyDecay, herbivoreDefaultEnergy);
        JButton startButton = new JButton("Start Simulation");
        startButton.addActionListener((ActionEvent e) -> {
            try {
                setupEnvironment(setupPanel);
                setupDialog.dispose();
                startSimulation();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(setupDialog, "Please enter valid numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setupDialog.add(setupPanel, BorderLayout.CENTER);
        setupDialog.add(startButton, BorderLayout.SOUTH);
        setupDialog.setVisible(true);
    }

    private void setupBalancedConfiguration() {
        // Set up parameters for a balanced ecosystem
        System.out.println("Balanced ecosystem configuration selected.");
    }

    private void setupOverpopulationConfiguration() {
        // Set up parameters for overpopulation
        System.out.println("Overpopulation configuration selected.");
    }

    private void setupExtinctionConfiguration() {
        // Set up parameters for extinction
        System.out.println("Extinction configuration selected.");
    }

    private void setupEnvironment(SimulationSetupPanel setupPanel) {
        int gridWidth = setupPanel.getGridWidth();
        int gridHeight = setupPanel.getGridHeight();
        double plantSpawnRate = setupPanel.getPlantSpawnRate();
        int plantCount = setupPanel.getPlantCount();
        int herbivoreCount = setupPanel.getHerbivoreCount();
        int carnivoreCount = setupPanel.getCarnivoreCount();

        Carnivore.setMoveSpeed(setupPanel.getCarnivoreSpeed());
        Carnivore.setVisionRange(setupPanel.getCarnivoreRange());
        Carnivore.setEnergyThresholdForReproduction(setupPanel.getCarnivoreEnergyThreshold());
        Carnivore.setEnergyDecay(setupPanel.getCarnivoreEnergyDecay());
        Carnivore.setDefaultEnergy(setupPanel.getCarnivoreDefaultEnergy());

        Herbivore.setMoveSpeed(setupPanel.getHerbivoreSpeed());
        Herbivore.setVisionRange(setupPanel.getHerbivoreRange());
        Herbivore.setEnergyThresholdForReproduction(setupPanel.getHerbivoreEnergyThreshold());
        Herbivore.setEnergyDecay(setupPanel.getHerbivoreEnergyDecay());
        Herbivore.setDefaultEnergy(setupPanel.getHerbivoreDefaultEnergy());

        environment = new Environment(gridWidth, gridHeight, plantSpawnRate);
        environment.populateRandomly(plantCount, carnivoreCount, herbivoreCount);
        System.out.println(environment);
        simulationPanel.setEnvironment(environment);
    }

    private void startSimulation() {
        if (isRunning) return;
        isRunning = true;

        simulationThread = new Thread(() -> {
            while (isRunning) {
                if (environment == null) return;

                environment.simulateStep();
                simulationPanel.repaint();

                if (checkSimulationEnd()) break;

                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        simulationThread.start();
    }

    private boolean checkSimulationEnd() {
        if (environment.getPlantCount() == 0 && environment.getHerbivoreCount() == 0 && environment.getCarnivoreCount() == 0) {
            JOptionPane.showMessageDialog(this, "Simulation ended: No organisms left.", "Simulation Ended", JOptionPane.INFORMATION_MESSAGE);
            isRunning = false;
            return true;
        }
        return false;
    }

    private void showHelpDialog() {
        JOptionPane.showMessageDialog(this, "Simulation Help:\n" +
                "- Plants grow at a constant rate.\n" +
                "- Herbivores eat plants to gain energy.\n" +
                "- Carnivores hunt herbivores for energy.\n" +
                "- Adjust parameters to observe ecosystem dynamics.", "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void confirmQuit() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EnvironmentSimulationGUI().setVisible(true));
    }
}

class SimulationSetupPanel extends JPanel {
    private JTextField gridWidthField, gridHeightField, plantSpawnRateField;
    private JTextField plantCountField, herbivoreCountField, carnivoreCountField;
    private JTextField carnivoreSpeedField, carnivoreRangeField, carnivoreEnergyThresholdField;
    private JTextField carnivoreEnergyDecayField, carnivoreDefaultEnergyField;
    private JTextField herbivoreSpeedField, herbivoreRangeField, herbivoreEnergyThresholdField;
    private JTextField herbivoreEnergyDecayField, herbivoreDefaultEnergyField;

    public SimulationSetupPanel() {
        setLayout(new GridLayout(16, 2, 5, 5));

        gridWidthField = addField("Grid Width:", "30");
        gridHeightField = addField("Grid Height:", "30");
        plantSpawnRateField = addField("Plant Spawn Rate:", "0.01");
        plantCountField = addField("Initial Plants:", "50");
        herbivoreCountField = addField("Initial Herbivores:", "30");
        carnivoreCountField = addField("Initial Carnivores:", "5");

        carnivoreSpeedField = addField("Carnivore Speed:", "2");
        carnivoreRangeField = addField("Carnivore Range:", "5");
        carnivoreEnergyThresholdField = addField("Carnivore Energy Threshold:", "200");
        carnivoreEnergyDecayField = addField("Carnivore Energy Decay:", "5");
        carnivoreDefaultEnergyField = addField("Carnivore Default Energy:", "200");

        herbivoreSpeedField = addField("Herbivore Speed:", "2");
        herbivoreRangeField = addField("Herbivore Range:", "5");
        herbivoreEnergyThresholdField = addField("Herbivore Energy Threshold:", "150");
        herbivoreEnergyDecayField = addField("Herbivore Energy Decay:", "4");
        herbivoreDefaultEnergyField = addField("Herbivore Default Energy:", "150");
    }

    private JTextField addField(String label, String defaultValue) {
        add(new JLabel(label));
        JTextField field = new JTextField(defaultValue);
        add(field);
        return field;
    }

    public int getGridWidth() { return Integer.parseInt(gridWidthField.getText()); }
    public int getGridHeight() { return Integer.parseInt(gridHeightField.getText()); }
    public double getPlantSpawnRate() { return Double.parseDouble(plantSpawnRateField.getText()); }
    public int getPlantCount() { return Integer.parseInt(plantCountField.getText()); }
    public int getHerbivoreCount() { return Integer.parseInt(herbivoreCountField.getText()); }
    public int getCarnivoreCount() { return Integer.parseInt(carnivoreCountField.getText()); }
    public int getCarnivoreSpeed() { return Integer.parseInt(carnivoreSpeedField.getText()); }
    public int getCarnivoreRange() { return Integer.parseInt(carnivoreRangeField.getText()); }
    public int getCarnivoreEnergyThreshold() { return Integer.parseInt(carnivoreEnergyThresholdField.getText()); }
    public int getCarnivoreEnergyDecay() { return Integer.parseInt(carnivoreEnergyDecayField.getText()); }
    public int getCarnivoreDefaultEnergy() { return Integer.parseInt(carnivoreDefaultEnergyField.getText()); }
    public int getHerbivoreSpeed() { return Integer.parseInt(herbivoreSpeedField.getText()); }
    public int getHerbivoreRange() { return Integer.parseInt(herbivoreRangeField.getText()); }
    public int getHerbivoreEnergyThreshold() { return Integer.parseInt(herbivoreEnergyThresholdField.getText()); }
    public int getHerbivoreEnergyDecay() { return Integer.parseInt(herbivoreEnergyDecayField.getText()); }
    public int getHerbivoreDefaultEnergy() { return Integer.parseInt(herbivoreDefaultEnergyField.getText()); }
    

    public void updateFields(String gridWidth, String gridHeight, String plantSpawnRate, String plantCount, String herbivoreCount, String carnivoreCount, String carnivoreSpeed, String carnivoreRange, String carnivoreEnergyThreshold, String carnivoreEnergyDecay, String carnivoreDefaultEnergy, String herbivoreSpeed, String herbivoreRange, String herbivoreEnergyThreshold, String herbivoreEnergyDecay, String herbivoreDefaultEnergy) {
        gridWidthField.setText(gridWidth);
        gridHeightField.setText(gridHeight);
        plantSpawnRateField.setText(plantSpawnRate);
        plantCountField.setText(plantCount);
        herbivoreCountField.setText(herbivoreCount);
        carnivoreCountField.setText(carnivoreCount);
        carnivoreSpeedField.setText(carnivoreSpeed);
        carnivoreRangeField.setText(carnivoreRange);
        carnivoreEnergyThresholdField.setText(carnivoreEnergyThreshold);
        carnivoreEnergyDecayField.setText(carnivoreEnergyDecay);
        carnivoreDefaultEnergyField.setText(carnivoreDefaultEnergy);
        herbivoreSpeedField.setText(herbivoreSpeed);
        herbivoreRangeField.setText(herbivoreRange);
        herbivoreEnergyThresholdField.setText(herbivoreEnergyThreshold);
        herbivoreEnergyDecayField.setText(herbivoreEnergyDecay);
        herbivoreDefaultEnergyField.setText(herbivoreDefaultEnergy);
    }
}

class SimulationPanel extends JPanel {
    private Environment environment;
    private final Map<Class<? extends Organism>, BufferedImage> organismImages = new HashMap<>();
    private BufferedImage blankImage;

    public SimulationPanel() {
        loadOrganismImages();
    }

    private void loadOrganismImages() {
        try {
            organismImages.put(Plant.class, ImageIO.read(getClass().getResource("/resources/grass.png")));
            organismImages.put(Herbivore.class, ImageIO.read(getClass().getResource("/resources/sheep.png")));
            organismImages.put(Carnivore.class, ImageIO.read(getClass().getResource("/resources/wolf.png")));
            blankImage = ImageIO.read(getClass().getResource("/resources/dirt.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading organism images. Ensure the files exist in the correct path.");
        }
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (environment == null) return;

        // Calculate the grid dimensions
        int panelHeight = getHeight();
        int panelWidth = getWidth();
        int statsHeight = 40; // Height reserved for stats display
        int cellWidth = panelWidth / environment.getWidth();
        int cellHeight = (panelHeight - statsHeight) / environment.getHeight();

        // Counters for organisms
        int plantCount = 0;
        int herbivoreCount = 0;
        int carnivoreCount = 0;

        // Draw the environment grid and count organisms
        for (int x = 0; x < environment.getWidth(); x++) {
            for (int y = 0; y < environment.getHeight(); y++) {
                Organism organism = environment.getOrganismAt(x, y);
                if (organism != null) {
                    BufferedImage image = getImageForOrganism(organism);
                    if (image != null) {
                        g.drawImage(image, x * cellWidth, y * cellHeight, cellWidth, cellHeight, null);
                    } else {
                        g.drawImage(blankImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight, null);
                    }

                    if (organism instanceof Plant) {
                        plantCount++;
                    } else if (organism instanceof Herbivore) {
                        herbivoreCount++;
                    } else if (organism instanceof Carnivore) {
                        carnivoreCount++;
                    }
                } else {
                    g.drawImage(blankImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight, null);
                }
                g.setColor(Color.BLACK);
                g.drawRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
            }
        }

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, panelHeight - statsHeight, panelWidth, statsHeight);
        g.setColor(Color.BLACK);
        g.drawRect(0, panelHeight - statsHeight, panelWidth, statsHeight);

        g.drawString("Plants: " + plantCount, 10, panelHeight - statsHeight + 15);
        g.drawString("Herbivores: " + herbivoreCount, 150, panelHeight - statsHeight + 15);
        g.drawString("Carnivores: " + carnivoreCount, 300, panelHeight - statsHeight + 15);
    }

    private BufferedImage getImageForOrganism(Organism organism) {
        return organismImages.getOrDefault(organism.getClass(), null);
    }
}
