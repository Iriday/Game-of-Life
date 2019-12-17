package life;

public class Generator {
    private static int gridSize;

    public static char[][] nextGeneration(char[][] previousGeneration) {
        gridSize = previousGeneration.length;
        char[][] nextGeneration = new char[gridSize][gridSize];
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
                    }
                    //A dead cell is reborn if has exactly three alive neighbors
                } else if (neighbors == 3) {
                    nextGeneration[i][j] = 'O';
                } else {
                    nextGeneration[i][j] = ' ';
                }

                neighbors = 0;
            }
        }
        return nextGeneration;
    }

    private static int balanceNeighborCoordinates(int coordinate) {

        return coordinate == -1 ? gridSize - 1 : (coordinate == gridSize ? 0 : coordinate);
    }
}
