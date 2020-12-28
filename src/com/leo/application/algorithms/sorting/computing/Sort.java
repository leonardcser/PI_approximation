/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        11:37
 */


package com.leo.application.algorithms.sorting.computing;

import com.leo.application.utils.Colors;

import java.util.Arrays;

public abstract class Sort implements Runnable {
    private final Integer[] arr;
    private final Colors[] states;
    private final int start;
    private final int end;
    private boolean finished = true;
    private volatile boolean paused = false;

    public boolean isExit() {
        return exit;
    }

    private volatile boolean exit = false;

    protected Sort(Integer[] arr) {
        this.arr = arr;
        states = new Colors[arr.length];
        start = 0;
        end = arr.length - 1;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isFinished() {
        return finished;
    }

    protected void setFinished(boolean finished) {
        this.finished = finished;
    }

    protected void setExit(boolean exit) {
        this.exit = exit;
    }

    protected void modifyStatesAt(int index, Colors color) {
        states[index] = color;
    }

    protected void clearStates() {
        Arrays.fill(states, null);
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
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int tmp = arr[initIndex];
            arr[initIndex] = arr[destIndex];
            arr[destIndex] = tmp;
        }

    }

    protected abstract void sort(Integer[] arr, int start, int end);

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
}
