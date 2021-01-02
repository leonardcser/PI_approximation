/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        15:00
 */

package com.leo.application;

import com.leo.application.utils.Terminal;
import com.leo.application.visualiserapp.AlgorithmVisualiser;

public class Loop implements Runnable, Updatable, Graphics {
    public static void main(String[] args) {
        new Loop(new AlgorithmVisualiser(159, 45)).start();
    }

    private final Application stateManager;
    private static final int FPS = 60;
    private long nextStatTime;
    private boolean isRunning = false;

    private int fpsCounter;
    private int upsCounter;

    public Loop(Application application) {
        this.stateManager = application;
    }

    public void start() {
        isRunning = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        long start = 0;
        long elapsed = 0;
        long wait = 0;

        while (isRunning) {
            start = System.nanoTime();
            update();
            render();

            elapsed = System.nanoTime() - start;
            long targetTime = 1000 / FPS;
            wait = targetTime - elapsed / 1000000;

            if (wait <= 0) {
                wait = 5;
            }
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                Terminal.logErr(e);
                Thread.currentThread().interrupt();
            }

            logStats();
        }
    }

    @Override
    public void update() {
        stateManager.update();
        ++upsCounter;
    }

    @Override
    public void render() {
        stateManager.render();
        ++fpsCounter;
    }

    private void logStats() {
        if (System.currentTimeMillis() > nextStatTime) {
            // Terminal.clearLogs();
            Terminal.log(String.format("FPS: %d, UPS: %d", fpsCounter, upsCounter));
            fpsCounter = 0;
            upsCounter = 0;
            nextStatTime = System.currentTimeMillis() + 1000;
        }
    }
}