/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:40
 */

package com.leo.jtengine;

import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.states.State;
import com.leo.jtengine.utils.TerminalLogger;
import com.leo.jtengine.window.Keyboard;
import com.leo.jtengine.window.Window;
import com.leo.jtengine.window.render.Canvas;
import com.leo.jtengine.window.render.Terminal;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class Application implements Updatable, Graphics, Listener, Terminatable {
    private final Terminal terminal;
    private final Window window;
    private final Canvas canvas;
    private final Deque<State> states;
    private boolean exit = false;

    protected Application(Terminal terminal) {
        this.terminal = terminal;
        this.window = terminal.getWindow();
        this.canvas = new Canvas(terminal);
        states = new ArrayDeque<>();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Window getWindow() {
        return window;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void pushState(State state) {
        states.push(state);
    }

    public void removeFirstState() {
        states.removeFirst();
    }

    @Override
    public boolean keyPressed(Keyboard key) {
        return states.peekFirst().keyPressed(key);
    }

    @Override
    public boolean keyDown(Keyboard key) {
        return states.peekFirst().keyDown(key);
    }

    @Override
    public boolean mouseHover(DiscreteCoordinates hover) {
        return states.peekFirst().mouseHover(hover);

    }

    @Override
    public boolean mouseClick(DiscreteCoordinates click) {
        return states.peekFirst().mouseClick(click);
    }

    @Override
    public void render() {
        terminal.render();
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
