package src.ecosystem;

import src.ecosystem.gui.SimulationInputForm;

import javax.swing.*;
import java.awt.*;

class main {
    // Static variables
    static double soilBreedRate = 1.0;
    static double plantPoisonChance = 1.0;
    static double plantBreedRate = 1.0;
    static double sheepBreedRate = 1.0;
    static double wolfBreedRate = 0.5;

    static int maxPlantNutrition = 10;
    static int initialSheepHealth = 40;
    static int initialWolfHealth = 5;

    static int mapDimension = 10;

    static int dayCount = 0;
    static int timePerDay = 1000;
    static boolean isPaused = false;
    //Variables for how many of each organism there is
    static int plantNumber = 1;
    static int sheepNumber = 1;
    static int wolfNumber = 1;

    public static void main(String[] args) {
        // Use SimulationInputForm to gather inputs
        SimulationInputForm inputForm = new SimulationInputForm();
        if (inputForm.collectInputs()) {
            // Assign parameters from the input form
            soilBreedRate = inputForm.getSoilBreedRate();
            plantPoisonChance = inputForm.getPlantPoisonChance();
            plantBreedRate = inputForm.getPlantBreedRate();
            sheepBreedRate = inputForm.getSheepBreedRate();
            wolfBreedRate = inputForm.getWolfBreedRate();
            maxPlantNutrition = inputForm.getMaxPlantNutrition();
            initialSheepHealth = inputForm.getInitialSheepHealth();
            initialWolfHealth = inputForm.getInitialWolfHealth();
            mapDimension = inputForm.getMapDimension();

            // Start the simulation
            startSim();
        }
    }

    // Simulation logic (same as the original)
    public static void startSim() {
        JOptionPane.showMessageDialog(null, "Start Sim");

        if (plantNumber == 0) {
            JOptionPane.showMessageDialog(null, "Plants are extinct!");
        }
        if (sheepNumber == 0) {
            JOptionPane.showMessageDialog(null, "Sheep are extinct!");
        }
        if (wolfNumber == 0) {
            JOptionPane.showMessageDialog(null, "Wolves are extinct!");
        }

        System.out.println("Simulation ended after " + dayCount + " days.");
    }


}