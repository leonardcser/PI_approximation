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
import com.leo.application.window.Cell;
import com.leo.application.window.Keyboard;

public class MazeState extends AlgorithmVisualiserState {
    private final MazeCell[][] maze;
    private final MazeGenerator mazeAlgorithm;

    public MazeState(AlgorithmVisualiser algorithmVisualiser) {
        super(algorithmVisualiser);
        maze = new MazeCell[getCanvas().getWidth() / 3][getCanvas().getHeight() / 3];
        generateEmptyMaze();
        mazeAlgorithm = new RecursiveBackTracker(maze);
    }

    private void generateEmptyMaze() {
        for (int i = 0; i < maze.length; ++i) {
            for (int j = 0; j < maze[i].length; ++j) {
                maze[i][j] = new MazeCell(new DiscreteCoordinates(j, i));
            }
        }
    }

    @Override
    public void render() {
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
                Colors color = null;
                if (maze[i][j].equals(mazeAlgorithm.getHead())) {
                    color = Colors.RED;
                } else if (maze[i][j].isVisited()) {
                    color = null;
                }
                updateHead(i, j, color);
                updateRightWalls(i, j);
                updateBottomWalls(i, j);
                updateCorners(i, j);

            }

        }
    }


    private void updateHead(int i, int j, Colors color) {
        for (int xHeadIndex = 0; xHeadIndex < 2; ++xHeadIndex) {
            for (int yHeadIndex = 0; yHeadIndex < 2; ++yHeadIndex) {
                if (maze[i][j].isVisited()) {
                    getCanvas().requestChange(
                            new Cell(new DiscreteCoordinates((i * 3) + xHeadIndex, (j * 3) + yHeadIndex), '█',
                                     color), 1);
                }
            }
        }
    }

    private void updateRightWalls(int i, int j) {
        if (maze[i][j].hasRightWall()) {
            for (int rightWallIndex = 0; rightWallIndex < 2; ++rightWallIndex) {
                getCanvas().requestChange(
                        new Cell(new DiscreteCoordinates((i * 3) + 2, (j * 3) + rightWallIndex), '█',
                                 Colors.DARK_GREY), 1);
            }
        } else {
            for (int leftWallIndex = 0; leftWallIndex < 2; ++leftWallIndex) {
                getCanvas().requestChange(
                        new Cell(new DiscreteCoordinates((i * 3) + 2, (j * 3) + leftWallIndex), '█', null), 1);
            }
        }
    }

    private void updateBottomWalls(int i, int j) {
        if (maze[i][j].hasBottomWall()) {
            for (int bottomWallIndex = 0; bottomWallIndex < 2; ++bottomWallIndex) {
                getCanvas().requestChange(
                        new Cell(new DiscreteCoordinates((i * 3) + bottomWallIndex, (j * 3) + 2), '█',
                                 Colors.DARK_GREY), 1);
            }
        } else {
            for (int bottomWallIndex = 0; bottomWallIndex < 2; ++bottomWallIndex) {
                getCanvas().requestChange(
                        new Cell(new DiscreteCoordinates((i * 3) + bottomWallIndex, (j * 3) + 2), '█', null),
                        1);
            }
        }
    }

    private void updateCorners(int i, int j) {
        getCanvas()
                .requestChange(new Cell(new DiscreteCoordinates((i * 3) + 2, (j * 3) + 2), '█', Colors.DARK_GREY),
                               1);
    }
}
