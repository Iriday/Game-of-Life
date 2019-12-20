package life;

public class Main {
    public static void main(String[] args) {

        Generator lifeGenerator = new Generator(20, 750, -2);
        //run GUI and Console version simultaneously
        GameOfLifeViewGUI gameOfLifeGUI = new GameOfLifeViewGUI(lifeGenerator);
        GameOfLifeViewConsole gameOfLifeViewConsole = new GameOfLifeViewConsole(lifeGenerator);

        lifeGenerator.generation();
    }
}
