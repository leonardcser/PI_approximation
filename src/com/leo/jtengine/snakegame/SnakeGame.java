/**
 * Author:     Leonard Cseres
 * Date:       Fri Jan 08 2021
 * Time:       15:26:03
 */

package com.leo.jtengine.snakegame;

import com.leo.jtengine.Application;
import com.leo.jtengine.snakegame.states.PlayState;
import com.leo.jtengine.utils.Terminal;

public class SnakeGame extends Application {

    public SnakeGame(Terminal terminal) {
        super(terminal);
        pushState(new PlayState(this));
    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
        super.end();
    }

}