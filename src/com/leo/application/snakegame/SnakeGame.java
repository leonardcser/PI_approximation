/**
 *  Author:     Leonard Cseres
 *  Date:       Fri Jan 08 2021
 *  Time:       15:26:03
 */

package com.leo.application.snakegame;

import com.leo.application.Application;
import com.leo.application.snakegame.states.PlayState;

public class SnakeGame extends Application {

    public SnakeGame(int width, int height) {
        super(width, height);
        pushState(new PlayState(this));
    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
        super.end();
    }

}