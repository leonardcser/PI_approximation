/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:40
 */

package com.leo.application;

import com.leo.application.states.State;
import com.leo.application.window.Canvas;
import com.leo.application.window.Keyboard;
import com.leo.application.window.Window;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class Application implements Updatable, Graphics, Listener, Terminatable {
    private final Canvas canvas;
    private final Window window;
    private final Deque<State> states;
    private boolean exit = false;

    protected Application(int width, int height) {
        this.window = new Window(width, height);
        this.canvas = new Canvas(width, height);
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
