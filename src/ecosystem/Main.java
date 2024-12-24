package src.ecosystem;

import src.ecosystem.gui.EnvironmentSimulationGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Khởi chạy giao diện GUI trong luồng Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                EnvironmentSimulationGUI gui = new EnvironmentSimulationGUI();
                gui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
