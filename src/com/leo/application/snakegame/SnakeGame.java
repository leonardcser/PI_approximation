/**
 *  Author:     Leonard Cseres
 *  Date:       Fri Jan 08 2021
 *  Time:       15:26:03
 */

package com.leo.application.snakegame;

import com.leo.application.Application;
import com.leo.application.snakegame.states.PlayState;
import com.leo.application.window.Window;

public class SnakeGame extends Application {

    public SnakeGame(Window window) {
        super(window);
        pushState(new PlayState(this));
    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
        super.end();
    }

}