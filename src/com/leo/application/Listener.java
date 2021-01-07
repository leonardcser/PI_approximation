package com.leo.application;

import com.leo.application.window.Keyboard;

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
