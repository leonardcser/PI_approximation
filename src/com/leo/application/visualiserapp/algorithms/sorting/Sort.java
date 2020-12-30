/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        11:37
 */


package com.leo.application.visualiserapp.algorithms.sorting;

import com.leo.application.utils.Colors;

import java.util.Arrays;

public abstract class Sort implements Runnable {
    private int sortSleepTime = 21;
    private final Integer[] arr;
    private final Colors[] states;
    private final int start;
    private final int end;
    private boolean finished = true;
    private boolean paused = false;
    private int swaps = 0;
    private boolean exit = false;


    protected Sort(Integer[] arr) {
        this.arr = arr;
        states = new Colors[arr.length];
        start = 0;
        end = arr.length - 1;
    }

    public void increaseSpeed() {
        sortSleepTime = sortSleepTime + 5 < 100 ? sortSleepTime + 5 : sortSleepTime;
    }

    public void decreaseSpeed() {
        sortSleepTime = sortSleepTime - 5 > 0 ? sortSleepTime - 5 : sortSleepTime;
    }

    public boolean isExit() {
        return exit;
    }

    protected void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean isFinished() {
        return finished;
    }

    protected void setFinished(boolean finished) {
        this.finished = finished;
    }

    protected void modifyStatesAt(int index, Colors color) {
        states[index] = color;
    }

    protected Integer[] getArr() {
        return arr;
    }

    public Colors[] getStates() {
        return states;
    }

    protected int getStart() {
        return start;
    }

    protected int getEnd() {
        return end;
    }

    protected void swap(Integer[] arr, int initIndex, int destIndex) {
        while (paused && !exit) {
            Thread.onSpinWait();
        }

        if (!exit) {
            try {
                Thread.sleep(sortSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            int tmp = arr[initIndex];
            arr[initIndex] = arr[destIndex];
            arr[destIndex] = tmp;
            ++swaps;
        }
    }

    protected abstract void sort(Integer[] arr, int start, int end);

    protected void reset() {
        swaps = 0;
        clearStates();
        setFinished(true);
        setPaused(false);
        setExit(false);
    }

    protected void clearStates() {
        Arrays.fill(states, null);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void stop() {
        exit = true;
    }

    public void togglePause() {
        paused = !paused;
    }

    @Override
    public void run() {
        finished = false;
    }

    public int getSwaps() {
        return swaps;
    }
}
