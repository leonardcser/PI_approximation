/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:40
 */

package com.leo.jtengine;

import java.util.ArrayDeque;
import java.util.Deque;

import com.leo.jtengine.states.State;
import com.leo.jtengine.utils.Terminal;
import com.leo.jtengine.window.Canvas;
import com.leo.jtengine.window.Keyboard;
import com.leo.jtengine.window.Window;

public abstract class Application implements Updatable, Graphics, Listener, Terminatable {
    private final Canvas canvas;
    private final Window window;
    private final Deque<State> states;
    private boolean exit = false;

    protected Application(Window window) {
        this.window = window;
        this.canvas = new Canvas(window.getWidth(), window.getHeight());
        states = new ArrayDeque<>();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Window getWindow() {
        return window;
    }

    public void pushState(State state) {
        states.push(state);
    }

    public void removeFirstState() {
        states.removeFirst();
    }

    @Override
    public boolean keyDown(Keyboard key) {
        return states.peekFirst().keyDown(key);
    }

    @Override
    public boolean keyPressed(Keyboard key) {
        return states.peekFirst().keyPressed(key);
    }

    @Override
    public void render() {
        window.render();
        canvas.render();
        assert states.peekFirst() != null;
    }

    @Override
    public void update() {
        assert states.peekFirst() != null;
        states.peekFirst().update();
        canvas.update();
    }

    @Override
    public void end() {
        exit = true;
        for (State state : states) {
            state.end();
        }
    }

    protected boolean isExitRequested() {
        return exit;
    }

}
