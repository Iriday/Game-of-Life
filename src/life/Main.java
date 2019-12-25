package life;

public class Main {
    public static void main(String[] args) {

        LifeGeneratorModel lifeGeneratorModel = new LifeGeneratorModel(30, 750, -2);

        //run GUI and console version simultaneously
        GameOfLifeViewConsole gameOfLifeViewConsole = new GameOfLifeViewConsole(lifeGeneratorModel);
        LifeGeneratorControllerGUI lifeGeneratorControllerGUI = new LifeGeneratorControllerGUI(lifeGeneratorModel);
    }
}
