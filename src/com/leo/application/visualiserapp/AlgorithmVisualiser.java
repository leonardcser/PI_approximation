/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:07
 */


package com.leo.application.visualiserapp;

import com.leo.application.Application;
import com.leo.application.states.State;
import com.leo.application.visualiserapp.states.MenuState;
import com.leo.application.window.Canvas;
import com.leo.application.window.Keyboard;
import com.leo.application.window.Window;

import java.util.ArrayDeque;
import java.util.Deque;

public class AlgorithmVisualiser extends Application {
    private final Canvas canvas;
    private final Window window;
    private final Deque<State> states;

    public AlgorithmVisualiser(int width, int height) {
        this.window = new Window(width, height);
        this.canvas = new Canvas(width, height);
        states = new ArrayDeque<>();
        states.push(new MenuState(this));
    }

    public Deque<State> getStates() {
        return states;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Window getWindow() {
        return window;
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

}
