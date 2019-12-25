package life;

import java.io.File;

public class LifeGeneratorControllerGUI {
    private final LifeGeneratorModelInterface model;
    private final GameOfLifeViewGUI view;

    public LifeGeneratorControllerGUI(LifeGeneratorModelInterface model) {
        this.model = model;
        view = new GameOfLifeViewGUI(this, model);
        view.createView();
        this.model.generation();
    }

    public void pause() {
        model.pause();
    }

    public void resume() {
        model.resume();
    }

    public void restart() {
        model.restart();
    }

    public void setSpeed(int value) {
        model.setSpeed(value);
    }

    public void setGridSize(int gridSize) {
        model.setGridSize(gridSize);
    }

    public void save(File file){
        model.saveState(file);
    }

    public void load(File file){
        model.loadState(file);
    }
}
