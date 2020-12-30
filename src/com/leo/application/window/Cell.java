/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        22:30
 */


package com.leo.application.window;

import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Colors;

public class Cell {
    public final DiscreteCoordinates coordinates;
    public final char value;
    public final Colors color;

    public Cell(DiscreteCoordinates coordinates, char value, Colors color) {
        this.coordinates = coordinates;
        this.value = value;
        this.color = color;
    }

    public boolean hasColor() {
        return color != null;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "coordinates=" + coordinates +
                ", value=" + value +
                ", color=" + color +
                '}';
    }
}
