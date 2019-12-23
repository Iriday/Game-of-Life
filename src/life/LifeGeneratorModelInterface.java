package life;

import java.io.File;

public interface LifeGeneratorModelInterface {

    void generation();

    void registerObservers(LifeGeneratorObserver lifeGeneratorObserver);

    void removeObservers(LifeGeneratorObserver lifeGeneratorObserver);

    void notifyObservers();

    int getNumAlive();

    int getNumGeneration();

    char[][] getGeneration();

    void setSpeed(int speed);

    void pause();

    void resume();

    void restart();

    void setGridSize(int gridSize);

    void saveState(File file);

    void loadState(File file);
}
