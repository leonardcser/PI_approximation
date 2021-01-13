package com.leo.jtengine.window;

import java.util.Arrays;

public enum Keyboard {
    ENTER(new Integer[]{13}),
    ESC(new Integer[]{27}),
    SPACE(new Integer[]{32}),
    NUM_1(new Integer[]{49}),
    NUM_2(new Integer[]{50}),
    NUM_3(new Integer[]{51}),
    A(new Integer[]{97}),
    B(new Integer[]{98}),
    C(new Integer[]{99}),
    D(new Integer[]{100}),
    E(new Integer[]{101}),
    F(new Integer[]{102}),
    G(new Integer[]{103}),
    H(new Integer[]{104}),
    I(new Integer[]{105}),
    J(new Integer[]{106}),
    K(new Integer[]{107}),
    L(new Integer[]{108}),
    M(new Integer[]{109}),
    N(new Integer[]{110}),
    O(new Integer[]{111}),
    P(new Integer[]{112}),
    Q(new Integer[]{113}),
    R(new Integer[]{114}),
    S(new Integer[]{115}),
    T(new Integer[]{116}),
    U(new Integer[]{117}),
    V(new Integer[]{118}),
    W(new Integer[]{119}),
    X(new Integer[]{120}),
    Y(new Integer[]{121}),
    Z(new Integer[]{122}),
    UP(new Integer[]{27,91,65}),
    DOWN(new Integer[]{27,91,66}),
    RIGHT(new Integer[]{27,91,67}),
    LEFT(new Integer[]{27,91,68});

    public Integer[] keyCode;

    Keyboard(Integer[] keyCode) {
        this.keyCode = keyCode;
    }

    public static Keyboard get(Integer[] key) {
        for (Keyboard toCompare : Keyboard.values()) {
            if (Arrays.equals(toCompare.keyCode, key)) {
                return toCompare;
            }
        }
        return null;
    }
}