/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        19:27
 */


package com.leo.application.window;

import com.leo.application.utils.Terminal;

import java.io.IOException;

public class InputListener implements Runnable {
    private int keyPressed = 0;
    private int keyDown = 0;

    public boolean keyIsDown(Keyboard key) {
        if (keyDown == key.keyCode) {
            keyDown = 0;
            return true;
        }
        return false;
    }

    public boolean keyIsPressed(Keyboard key) {
        return keyPressed == key.keyCode;
    }

    @Override
    public void run() {
        try {
            keyDown = System.in.read();
            keyPressed = keyDown;
            if (keyDown == Keyboard.ESC.keyCode) {
                Terminal.resetCursorPos();
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        run();
    }
}
