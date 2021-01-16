package com.leo.jtengine.window.render;

public class Color {
    // TODO: add default colors
    public static final String WHITE = "\033[38;5;255m";
    public static final String BOLD = "\033[1m";
    public static final String RESET = "\033[0m";
    public static final String INVISIBLE = "\033[7m";

    private final String value;

    public Color(String value) {
        this.value = value;
    }

    public Color(int id) {
        this(XtermUtils.getExtendedFgColor(id));
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
