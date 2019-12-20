package life;

import java.io.IOException;

public class GameOfLifeViewConsole implements LifeGeneratorObserver {
    private final LifeGeneratorModelInterface lifeGeneratorModel;

    GameOfLifeViewConsole(LifeGeneratorModelInterface lifeGeneratorModel) {
        this.lifeGeneratorModel = lifeGeneratorModel;
        this.lifeGeneratorModel.registerObservers(this);
    }

    @Override
    public void updateNumGeneration() {
        clearConsole();
        System.out.printf("Generation #%d\n", lifeGeneratorModel.getNumGeneration());
    }

    @Override
    public void updateNumAlive() {
        System.out.printf("Alive: %d\n\n", lifeGeneratorModel.getNumAlive());
    }

    @Override
    public void updateGeneration() {
        output(lifeGeneratorModel.getGeneration());
    }

    private void output(char[][] field) {
        for (char[] l : field) {
            for (char r : l) {
                System.out.print(r);
            }
            System.out.println();
        }
    }

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}
