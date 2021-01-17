package com.leo.jtengine.window.render;

public class Xterm {
    protected static final String CLEAR_SCREEN = "\033[2J";
    protected static final String HIDE_CURSOR = "\033[?25l";
    protected static final String SHOW_CURSOR = "\033[?25h";
    protected static final String SAVE_CURSOR_POS = "\033[s";
    protected static final String REQUEST_CURSOR_POS = "\033[6n";
    protected static final String RESTORE_CURSOR_POS = "\033[u";
    protected static final String ENABLE_SCREEN_BUFFER = "\033[?1049h";
    protected static final String DISABLE_SCREEN_BUFFER = "\033[?1049l";
    protected static final String ENABLE_MOUSE_EVENTS = "\033[?1000;1003;1006;1015h";
    protected static final String DISABLE_MOUSE_EVENTS = "\033[?1000;1003;1006;1015l";
    
    private Xterm() {
        throw new IllegalStateException("Utility class");
    }

    protected static String moveWindow(int x, int y) {
        return String.format("\033[3;%d;%dt", y, x);
    }

    protected static String moveCursor(int x, int y) {
        return String.format("\033[%d;%df", y, x);
    }

    protected static String resizeWindow(int width, int height) {
        return String.format("\033[8;%d;%dt", height, width);
    }

    protected static String setProfile(String name) {
        return String.format("\033]50;SetProfile=%s\007", name);
    }

    protected static String setTitle(String title) {
        return String.format("\033]2;%s\u0007", title);
    }

    protected static String moveDownAtStart(int lines) {
        return String.format("\033[%dE", lines);
    }

    protected static String moveUpAtStart(int lines) {
        return String.format("\033[%dF", lines);
    }

    public static String get8BitFgColor(int id) {
        return String.format("\033[38;5;%dm", id);
    }

    public static String get8BitBgColor(int id) {
        return String.format("\033[48;5;%dm", id);
    }

	public static String get24BitFgColor(int r, int g, int b) {
		return String.format("\033[38;2;%d;%d;%d", r, g, b);
    }
    
    public static String get24BitBgColor(int r, int g, int b) {
		return String.format("\033[48;2;%d;%d;%d", r, g, b);
	}
}
