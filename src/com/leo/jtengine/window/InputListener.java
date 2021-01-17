/**
 *  Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        19:27
 */

package com.leo.jtengine.window;

import java.util.ArrayList;
import java.util.List;

import com.leo.jtengine.maths.DiscreteCoordinates;

public class InputListener implements Runnable {
    private List<Integer> input = new ArrayList<>();
    private boolean isPressed = true;
    private boolean isRunning = false;
    private boolean updated = false;

    public boolean isPressed() {
        return isPressed;
    }

    public boolean hasUpdated() {
        return updated;
    }

    public void reset() {
        updated = false;
        input.clear();
    }

    public Keyboard getKey() {
        return Keyboard.get(input.toArray(new Integer[0]));
    }

    public DiscreteCoordinates getMouseClick() {
        return Mouse.getClick(input.toArray(new Integer[0]));
    }

    public DiscreteCoordinates getMouseHover() {
        return Mouse.getHover(input.toArray(new Integer[0]));
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
        while (isRunning) {
            start = System.nanoTime();
            char[] terminalInput = System.console().readLine().toCharArray();
            input.clear();
            
            if (terminalInput.length == 0) {
                input.add(13);
            } else {
                for (char c : terminalInput) {
                    input.add((int) c);
                }
            }
            
            elapsed = (System.nanoTime() - start) / 10000;
            if (elapsed < 9000 || (elapsed > 49000 && elapsed < 51000)) {
                isPressed = true;
            } else {
                isPressed = false;
            }
            updated = true;
        }
    }
}
