package com.leo.jtengine.utils;

public enum Color {
    WHITE("\033[38;5;255m"),
    GREEN("\033[38;5;76m"),
    DARK_GREEN("\033[38;5;64m"),
    DARKEST_GREEN("\033[38;5;22m"),
    LIGHT_GREEN("\033[38;5;121m"),
    RED("\033[38;5;203m"),
    DARK_RED("\033[38;5;88m"),
    DARK_CYAN("\033[38;5;73m"),
    BLUE("\033[38;5;32m"),
    DARK_BLUE("\033[38;5;27m"),
    BOLD("\033[1m"),
    BLACK("\033[38;5;232m"),
    DARK_GREY("\033[38;5;233m"),
    GREY("\033[38;5;236m"),
    LIGHT_GREY("\033[38;5;243m"),
    RESET("\033[0m"),
    INVISIBLE("\033[7m");

    public final String value;

    Color(String color) {
        value = color;
    }

    public boolean compare(Color other) {
        try {
            if (other.value.equals(this.value)) {
                return true;
            }
        } catch (NullPointerException ignore) {
            // Null value
        }

        return false;
    }
}
