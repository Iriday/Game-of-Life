package life;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameOfLifeViewGUI extends JFrame implements LifeGeneratorObserver {
    private final LifeGeneratorModelInterface lifeGeneratorModel;
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    private JPanel labelsPanel;
    private JPanel speedPanel;
    private JPanel gridSizePanel;
    private JPanel saveLoadStatePanel;
    private JLabel generationLabel;
    private JLabel aliveLabel;
    private JLabel speedModeLabel;
    private JSlider speedSlider;
    private JButton pauseResumeButton;
    private JButton restartButton;
    private JButton colorChooserButton;
    private JButton gridSizeButton;
    private JButton saveStateButton;
    private JButton loadStateButton;
    private JFileChooser fileChooser;
    private JTextField gridSizeField;
    private Field field;
    private boolean paused = false;

    public GameOfLifeViewGUI(LifeGeneratorModelInterface lifeGeneratorModel) {
        super("Game of Life");
        this.lifeGeneratorModel = lifeGeneratorModel;
        this.lifeGeneratorModel.registerObservers(this);
        createView();
    }

    private void createView() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800, 600);
        //setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        createButtonsPanel();
        createLabelsPanel();
        createSpeedPanel();
        createGridSizePanel();
        createSaveLoadStatePanel();
        createMainPanel();

        add(mainPanel, BorderLayout.WEST);
        field = new Field();
        add(field, BorderLayout.CENTER);

        //setAlwaysOnTop(true);
        setVisible(true);
    }

    private void createButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        pauseResumeButton = new JButton("PR");
        restartButton = new JButton("New");
        colorChooserButton = new JButton("Color");

        pauseResumeButton.setMaximumSize(new Dimension(65, 20));
        restartButton.setMaximumSize(new Dimension(65, 20));
        colorChooserButton.setMaximumSize(new Dimension(65, 20));

        pauseResumeButton.addActionListener(l -> {
            if (!paused) {
                paused = true;
                //pauseResumeButton.setText("RP");
                lifeGeneratorModel.pause();
            } else {
                paused = false;
                //pauseResumeButton.setText("PR");
                lifeGeneratorModel.resume();
            }
        });
        colorChooserButton.addActionListener(l -> {
            Color color = JColorChooser.showDialog(this, "Choose color", Color.BLACK);
            field.setColor(color);
        });
        restartButton.addActionListener(l -> lifeGeneratorModel.restart());

        buttonsPanel.add(Box.createHorizontalStrut(2));
        buttonsPanel.add(pauseResumeButton);
        buttonsPanel.add(Box.createHorizontalStrut(2));
        buttonsPanel.add(colorChooserButton);
        buttonsPanel.add(Box.createHorizontalStrut(2));
        buttonsPanel.add(restartButton);
        buttonsPanel.add(Box.createHorizontalStrut(2));
        buttonsPanel.setMaximumSize(new Dimension(250, 20));
    }

    private void createLabelsPanel() {
        labelsPanel = new JPanel(new GridLayout(2, 1, 3, 5));
        generationLabel = new JLabel();
        aliveLabel = new JLabel();
        labelsPanel.add(generationLabel);
        labelsPanel.add(aliveLabel);
        labelsPanel.setMaximumSize(new Dimension(180, 30));
    }

    private void createSpeedPanel() {
        speedPanel = new JPanel(new BorderLayout(3, 1));
        speedModeLabel = new JLabel("Speed mode:");
        speedModeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        speedSlider = new JSlider(1, 3000, 750);//2250
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                lifeGeneratorModel.setSpeed(speedSlider.getValue());
            }
        });
        speedPanel.add(speedModeLabel, BorderLayout.NORTH);
        speedPanel.add(speedSlider);
        //speedPanel.setToolTipText();
        speedPanel.setMaximumSize(new Dimension(180, 35));
    }

    private void createGridSizePanel() {
        gridSizePanel = new JPanel();
        gridSizePanel.setLayout(new BoxLayout(gridSizePanel, BoxLayout.X_AXIS));
        gridSizeButton = new JButton("Grid size:");
        gridSizeButton.addActionListener(l -> {
            try {
                setGridSize(Integer.parseInt(gridSizeField.getText().trim()));
            } catch (Exception e) {

            }
        });
        gridSizeField = new JTextField(2);
        gridSizeField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        setGridSize(Integer.parseInt(gridSizeField.getText().trim()));
                    } catch (Exception e) {
                    }
                }
            }
        });
        gridSizePanel.add(gridSizeButton);
        gridSizePanel.add(Box.createHorizontalStrut(2));
        gridSizePanel.add(gridSizeField);
        gridSizePanel.setMaximumSize(new Dimension(150, 20));
    }

    void createSaveLoadStatePanel() {
        saveLoadStatePanel = new JPanel();
        saveLoadStatePanel.setLayout(new BoxLayout(saveLoadStatePanel, BoxLayout.X_AXIS));
        fileChooser = new JFileChooser();
        saveStateButton = new JButton("Save");
        saveStateButton.addActionListener(l -> {
            lifeGeneratorModel.pause();
            int choice = fileChooser.showSaveDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION) {
                lifeGeneratorModel.saveState(fileChooser.getSelectedFile());
            }
            lifeGeneratorModel.resume();
        });
        loadStateButton = new JButton("Load");
        loadStateButton.addActionListener(l -> {
            //boolean paused;
            lifeGeneratorModel.pause();
            int choice = fileChooser.showOpenDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION) {
                lifeGeneratorModel.loadState(fileChooser.getSelectedFile());
            }
            lifeGeneratorModel.resume();
        });

        saveLoadStatePanel.add(saveStateButton);
        saveLoadStatePanel.add(Box.createHorizontalStrut(2));
        saveLoadStatePanel.add(loadStateButton);
        saveLoadStatePanel.setMaximumSize(new Dimension(130, 20));
    }

    void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(Box.createRigidArea(new Dimension(5, 7)));
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 17)));
        mainPanel.add(labelsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 17)));
        mainPanel.add(speedPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 17)));
        mainPanel.add(gridSizePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 17)));
        mainPanel.add(saveLoadStatePanel);
    }

    private void setGridSize(int gridSize) {
        lifeGeneratorModel.setGridSize(gridSize);
    }

    @Override
    public void updateNumGeneration() {
        generationLabel.setText("Generation #" + lifeGeneratorModel.getNumGeneration());
    }

    @Override
    public void updateNumAlive() {
        aliveLabel.setText("Alive: " + lifeGeneratorModel.getNumAlive());
    }

    @Override
    public void updateGeneration() {
        field.setGeneration(lifeGeneratorModel.getGeneration());
        field.repaint();
    }

    private class Field extends JPanel {
        private char[][] generation = new char[1][1];
        private Color color = Color.BLACK;

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            int width = field.getWidth();
            int height = field.getHeight();
            int cellWidth = width / generation.length;
            int cellHeight = height / generation.length;
            graphics.setColor(Color.BLACK);

            //draw rows
            for (int i = 0; i < height; i += cellHeight) {
                graphics.drawLine(0, i, width, i);
            }

            //draw columns
            for (int i = 0; i < width; i += cellWidth) {
                graphics.drawLine(i, 0, i, height);
            }

            graphics.setColor(color);
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

        private void setColor(Color color) {
            this.color = color;
        }
    }
}
