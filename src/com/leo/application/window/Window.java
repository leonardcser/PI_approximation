/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        16:01
 */


package com.leo.application.window;

import com.leo.application.Graphics;
import com.leo.application.utils.Terminal;

public class Window implements Graphics {
    private final int width;
    private final int height;

    public Window(int width, int height) {
        Terminal.init();
        // Resize terminal window
        Terminal.setSize(width, height);
        Terminal.setFullScreen();
        // Terminal.moveToTopLeft();
        
        this.width = Terminal.getWidth();
        this.height = Terminal.getHeight();
    }

    @Override
    public void render() {
        Terminal.resetCursorPos();
    }

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
