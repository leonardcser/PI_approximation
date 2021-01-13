package com.leo.jtengine.maths;

public enum Orientation {
    UP(new DiscreteCoordinates(0, -1)),
    DOWN(new DiscreteCoordinates(0, 1)),
    LEFT(new DiscreteCoordinates(-1, 0)),
    RIGHT(new DiscreteCoordinates(1, 0));

    public final DiscreteCoordinates coord;

    private Orientation(DiscreteCoordinates coordinates) {
        coord = coordinates;
    }

}