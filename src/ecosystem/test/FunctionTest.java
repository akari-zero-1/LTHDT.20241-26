package src.ecosystem.test;

import src.ecosystem.environment.Environment;
import src.ecosystem.organism.*;

class FunctionTest {
    private Environment environment;

    private void assertOrganismAtPosition(Organism expected, int x, int y) {
        Organism actual = environment.getOrganismAt(x, y);
        if (expected == null) {
            if (actual != null) {
                System.out.println("Assertion failed: Expected null, but found " + actual.getClass().getSimpleName()
                        + " at (" + x + ", " + y + ")");
            }
        } else {
            if (actual == null) {
                System.out.println("Assertion failed: Expected " + expected.getClass().getSimpleName() + " at (" + x
                        + ", " + y + "), but found null");
            } else if (actual.getClass() != expected.getClass()) {
                System.out.println("Assertion failed: Expected " + expected.getClass().getSimpleName() + ", but found "
                        + actual.getClass().getSimpleName() + " at (" + x + ", " + y + ")");
            }
        }
    }

    private void assertOrganismsCount(int expected) {
        int actual = environment.getOrganisms().size();
        if (actual != expected) {
            System.out.println("Assertion failed: Expected " + expected + " organisms, but found " + actual);
        }
    }

    private void testAddOrganism() {
        System.out.println("Running testAddOrganism");
        environment = new Environment(10, 10, 0.05);
        Organism plant = new Plant(20, 5, 5);
        environment.addOrganism(plant, 5, 5);
        Herbivore herbivore = new Herbivore(160, 7, 7);
        environment.addOrganism(herbivore, 7, 7);
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        assertOrganismAtPosition(plant, 5, 5);
        assertOrganismsCount(1);
        System.out.println("========================================");
        System.out.println("\n");
    }

    private void testSimulateStep() {
        System.out.println("Running testSimulateStep");
        environment = new Environment(10, 10, 0.05);
        Herbivore herbivore = new Herbivore(0, 6, 0);
        environment.addOrganism(herbivore, 6, 6);
        Organism plant = new Plant(20, 5, 5);
        environment.addOrganism(plant, 5, 5);
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        // assertOrganismAtPosition(carnivore, 5, 5);
        assertOrganismsCount(1);
        System.out.println("========================================");
        System.out.println("\n");
    }

    private void testCleanUpDeadOrganisms() {
        System.out.println("Running testCleanUpDeadOrganisms");
        environment = new Environment(10, 10, 5);
        Herbivore herbivore = new Herbivore(0, 7, 7);
        environment.addOrganism(herbivore, 7, 7);
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        assertOrganismAtPosition(null, 5, 5);
        assertOrganismsCount(0);
        System.out.println("========================================");
        System.out.println("\n");
    }

    private void testPopulateRandomly() {
        System.out.println("Running testPopulateRandomly");
        environment = new Environment(10, 10, 0.0);
        environment.populateRandomly(5, 3, 2);
        int plantCount = 0;
        int carnivoreCount = 0;
        int herbivoreCount = 0;
        for (Organism organism : environment.getOrganisms()) {
            if (organism instanceof Plant) {
                plantCount++;
            } else if (organism instanceof Carnivore) {
                carnivoreCount++;
            } else if (organism instanceof Herbivore) {
                herbivoreCount++;
            }
        }
        environment.displayGrid();
        environment.simulateStep();
        environment.displayGrid();
        if (plantCount != 5) {
            System.out.println("Assertion failed: Expected 5 plants, but found " + plantCount);
        }
        if (carnivoreCount != 3) {
            System.out.println("Assertion failed: Expected 3 carnivores, but found " + carnivoreCount);
        }
        if (herbivoreCount != 2) {
            System.out.println("Assertion failed: Expected 2 herbivores, but found " + herbivoreCount);
        }
        System.out.println("========================================");
        System.out.println("\n");
    }

    public static void main(String[] args) {
        FunctionTest test = new FunctionTest();
        // test.testAddOrganism();
        test.testSimulateStep();
        // test.testCleanUpDeadOrganisms();
        // test.testPopulateRandomly();
    }
}
