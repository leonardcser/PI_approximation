package com.leo.jtengine.window.render;

public class Color {
    // TODO: add default colors
    public static final String RED = Xterm.get8BitFgColor(197);
    public static final String GREEN = Xterm.get8BitFgColor(154);
    public static final String YELLOW = Xterm.get8BitFgColor(227);
    public static final String BLUE = Xterm.get8BitFgColor(33);
    public static final String BACK = Xterm.get8BitFgColor(0);
    public static final String WHITE = Xterm.get8BitFgColor(231);
    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";
    public static final String DIM = "\033[2m";
    public static final String ITALIC = "\033[3m";
    public static final String UNDERLINE = "\033[4m";
    public static final String SLOW_BINK = "\033[5m";
    public static final String RAPID_BINK = "\033[6m";
    public static final String INVISIBLE = "\033[7m";
    public static final String STROKED = "\033[9m";

    private final String value;

    public Color(String value) {
        this.value = value;
    }

    public Color(int id) {
        this(Xterm.get8BitFgColor(id));
    }

    public Color(int r, int g, int b) {
        this(Xterm.get24BitFgColor(r, g, b));
    }

    public boolean compare(Color other) {
        try {
            if (other.value.equals(value)) {
                return true;
            }
        } catch (NullPointerException ignore) {
            // Null value
        }
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}
