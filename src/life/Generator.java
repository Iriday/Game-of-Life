package life;

import java.util.Random;

public class Generator {
    private int gridSize;
    private int generation = 0;
    private int alive = 0;

    public Generator(int gridSize) {
        this.gridSize = gridSize;
    }

    public char[][] createFirstGeneration() {
        char[][] firstGeneration = new char[gridSize][gridSize];
        Random random = new Random();//seed

        for (int i = 0; i < firstGeneration.length; i++) {
            for (int j = 0; j < firstGeneration.length; j++) {
                boolean value = random.nextBoolean();
                firstGeneration[i][j] = value ? 'O' : ' ';
                alive = value ? alive + 1 : alive;
            }
        }
        generation++;
        return firstGeneration;
    }

    public char[][] nextGeneration(char[][] previousGeneration) { //evolve
        gridSize = previousGeneration.length;
        char[][] nextGeneration = new char[gridSize][gridSize];
        alive = 0;
        char aliveCell = 'O';
        //char space = ' ';
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
                        alive++;
                    }
                    //A dead cell is reborn if has exactly three alive neighbors
                } else if (neighbors == 3) {
                    nextGeneration[i][j] = 'O';
                    alive++;
                } else {
                    nextGeneration[i][j] = ' ';
                }

                neighbors = 0;
            }
        }
        generation++;
        return nextGeneration;
    }

    private int balanceNeighborCoordinates(int coordinate) {

        return coordinate == -1 ? gridSize - 1 : (coordinate == gridSize ? 0 : coordinate);
    }

    public int getGeneration() {
        return generation;
    }

    public int getAlive() {
        return alive;
    }
}
