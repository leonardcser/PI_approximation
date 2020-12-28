/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        16:01
 */


package com.leo.application.window;

import com.leo.application.Graphics;
import com.leo.application.utils.Terminal;

public class Window implements Graphics {
    public Window(int width, int height) {
        Terminal.executeCmd("printf '\\e[8;" + (height + 1) + ";" + width + "t'");
        Terminal.executeCmd("clear");
    }

    @Override
    public void render() {
        Terminal.resetCursorPos();
    }
}
