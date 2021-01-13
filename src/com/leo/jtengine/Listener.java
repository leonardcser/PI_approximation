package com.leo.jtengine;

import com.leo.jtengine.window.Keyboard;

public interface Listener {

    default boolean keyPressed(Keyboard key) {
        // Do nothing
        return false;
    }

    default boolean keyDown(Keyboard key) {
        // Do nothing
        return false;
    }
}
