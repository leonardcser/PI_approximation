/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        17:15
 */


package com.leo.application.algorithms.sorting;

import com.leo.application.Graphics;
import com.leo.application.Updatable;
import com.leo.application.algorithms.sorting.computing.QuickSort;
import com.leo.application.algorithms.sorting.computing.Sort;
import com.leo.application.graphics.TextGraphics;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.window.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SortingApplication implements Updatable, Graphics {
    private final Window window;
    private final Canvas canvas;
    private final InputListener inputListener = new InputListener();
    private final TextGraphics title;
    private final Random rand = new Random();
    private final Integer[] barLengths;
    private final Sort quickSort;
    private boolean requestSort = false;

    public SortingApplication(int width, int height) {
        this.window = new Window(width, height);
        this.canvas = new Canvas(width, height);
        barLengths = new Integer[canvas.getWidth()];
        quickSort = new QuickSort(barLengths);
        new Thread(inputListener).start();
        generateBars(true);
        title = new TextGraphics(canvas, new DiscreteCoordinates(2, 0), "");
    }

    private void generateBars(boolean randomised) {
        canvas.clear();
        requestSort = false;

        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < canvas.getWidth(); ++i) {
            arr.add((i / 2) + 1);
        }
        Collections.shuffle(arr);
        for (int i = 0; i < canvas.getWidth(); ++i) {
            if (randomised) {
                barLengths[i] = rand.nextInt(canvas.getHeight()) + 1;
            } else {
                barLengths[i] = arr.get(i);
            }
        }
    }

    @Override
    public void render() {
        window.render();
        canvas.render();
    }

    @Override
    public void update() {
        // User input
        if (inputListener.keyIsDown(Keyboard.SPACE)) {
            if (!quickSort.isFinished()) {
                quickSort.stop();
            }
            title.setText("");
            generateBars(true);
        } else if (inputListener.keyIsDown(Keyboard.S)) {
            requestSort = true;
        }

        // Update arr
        for (int i = 0; i < barLengths.length; ++i) {
            for (int j = 1; j <= barLengths[i]; ++j) {
                canvas.setCell(new Cell(new DiscreteCoordinates(i, canvas.getHeight() - j), '█', quickSort.getStates()[i]));
            }
        }
        if (requestSort) {
            if (quickSort.isFinished()) {
                title.setText(quickSort.toString());
                new Thread(quickSort).start();
            } else {
                quickSort.togglePause();
            }
            requestSort = false;
        }
        title.update();
    }

}
