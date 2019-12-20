package life;

import java.util.ArrayList;
import java.util.Random;

public class Generator implements LifeGeneratorModelInterface {//LifeGeneratorModel
    private int gridSize;
    private final int timeBetweenGenerations;
    private final int numOfGeneration;
    private char[][] previousGeneration;
    private int numGeneration = 0;
    private int numAlive = 0;
    private boolean firstGen = false;
    private final ArrayList<LifeGeneratorObserver> lifeGeneratorObservers = new ArrayList<>();

    public Generator(int gridSize, int timeBetweenGenerations, int numOfGenerations) {
        this.gridSize = gridSize;
        this.timeBetweenGenerations = timeBetweenGenerations;
        this.numOfGeneration = numOfGenerations;
    }

    @Override
    public void generation() {
        for (int i = 0; i < numOfGeneration; i++) {
            nextGeneration();
            notifyObservers();
        }

        if (numOfGeneration <= 0) {
            while (true) {
                nextGeneration();
                notifyObservers();
            }
        }
    }

    @Override
    public char[][] createFirstGeneration() {
        char[][] firstGeneration = new char[gridSize][gridSize];
        Random random = new Random();//seed

        for (int i = 0; i < firstGeneration.length; i++) {
            for (int j = 0; j < firstGeneration.length; j++) {
                boolean value = random.nextBoolean();
                firstGeneration[i][j] = value ? 'O' : ' ';
                numAlive = value ? numAlive + 1 : numAlive;
            }
        }
        numGeneration++;
        return firstGeneration;
    }

    private void nextGeneration() {
        if (!firstGen) {
            previousGeneration = createFirstGeneration();
            firstGen = true;
            return;
        }
        doSomething();

        gridSize = previousGeneration.length;
        char[][] nextGeneration = new char[gridSize][gridSize];
        numAlive = 0;
        char aliveCell = 'O';
        int neighbors = 0;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                boolean aliveCellOrSpace = previousGeneration[i][j] == aliveCell ? true : false;

                // same row
                if (previousGeneration[balanceNeighborCoordinates(i)][balanceNeighborCoordinates(j - 1)] == aliveCell) {
                    neighbors++;
                }
                if (previousGeneration[balanceNeighborCoordinates(i)][balanceNeighborCoordinates(j + 1)] == aliveCell) {
                    neighbors++;
                }

                // upper row
                if (previousGeneration[balanceNeighborCoordinates(i - 1)][balanceNeighborCoordinates(j - 1)] == aliveCell) {
                    neighbors++;
                }
                if (previousGeneration[balanceNeighborCoordinates(i - 1)][balanceNeighborCoordinates(j)] == aliveCell) {
                    neighbors++;
                }
                if (previousGeneration[balanceNeighborCoordinates(i - 1)][balanceNeighborCoordinates(j + 1)] == aliveCell) {
                    neighbors++;
                }

                // lower row
                if (previousGeneration[balanceNeighborCoordinates(i + 1)][balanceNeighborCoordinates(j - 1)] == aliveCell) {
                    neighbors++;
                }
                if (previousGeneration[balanceNeighborCoordinates(i + 1)][balanceNeighborCoordinates(j)] == aliveCell) {
                    neighbors++;
                }
                if (previousGeneration[balanceNeighborCoordinates(i + 1)][balanceNeighborCoordinates(j + 1)] == aliveCell) {
                    neighbors++;
                }


                if (aliveCellOrSpace) {
                    //An alive sell survives if has two or three alive neighbors. Otherwise dies of boredom or overpopulation.
                    if (neighbors < 2 || neighbors > 3) {
                        nextGeneration[i][j] = ' ';
                    } else {
                        nextGeneration[i][j] = previousGeneration[i][j];//'O'
                        numAlive++;
                    }
                    //A dead cell is reborn if has exactly three alive neighbors
                } else if (neighbors == 3) {
                    nextGeneration[i][j] = 'O';
                    numAlive++;
                } else {
                    nextGeneration[i][j] = ' ';
                }

                neighbors = 0;
            }
        }
        previousGeneration = nextGeneration;
        numGeneration++;
    }

    private int balanceNeighborCoordinates(int coordinate) {

        return coordinate == -1 ? gridSize - 1 : (coordinate == gridSize ? 0 : coordinate);
    }

    private void doSomething() {
        try {
            Thread.sleep(timeBetweenGenerations);
        } catch (InterruptedException e) {
            System.out.println("Sleep was interrupted");
        }
    }

    @Override
    public int getNumGeneration() {
        return numGeneration;
    }

    @Override
    public int getNumAlive() {
        return numAlive;
    }

    @Override
    public char[][] getGeneration() {
        return previousGeneration;
    }

    @Override
    public void registerObservers(LifeGeneratorObserver lifeGeneratorObserver) {
        lifeGeneratorObservers.add(lifeGeneratorObserver);
    }

    @Override
    public void removeObservers(LifeGeneratorObserver lifeGeneratorObserver) {
        lifeGeneratorObservers.remove(lifeGeneratorObserver);
    }

    @Override
    public void notifyObservers() {
        for (LifeGeneratorObserver observer : lifeGeneratorObservers) {
            observer.updateNumGeneration();
            observer.updateNumAlive();
            observer.updateGeneration();
        }
    }
}
