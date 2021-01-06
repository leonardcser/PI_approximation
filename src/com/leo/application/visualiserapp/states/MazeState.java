/*
 *	Author:      Leonard Cseres
 *	Date:        29.12.20
 *	Time:        02:33
 */

package com.leo.application.visualiserapp.states;

import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Colors;
import com.leo.application.visualiserapp.AlgorithmVisualiser;
import com.leo.application.visualiserapp.algorithms.maze.MazeCell;
import com.leo.application.visualiserapp.algorithms.maze.MazeGenerator;
import com.leo.application.visualiserapp.algorithms.maze.RecursiveBackTracker;
import com.leo.application.window.Keyboard;

public class MazeState extends AlgorithmVisualiserState {
    private final MazeCell[][] maze;
    private final MazeGenerator mazeAlgorithm;

    public MazeState(AlgorithmVisualiser algorithmVisualiser) {
        super(algorithmVisualiser);
        maze = new MazeCell[getCanvas().getWidth() / 3][(int) (2 * getCanvas().getHeight() / 3)];
        generateEmptyMaze();
        mazeAlgorithm = new RecursiveBackTracker(maze);
        getCanvas().setBackground(Colors.LIGHT_GREY);
    }

    private void generateEmptyMaze() {
        for (int i = 0; i < maze.length; ++i) {
            for (int j = 0; j < maze[i].length; ++j) {
                maze[i][j] = new MazeCell(new DiscreteCoordinates(j, i));
            }
        }
    }

    @Override
    public void update() {
        if (getWindow().getKeyListener().keyIsDown(Keyboard.ESC)) {
            if (mazeAlgorithm.isStarted()) {
                mazeAlgorithm.stop();
            }
            getAlgorithmVisualiser().getStates().removeFirst();
        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.SPACE)) {
            if (mazeAlgorithm.isStarted()) {
                mazeAlgorithm.stop();
            }
            while (mazeAlgorithm.isExit()) {
                Thread.onSpinWait();
            }
            generateEmptyMaze();
        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.ENTER)) {
            if (!mazeAlgorithm.isStarted()) {
                new Thread(mazeAlgorithm).start();
            } else {
                mazeAlgorithm.togglePause();
            }
        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.LEFT)) {
            mazeAlgorithm.increaseSpeed();
        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.RIGHT)) {
            mazeAlgorithm.decreaseSpeed();
        }
        for (int i = 0; i < maze.length; ++i) {
            for (int j = 0; j < maze[i].length; ++j) {
                updateHead(i, j);
                updateRightWalls(i, j);
                updateBottomWalls(i, j);
                updateCorners(i, j);
            }

        }
    }

    private void updateHead(int i, int j) {
        Colors color = null;
        if (maze[i][j].equals(mazeAlgorithm.getHead())) {
            color = Colors.RED;
        } else if (maze[i][j].isVisited()) {
            color = Colors.WHITE;
        }
        
        getCanvas().requestPixelChange(new DiscreteCoordinates(i * 3, j * 3), color, 2);
        getCanvas().requestPixelChange(new DiscreteCoordinates(i * 3, j * 3 + 1), color, 2);
        getCanvas().requestPixelChange(new DiscreteCoordinates(i * 3 + 1, j * 3), color, 2);
        getCanvas().requestPixelChange(new DiscreteCoordinates(i * 3 + 1, j * 3 + 1), color, 2);
    }

    private void updateRightWalls(int i, int j) {
        Colors color = getWallColor(maze[i][j].hasRightWall());
        getCanvas().requestPixelChange(new DiscreteCoordinates(i * 3 + 2, j * 3), color, 1);
        getCanvas().requestPixelChange(new DiscreteCoordinates(i * 3 + 2, j * 3 + 1), color, 1);
    }

    private void updateBottomWalls(int i, int j) {
        Colors color = getWallColor(maze[i][j].hasBottomWall());
        getCanvas().requestPixelChange(new DiscreteCoordinates(i * 3, j * 3 + 2), color, 1);
        getCanvas().requestPixelChange(new DiscreteCoordinates(i * 3 + 1, j * 3 + 2), color, 1);
    }

    private Colors getWallColor(boolean hasWall) {
        Colors color = Colors.WHITE;
        if (hasWall) {
            color = Colors.DARK_GREY;
        }
        return color;
    }

    private void updateCorners(int i, int j) {
        getCanvas().requestPixelChange(new DiscreteCoordinates(i * 3 + 2, j * 3 + 2), Colors.DARK_GREY, 1);
    }
}
