/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        17:15
 */


package com.leo.application.visualiserapp.states;

import com.leo.application.graphics.TextGraphics;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.visualiserapp.AlgorithmVisualiser;
import com.leo.application.visualiserapp.algorithms.sorting.BubbleSort;
import com.leo.application.visualiserapp.algorithms.sorting.QuickSort;
import com.leo.application.visualiserapp.algorithms.sorting.Sort;
import com.leo.application.visualiserapp.algorithms.sorting.SortingAlgorithm;
import com.leo.application.window.Cell;
import com.leo.application.window.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SortingState extends AlgorithmVisualiserState {
    private final TextGraphics title;
    private final Random rand = new Random();
    private final Integer[] barLengths;
    private final Sort sortingAlgorithm;

    public SortingState(AlgorithmVisualiser algorithmVisualiser, SortingAlgorithm algorithm) {
        super(algorithmVisualiser);
        barLengths = new Integer[getCanvas().getWidth()];

        if (algorithm.equals(SortingAlgorithm.BUBBLE_SORT)) {
            sortingAlgorithm = new BubbleSort(barLengths);
        } else {
            sortingAlgorithm = new QuickSort(barLengths);
        }
        generateBars(true);
        title = new TextGraphics(getCanvas(), new DiscreteCoordinates(2, 0), sortingAlgorithm + " (shuffled)");
    }

    private void generateBars(boolean randomised) {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < getCanvas().getWidth(); ++i) {
            arr.add((i / 2) + 1);
        }
        Collections.shuffle(arr);
        for (int i = 0; i < getCanvas().getWidth(); ++i) {
            if (randomised) {
                barLengths[i] = rand.nextInt(getCanvas().getHeight()) + 1;
            } else {
                barLengths[i] = arr.get(i);
            }
        }
    }

    @Override
    public void render() {
    }

    @Override
    public void update() {
        // User input
        if (getWindow().getKeyListener().keyIsDown(Keyboard.ESC)) {
            if (!sortingAlgorithm.isFinished()) {
                sortingAlgorithm.stop();
            }
            getAlgorithmVisualiser().getStates().removeFirst();
        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.SPACE)) {
            if (!sortingAlgorithm.isFinished()) {
                sortingAlgorithm.stop();
            }
            while (!sortingAlgorithm.isFinished()) {
                Thread.onSpinWait();
            }
            title.setText(sortingAlgorithm + " (shuffled)");
            generateBars(true);
        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.ENTER)) {
            if (sortingAlgorithm.isFinished()) {
                new Thread(sortingAlgorithm).start();
            } else {
                sortingAlgorithm.togglePause();
            }
        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.LEFT)) {
            sortingAlgorithm.increaseSpeed();
        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.RIGHT)) {
            sortingAlgorithm.decreaseSpeed();
        }

        // Update arr
        for (int i = 0; i < barLengths.length; ++i) {
            for (int j = 1; j <= barLengths[i]; ++j) {
                getCanvas().requestCellChange(new Cell(new DiscreteCoordinates(i, getCanvas().getHeight() - j), '█', sortingAlgorithm.getStates()[i], 1));
            }
        }
        if (!sortingAlgorithm.isFinished()) {
            title.setText(sortingAlgorithm + " (swaps: " + sortingAlgorithm.getSwaps() + ")");
        }
        title.update();

    }
}
