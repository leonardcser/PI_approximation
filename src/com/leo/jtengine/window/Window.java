/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        16:01
 */

package com.leo.jtengine.window;

public class Window {
    private int initWidth;
    private int initHeight;
    private int width;
    private int height;
    private boolean fullScreen = false;
    public Window() {
        this(0, 0);
    }
    public Window(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen() {
        this.fullScreen = true;
    }

    public int getInitWidth() {
        return initWidth;
    }

    public void setInitWidth(int initWidth) {
        this.initWidth = initWidth;
    }

    public int getInitHeight() {
        return initHeight;
    }

    public void setInitHeight(int initHeight) {
        this.initHeight = initHeight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
