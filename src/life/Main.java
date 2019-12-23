package life;

public class Main {
    public static void main(String[] args) {

        LifeGeneratorModel lifeGeneratorModel = new LifeGeneratorModel(30, 750, -2);
        //run GUI and Console version simultaneously
        GameOfLifeViewGUI gameOfLifeGUI = new GameOfLifeViewGUI(lifeGeneratorModel);
        GameOfLifeViewConsole gameOfLifeViewConsole = new GameOfLifeViewConsole(lifeGeneratorModel);

        lifeGeneratorModel.generation();
    }
}
