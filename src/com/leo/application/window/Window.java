/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        16:01
 */

package com.leo.application.window;

import com.leo.application.Graphics;
import com.leo.application.utils.Terminal;

public class Window implements Graphics {
    private int width;
    private int height;

    public Window(int width, int height) {
        Terminal.init();
        Terminal.setSize(width, height);

        this.width = width;
        this.height = height;
    }

    public Window() {
        Terminal.init();

        this.width = Terminal.getWidth();
        this.height = Terminal.getHeight();
    }

    @Override
    public void render() {
        Terminal.resetCursorPos();
    }

    public void setFullScreen() {
        Terminal.setFullScreen();
        width = Terminal.getWidth();
        height = Terminal.getHeight();
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
