package com.leo.jtengine;

import com.leo.jtengine.snakegame.SnakeGame;
import com.leo.jtengine.utils.Terminal;
import com.leo.jtengine.visualiserapp.AlgorithmVisualiser;
import com.leo.jtengine.window.Window;

public class Run {
    public static void main(String[] args) {
        Window window = new Window();
        // Window window = new Window(159, 45);
        // Window window = new Window(40, 24);
        window.setFullScreen();
        Terminal terminal = new Terminal(window);
    //    new Loop(new AlgorithmVisualiser(terminal)).start();
        new Loop(new SnakeGame(terminal)).start();
    }
}
