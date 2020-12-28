/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        17:21
 */


package com.leo.application.graphics;

public class Circle extends Shape {
    private final String[] circle = {
                    "                              ▓▓▓▓▓▓▓▓▓▓                              ",
                    "                        ██████          ██████                        ",
                    "                    ████                      ████                    ",
                    "                ████                              ████                ",
                    "              ██                                      ██              ",
                    "            ██                                          ██            ",
                    "          ██                                              ██          ",
                    "        ██                                                  ██        ",
                    "      ██                                                      ██      ",
                    "      ██                                                      ██      ",
                    "    ██                                                          ██    ",
                    "    ██                                                          ██    ",
                    "  ██                                                              ██  ",
                    "  ██                                                              ██  ",
                    "  ██                                                              ██  ",
                    "██                                                                  ██",
                    "██                                                                  ██",
                    "██                                                                  ██",
                    "██                                                                  ██",
                    "██                                                                  ██",
                    "██                                                                  ██",
                    "  ██                                                              ██  ",
                    "  ██                                                              ██  ",
                    "  ██                                                              ██  ",
                    "    ██                                                          ██    ",
                    "    ██                                                          ██    ",
                    "      ██                                                      ██      ",
                    "      ██                                                      ██      ",
                    "        ██                                                  ██        ",
                    "          ██                                              ██          ",
                    "            ██                                          ██            ",
                    "              ██                                      ██              ",
                    "                ████                              ████                ",
                    "                    ████                      ████                    ",
                    "                        ██████          ██████                        ",
                    "                              ▓▓▓▓▓▓▓▓▓▓                              "};

    @Override
    public void draw() {
        System.out.println(circle);
    }

    @Override
    public String getString() {
        return "";
    }
}
