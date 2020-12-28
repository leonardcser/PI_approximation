package com.leo.application.utils;

public enum Colors {
    GREEN("\u001b[38;5;76m"),
    RED("\u001b[38;5;203m"),
    DARK_CYAN("\u001b[38;5;73m"),
    RESET("\u001b[0m");

    public String value;

    Colors(String color) {
        value = color;
    }
}
