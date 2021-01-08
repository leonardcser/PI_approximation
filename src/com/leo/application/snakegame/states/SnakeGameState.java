/**
 *  Author:     Leonard Cseres
 *  Date:       Fri Jan 08 2021
 *  Time:       15:30:40
 */

package com.leo.application.snakegame.states;

import com.leo.application.snakegame.SnakeGame;
import com.leo.application.states.State;

public abstract class SnakeGameState extends State {

    private SnakeGame snakegame;

    protected SnakeGameState(SnakeGame snakegame) {
        super(snakegame);
        this.snakegame = snakegame;
    }

    public SnakeGame getSnakeGame() {
        return snakegame;
    }
}
