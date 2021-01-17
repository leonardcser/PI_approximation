/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        15:00
 */

package com.leo.jtengine;

import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.utils.TerminalLogger;
import com.leo.jtengine.window.InputListener;
import com.leo.jtengine.window.Keyboard;

public class Loop implements Runnable, Updatable, Graphics, Terminatable {

    private static final int FPS = 60;
    private final Application application;
    private final InputListener keyListener = new InputListener();
    private long nextStatTime;
    private boolean isRunning = false;

    private boolean showFpsTitle = false;
    private boolean logFps = false;
    private int fpsCounter;
    private int upsCounter;

    public void setShowFpsTitle(boolean showFpsTitle) {
        this.showFpsTitle = showFpsTitle;
    }

    public void setLogFps(boolean logFps) {
        this.logFps = logFps;
    }

    public Loop(Application application) {
        TerminalLogger.redirectErr();
        // Terminal.writePID();
        this.application = application;
    }

    public void start() {
        isRunning = true;
        Thread thread = new Thread(this);
        thread.start();
        keyListener.start();
    }

    @Override
    public void run() {
        long start = 0;
        long elapsed = 0;
        long wait = 0;

        while (isRunning && !application.isExitRequested()) {
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
                TerminalLogger.logErr(e);
                Thread.currentThread().interrupt();
            }

            logStats();
        }
        isRunning = false;
        end();
    }

    @Override
    public void update() {
        // Check for user input
        Keyboard key = keyListener.getKey();
        DiscreteCoordinates hover = keyListener.getMouseHover();
        DiscreteCoordinates click = keyListener.getMouseClick();
        if (keyListener.hasUpdated()) {
            if (key != null) {
                application.keyDown(key);
                if (!keyListener.isPressed()) {
                    application.keyPressed(key);
                }
            }
            if (hover != null) {
                application.mouseHover(hover);
            }
            if (click != null) {
                application.mouseClick(click);
            }
            keyListener.reset();
        }

        application.update();
        ++upsCounter;
    }

    @Override
    public void render() {
        application.render();
        ++fpsCounter;
    }

    private void logStats() {
        if (System.currentTimeMillis() > nextStatTime) {
            if (logFps) {
                TerminalLogger.log(String.format("FPS: %d, UPS: %d", fpsCounter, upsCounter));
            }
            if (showFpsTitle) {
                // Set title to fps
                application.getTerminal().setTitle("Fps:" + fpsCounter);
            }
            fpsCounter = 0;
            upsCounter = 0;
            nextStatTime = System.currentTimeMillis() + 1000;
        }
    }

    @Override
    public void end() {
        application.getTerminal().close();
        System.exit(0);
    }
}