/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        15:00
 */

package com.leo.jtengine;

import com.leo.jtengine.snakegame.SnakeGame;
import com.leo.jtengine.utils.Terminal;
import com.leo.jtengine.utils.TerminalLogger;
import com.leo.jtengine.window.KeyListener;
import com.leo.jtengine.window.Keyboard;
import com.leo.jtengine.window.Window;

public class Loop implements Runnable, Updatable, Graphics, Terminatable {

    public static void main(String[] args) {
        Window window = new Window();
        Terminal terminal = new Terminal(window);
        // Window window = new Window(159, 45);
        // Window window = new Window(40, 24);
//        window.setFullScreen();
//        new Loop(new AlgorithmVisualiser(window)).start();
        new Loop(new SnakeGame(terminal)).start();
    }

    private static final int FPS = 60;
    private final Application application;
    private final KeyListener keyListener = new KeyListener();
    private long nextStatTime;
    private boolean isRunning = false;

    private int fpsCounter;
    private int upsCounter;


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
        if (keyListener.hasUpdated() && key != null) {
            if (application.keyDown(key)) {
                keyListener.reset();
            }
            if (!keyListener.isPressed() && application.keyPressed(key)) {
                keyListener.reset();
            }
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
            TerminalLogger.log(String.format("FPS: %d, UPS: %d", fpsCounter, upsCounter));
            // Set title to fps
            application.getTerminal().executeCmd("printf '\033]2;" + "Fps:" + fpsCounter + "\u0007'");
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