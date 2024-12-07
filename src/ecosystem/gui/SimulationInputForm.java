package src.ecosystem.gui;

import javax.swing.*;
import java.awt.*;

public class SimulationInputForm {
    // Simulation parameters
    private double soilBreedRate = 1.0;
    private double plantPoisonChance = 1.0;
    private double plantBreedRate = 1.0;
    private double sheepBreedRate = 1.0;
    private double wolfBreedRate = 0.5;

    private int maxPlantNutrition = 10;
    private int initialSheepHealth = 40;
    private int initialWolfHealth = 5;

    private int mapDimension = 10;

    // Display the input form and collect inputs
    public boolean collectInputs() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Input fields
        JTextField soilRateField = new JTextField(Double.toString(soilBreedRate));
        JTextField plantPoisonField = new JTextField(Double.toString(plantPoisonChance));
        JTextField plantBreedField = new JTextField(Double.toString(plantBreedRate));
        JTextField sheepBreedField = new JTextField(Double.toString(sheepBreedRate));
        JTextField wolfBreedField = new JTextField(Double.toString(wolfBreedRate));
        JTextField maxNutritionField = new JTextField(Integer.toString(maxPlantNutrition));
        JTextField sheepHealthField = new JTextField(Integer.toString(initialSheepHealth));
        JTextField wolfHealthField = new JTextField(Integer.toString(initialWolfHealth));
        JTextField mapDimensionField = new JTextField(Integer.toString(mapDimension));

        // Add labels and fields to panel
        panel.add(new JLabel("Soil Fertility Rate (0-1):"));
        panel.add(soilRateField);
        panel.add(new JLabel("Plant Poison Chance (0-1):"));
        panel.add(plantPoisonField);
        panel.add(new JLabel("Plant Reproduction Rate (0-1):"));
        panel.add(plantBreedField);
        panel.add(new JLabel("Sheep Reproduction Rate (0-1):"));
        panel.add(sheepBreedField);
        panel.add(new JLabel("Wolf Reproduction Rate (0-1):"));
        panel.add(wolfBreedField);
        panel.add(new JLabel("Max Plant Nutrition:"));
        panel.add(maxNutritionField);
        panel.add(new JLabel("Initial Sheep Health:"));
        panel.add(sheepHealthField);
        panel.add(new JLabel("Initial Wolf Health:"));
        panel.add(wolfHealthField);
        panel.add(new JLabel("Map Dimension:"));
        panel.add(mapDimensionField);

        // Show dialog
        int result = JOptionPane.showConfirmDialog(null, panel, "Simulation Parameters", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return false; // User canceled
        }

        // Validate inputs
        try {
            soilBreedRate = validateDouble(soilRateField.getText(), 0, 1, "Soil Fertility Rate");
            plantPoisonChance = validateDouble(plantPoisonField.getText(), 0, 1, "Plant Poison Chance");
            plantBreedRate = validateDouble(plantBreedField.getText(), 0, 1, "Plant Reproduction Rate");
            sheepBreedRate = validateDouble(sheepBreedField.getText(), 0, 1, "Sheep Reproduction Rate");
            wolfBreedRate = validateDouble(wolfBreedField.getText(), 0, 1, "Wolf Reproduction Rate");
            maxPlantNutrition = validateInteger(maxNutritionField.getText(), 1, Integer.MAX_VALUE, "Max Plant Nutrition");
            initialSheepHealth = validateInteger(sheepHealthField.getText(), 1, Integer.MAX_VALUE, "Initial Sheep Health");
            initialWolfHealth = validateInteger(wolfHealthField.getText(), 1, Integer.MAX_VALUE, "Initial Wolf Health");
            mapDimension = validateInteger(mapDimensionField.getText(), 5, 100, "Map Dimension");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            return collectInputs(); // Retry form
        }

        return true;
    }

    // Getter and Setter methods
    public double getSoilBreedRate() {
        return soilBreedRate;
    }

    public void setSoilBreedRate(double soilBreedRate) {
        this.soilBreedRate = soilBreedRate;
    }

    public double getPlantPoisonChance() {
        return plantPoisonChance;
    }

    public void setPlantPoisonChance(double plantPoisonChance) {
        this.plantPoisonChance = plantPoisonChance;
    }

    public double getPlantBreedRate() {
        return plantBreedRate;
    }

    public void setPlantBreedRate(double plantBreedRate) {
        this.plantBreedRate = plantBreedRate;
    }

    public double getSheepBreedRate() {
        return sheepBreedRate;
    }

    public void setSheepBreedRate(double sheepBreedRate) {
        this.sheepBreedRate = sheepBreedRate;
    }

    public double getWolfBreedRate() {
        return wolfBreedRate;
    }

    public void setWolfBreedRate(double wolfBreedRate) {
        this.wolfBreedRate = wolfBreedRate;
    }

    public int getMaxPlantNutrition() {
        return maxPlantNutrition;
    }

    public void setMaxPlantNutrition(int maxPlantNutrition) {
        this.maxPlantNutrition = maxPlantNutrition;
    }

    public int getInitialSheepHealth() {
        return initialSheepHealth;
    }

    public void setInitialSheepHealth(int initialSheepHealth) {
        this.initialSheepHealth = initialSheepHealth;
    }

    public int getInitialWolfHealth() {
        return initialWolfHealth;
    }

    public void setInitialWolfHealth(int initialWolfHealth) {
        this.initialWolfHealth = initialWolfHealth;
    }

    public int getMapDimension() {
        return mapDimension;
    }

    public void setMapDimension(int mapDimension) {
        this.mapDimension = mapDimension;
    }

    // Helper methods for input validation
    private double validateDouble(String input, double min, double max, String fieldName) {
        try {
            double value = Double.parseDouble(input);
            if (value < min || value > max) {
                throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max + ".");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid decimal number.");
        }
    }

    private int validateInteger(String input, int min, int max, String fieldName) {
        try {
            int value = Integer.parseInt(input);
            if (value < min || value > max) {
                throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max + ".");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer.");
        }
    }
}
