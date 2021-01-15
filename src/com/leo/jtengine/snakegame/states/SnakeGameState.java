/**
 * Author:     Leonard Cseres
 * Date:       Fri Jan 08 2021
 * Time:       15:30:40
 */

package com.leo.jtengine.snakegame.states;

import com.leo.jtengine.snakegame.SnakeGame;
import com.leo.jtengine.states.State;

public abstract class SnakeGameState extends State {

    private final SnakeGame snakegame;

    protected SnakeGameState(SnakeGame snakegame) {
        super(snakegame);
        this.snakegame = snakegame;
    }

    public SnakeGame getSnakeGame() {
        return snakegame;
    }
}
