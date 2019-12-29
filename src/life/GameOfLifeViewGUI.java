package life;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameOfLifeViewGUI extends JFrame implements LifeGeneratorObserver {
    private final LifeGeneratorControllerGUI controller;
    private final LifeGeneratorModelInterface model;
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    private JPanel labelsPanel;
    private JPanel speedPanel;
    private JPanel gridSizePanel;
    private JPanel saveLoadStatePanel;
    private JPanel cellSizePanel;
    private JPanel randomColorPanel;
    private JLabel generationLabel;
    private JLabel aliveLabel;
    private JLabel speedModeLabel;
    private JLabel cellSizeLabel;
    private JLabel randomColorLabel;
    private JSlider speedSlider;
    private JSlider cellSizeSlider;
    private JButton pauseResumeButton;
    private JButton restartButton;
    private JButton colorChooserButton;
    private JButton gridSizeButton;
    private JButton saveStateButton;
    private JButton loadStateButton;
    private JFileChooser fileChooser;
    private JTextField gridSizeField;
    private JToggleButton randomColorToggleButton;
    private Field field;
    private boolean paused = false;
    private static final Random random = new Random();

    public GameOfLifeViewGUI(LifeGeneratorControllerGUI controller, LifeGeneratorModelInterface model) {
        super("Game of Life");
        this.controller = controller;
        this.model = model;
        this.model.registerObservers(this);
    }

    void createView() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800, 600);
        //setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        createButtonsPanel();
        createLabelsPanel();
        createSpeedPanel();
        createGridSizePanel();
        createSaveLoadStatePanel();
        createCellSizePanel();
        createRandomColorPanel();
        createMainPanel();

        add(mainPanel, BorderLayout.WEST);
        field = new Field();
        add(field, BorderLayout.CENTER);

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
                controller.pause();
            } else {
                paused = false;
                //pauseResumeButton.setText("PR");
                controller.resume();
            }
        });
        colorChooserButton.addActionListener(l -> {
            Color color = JColorChooser.showDialog(this, "Choose color", Color.BLACK);
            field.setColor(color);
        });
        restartButton.addActionListener(l -> controller.restart());

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
                controller.setSpeed(speedSlider.getValue());
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
                controller.setGridSize(Integer.parseInt(gridSizeField.getText().trim()));
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
                        controller.setGridSize(Integer.parseInt(gridSizeField.getText().trim()));
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

    private void createSaveLoadStatePanel() {
        saveLoadStatePanel = new JPanel();
        saveLoadStatePanel.setLayout(new BoxLayout(saveLoadStatePanel, BoxLayout.X_AXIS));
        fileChooser = new JFileChooser();
        saveStateButton = new JButton("Save");
        saveStateButton.addActionListener(l -> {
            controller.pause();
            int choice = fileChooser.showSaveDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION) {
                controller.save(fileChooser.getSelectedFile());
            }
            controller.resume();
        });
        loadStateButton = new JButton("Load");
        loadStateButton.addActionListener(l -> {
            controller.pause();
            int choice = fileChooser.showOpenDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION) {
                controller.load(fileChooser.getSelectedFile());
            }
            controller.resume();
        });

        saveLoadStatePanel.add(saveStateButton);
        saveLoadStatePanel.add(Box.createHorizontalStrut(2));
        saveLoadStatePanel.add(loadStateButton);
        saveLoadStatePanel.setMaximumSize(new Dimension(130, 20));
    }

    private void createCellSizePanel() {
        cellSizePanel = new JPanel(new BorderLayout(3, 1));
        cellSizeLabel = new JLabel("Cell size:");
        cellSizeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        cellSizeSlider = new JSlider(0, 100, 0);
        cellSizeSlider.addChangeListener(l -> field.setCellSize(cellSizeSlider.getValue()));
        cellSizePanel.add(cellSizeLabel, BorderLayout.NORTH);
        cellSizePanel.add(cellSizeSlider);
        cellSizePanel.setMaximumSize(new Dimension(180, 35));
    }

    private void createRandomColorPanel() {
        randomColorPanel = new JPanel();
        randomColorLabel = new JLabel("Random color: ");
        randomColorToggleButton = new JToggleButton();
        randomColorToggleButton.addActionListener(l -> {
            if (colorChooserButton.isEnabled()) {
                colorChooserButton.setEnabled(false);
                field.setRandomColor(true);
            } else {
                colorChooserButton.setEnabled(true);
                field.setRandomColor(false);
            }
        });
        randomColorPanel.add(randomColorLabel);
        randomColorPanel.add(randomColorToggleButton);
    }

    private void createMainPanel() {
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
        mainPanel.add(Box.createRigidArea(new Dimension(5, 17)));
        mainPanel.add(cellSizePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 17)));
        mainPanel.add(randomColorPanel);
    }

    @Override
    public void updateNumGeneration() {
        generationLabel.setText("Generation #" + model.getNumGeneration());
    }

    @Override
    public void updateNumAlive() {
        aliveLabel.setText("Alive: " + model.getNumAlive());
    }

    @Override
    public void updateGeneration() {
        field.setGeneration(model.getGeneration());
        field.repaint();
    }

    private class Field extends JPanel {
        private char[][] generation = new char[1][1];
        private Color color = Color.BLACK;
        private int cellSize = 0;
        private int cellWidthOffset = 0;
        private int cellHeightOffset = 0;
        private boolean randomColor = false;

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            int cellWidth = field.getWidth() / generation.length;
            int cellHeight = field.getHeight() / generation.length;
            int gridWidth = generation.length * cellWidth;
            int gridHeight = generation.length * cellHeight;
            cellWidthOffset = cellWidth * cellSize / 200;
            cellHeightOffset = cellHeight * cellSize / 200;
            graphics.setColor(Color.BLACK);

            //draw rows
            for (int i = 0; i <= gridHeight; i += cellHeight) {
                graphics.drawLine(0, i, gridWidth, i);
            }

            //draw columns
            for (int i = 0; i <= gridWidth; i += cellWidth) {
                graphics.drawLine(i, 0, i, gridHeight);
            }

            graphics.setColor(color);
            // fill cells
            for (int i = 0; i < generation.length; i++) {
                for (int j = 0; j < generation.length; j++) {
                    if (generation[i][j] == 'O') {
                        if (randomColor) {
                            graphics.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                        }
                        graphics.fillRect(j * cellWidth + cellWidthOffset, i * cellHeight + cellHeightOffset, cellWidth - cellWidthOffset * 2, cellHeight - cellHeightOffset * 2);
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

        private void setCellSize(int value) {
            cellSize = value;
        }

        private void setRandomColor(boolean value) {
            randomColor = value;
        }
    }
}
