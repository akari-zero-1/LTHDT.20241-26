package src.ecosystem.test;


import src.ecosystem.environment.Environment;
import src.ecosystem.organism.Herbivore;
import src.ecosystem.organism.Plant;

public class EcosystemTest {
    public static void main(String[] args) {
        // Create an environment of size 5x5
        Environment environment = new Environment(5, 5);

        // Add plants to the environment
        environment.addOrganism(new Plant(10, 0, 0), 0, 0);
        environment.addOrganism(new Plant(10, 4, 4), 4, 4);
        environment.addOrganism(new Plant(10, 2, 2), 2, 2);

        // Add herbivores to the environment
        environment.addOrganism(new Herbivore(50, 1, 1), 1, 1);
        environment.addOrganism(new Herbivore(50, 3, 3), 3, 3);

        // Display the initial state
        System.out.println("Initial Environment:");
        environment.displayGrid();

        // Simulate 10 steps
        for (int i = 1; i <= 10; i++) {
            System.out.println("\nStep " + i + ":");
            environment.simulateStep();
            environment.displayGrid();
        }
    }
}
