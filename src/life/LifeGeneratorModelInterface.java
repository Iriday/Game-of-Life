package life;

public interface LifeGeneratorModelInterface {

    void generation();

    char[][] createFirstGeneration();

    void registerObservers(LifeGeneratorObserver lifeGeneratorObserver);

    void removeObservers(LifeGeneratorObserver lifeGeneratorObserver);

    void notifyObservers();

    int getNumAlive();

    int getNumGeneration();

    char[][] getGeneration();
}
