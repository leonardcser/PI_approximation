package com.leo.application.utils;

public enum Colors {
    WHITE("\u001b[38;5;255m"),
    GREEN("\u001b[38;5;76m"),
    LIGHT_GREEN("\u001b[38;5;121m"),
    RED("\u001b[38;5;203m"),
    DARK_CYAN("\u001b[38;5;73m"),
    BLUE("\u001b[38;5;32m"),
    DARK_BLUE("\u001b[38;5;27m"),
    BOLD("\u001b[1m"),
    BLACK("\u001b[38;5;232m"),
    DARK_GREY("\u001b[38;5;233m"),
    LIGHT_GREY("\u001b[38;5;243m"),
    RESET("\u001b[0m");

    public String value;

    Colors(String color) {
        value = color;
    }
}
