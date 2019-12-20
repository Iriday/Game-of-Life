package life;

import javax.swing.*;
import java.awt.*;

public class GameOfLifeViewGUI extends JFrame implements LifeGeneratorObserver {
    private final LifeGeneratorModelInterface lifeGeneratorModel;
    private JPanel panel;
    private JLabel generation;
    private JLabel alive;
    private Field field;

   public GameOfLifeViewGUI(LifeGeneratorModelInterface lifeGeneratorModel) {
        super("Game of Life");
        this.lifeGeneratorModel = lifeGeneratorModel;
        this.lifeGeneratorModel.registerObservers(this);
        createView();
    }

    private void createView() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 450);
        setLayout(new BorderLayout());
        //JTextArea f= new JTextArea();
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        generation = new JLabel();//"Generation #1"
        alive = new JLabel();//"Alive: 2"
        panel.add(generation);
        panel.add(alive);

        add(panel, BorderLayout.NORTH);
        field = new Field();
        add(field);

        //setAlwaysOnTop(true);
        setVisible(true);
    }

    @Override
    public void updateNumGeneration() {
        generation.setText("Generation #" + lifeGeneratorModel.getNumGeneration());
    }

    @Override
    public void updateNumAlive() {
        alive.setText("Alive: " + lifeGeneratorModel.getNumAlive());
    }

    @Override
    public void updateGeneration() {
        field.setGeneration(lifeGeneratorModel.getGeneration());
        field.repaint();
    }

    private class Field extends JPanel {
        char[][] generation = new char[0][0];

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            int width = getWidth();
            int height = getHeight();
            int cellWidth = width / 20;
            int cellHeight = height / 20;

            //draw rows
            for (int i = 0; i < height; i += cellHeight) {
                graphics.drawLine(0, i, width, i);
            }

            //draw columns
            for (int i = 0; i < width; i += cellWidth) {
                graphics.drawLine(i, 0, i, height);
            }

            // fill cells
            for (int i = 0; i < generation.length; i++) {
                for (int j = 0; j < generation.length; j++) {
                    if (generation[i][j] == 'O') {
                        graphics.fillRect(j * cellWidth/*+2*/, i * cellHeight /*+2*/, cellWidth /*-4*/, cellHeight /*-4*/);
                    }
                }
            }
        }

        private void setGeneration(char[][] generation) {
            this.generation = generation;
        }
    }
}
