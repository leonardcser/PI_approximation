/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        15:00
 */


package com.leo.application;

import com.leo.application.algorithms.sorting.SortingApplication;
import com.leo.application.utils.ApplicationLogger;

public class Loop implements Runnable, Updatable, Graphics {

    private static final int FPS = 60;
    private final SortingApplication sortingApplication;
    private final long targetTime = 1000 / FPS;
    private long nextStatTime;
    private int fps, ups;

    ApplicationLogger logger = new ApplicationLogger();
    private Thread thread;
    private boolean isRunning = false;

    public Loop(SortingApplication sortingApplication) {
        this.sortingApplication = sortingApplication;
    }

    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        long start, elpased, wait;

        while (isRunning) {
            start = System.nanoTime();
            update();
            render();

            elpased = System.nanoTime() - start;
            wait = targetTime - elpased / 1000000;

            if (wait <= 0) {
                wait = 5;
            }
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            printStats();
        }
    }

    @Override
    public void update() {
        sortingApplication.update();
        ++ups;
    }

    @Override
    public void render() {
        sortingApplication.render();
        ++fps;
    }

    private void printStats() {
        if (System.currentTimeMillis() > nextStatTime) {
            logger.write(String.format("FPS: %d, UPS: %d", fps, ups));
            fps = 0;
            ups = 0;
            nextStatTime = System.currentTimeMillis() + 1000;
        }
    }
}