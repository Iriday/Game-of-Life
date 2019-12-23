package life;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LifeGeneratorModel implements LifeGeneratorModelInterface {
    private int gridSize;
    private int newGridSize;
    private boolean gridSizedChanged = false;
    private int timeBetweenGenerations;
    private final int numOfGeneration;
    private char[][] previousGeneration;
    private volatile int numGeneration = 0;
    private volatile int numAlive = 0;
    private boolean firstGen = false;
    private final ArrayList<LifeGeneratorObserver> lifeGeneratorObservers = new ArrayList<>();
    private volatile boolean paused = false;
    private volatile boolean restart = false;

    public LifeGeneratorModel(int gridSize, int timeBetweenGenerations, int numOfGenerations) {
        this.gridSize = gridSize;
        this.timeBetweenGenerations = timeBetweenGenerations;
        this.numOfGeneration = numOfGenerations;
    }

    @Override
    public void generation() {

        for (int i = 0; i < numOfGeneration; ) {
            if (!paused) {
                nextGeneration();
                notifyObservers();
                i++;
            }
        }
        if (numOfGeneration <= 0) {
            while (true) {
                if (!paused) {
                    nextGeneration();
                    notifyObservers();
                }
            }
        }
    }

    private char[][] createFirstGeneration() {
        char[][] firstGeneration = new char[gridSize][gridSize];
        Random random = new Random();

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
        if (restart) {
            firstGen = false;
            numGeneration = 0;
            numAlive = 0;
            restart = false;
            if (gridSizedChanged) {
                gridSize = newGridSize;
            }
        }
        if (!firstGen) {
            previousGeneration = createFirstGeneration();
            firstGen = true;
            restart = false;
            return;
        }
        doSomething();

        while (paused) {
            doSomething();
        }

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

    @Override
    public void setSpeed(int speed) {
        timeBetweenGenerations = speed;
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void restart() {
        restart = true;
    }

    @Override
    public void setGridSize(int gridSize) {
        this.newGridSize = gridSize;
        gridSizedChanged = true;
        restart();
    }

    @Override
    public void saveState(File file) {
        // paused = true;
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(getNumGeneration() + " " + getNumAlive() + "\n");
            for (char[] r : previousGeneration) {
                for (char c : r) {
                    fileWriter.write(c);
                }
                fileWriter.write("\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
        //paused = false;
    }

    @Override
    public void loadState(File file) {
        char[][] data = null;
        int index = 0;
        try {
            Scanner scn = new Scanner(file);
            String[] counters = scn.nextLine().split(" ");
            System.out.println(counters[0] + "   " + counters[1]);

            while (scn.hasNext()) {
                String temp = scn.nextLine();
                if (data == null) {
                    data = new char[temp.length()][temp.length()];
                }
                data[index++] = temp.toCharArray();
            }
            numGeneration = Integer.parseInt(counters[0]);
            numAlive = Integer.parseInt(counters[1]);
            previousGeneration = data;
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }
}