/**
 *  Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        19:27
 */

package com.leo.application.window;

import com.leo.application.utils.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeyListener implements Runnable {
    private final List<Integer> keyHistory = new ArrayList<>();
    private int keyDown = 0;
    private boolean isRunning;
    private boolean isMultiCharKey = false;

    public boolean keyIsDown(Keyboard key) {
        if (keyDown == key.keyCode) {
            keyDown = 0;
            if (keyDown == Keyboard.ESC.keyCode && keyHistory.size() == 1) {
                keyHistory.clear();
                new Thread(() -> Terminal.executeCmd("afplay bin/sounds/rollover2.wav")).start();
                return true;
            } else if (keyDown == Keyboard.ESC.keyCode) {
                return false;
            }
            keyHistory.clear();
            new Thread(() -> Terminal.executeCmd("afplay bin/sounds/bong_001.wav")).start();
            return true;
        }
        return false;
    }

    public void start() {
        isRunning = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                if (isMultiCharKey) {
                    keyDown += System.in.read();
                } else {
                    keyDown = System.in.read();
                }
                if (keyDown == 91 || isMultiCharKey) {
                    isMultiCharKey = !isMultiCharKey;
                }
                keyHistory.add(keyDown);
            } catch (IOException e) {
                Terminal.logErr(e);
            }
            if (keyHistory.size() > 1000) {
                keyHistory.clear();
            }
        }
    }
}
