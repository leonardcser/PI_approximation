/**
 *  Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        19:27
 */

package com.leo.application.window;

import java.util.ArrayList;
import java.util.List;

public class KeyListener implements Runnable {
    private List<Integer> keyFromInput = new ArrayList<>();
    private boolean isPressed = true;
    private boolean isRunning;
    private boolean updated = false;

    public boolean isPressed() {
        return isPressed;
    }

    public boolean hasUpdated() {
        return updated;
    }

    public void reset() {
        updated = false;
        keyFromInput.clear();
    }

    public Keyboard getKey() {
        return Keyboard.get(keyFromInput.toArray(new Integer[0]));
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
            char[] input = System.console().readLine().toCharArray();
            updated = true;
            keyFromInput.clear();

            if (input.length == 0) {
                keyFromInput.add(13);
            } else {
                for (char c : input) {
                    keyFromInput.add((int) c);
                }
            }

            elapsed = (System.nanoTime() - start) / 10000;
            if (elapsed < 9000 || (elapsed > 49000 && elapsed < 51000)) {
                isPressed = true;
            } else {
                isPressed = false;
            }
        }
    }
}
