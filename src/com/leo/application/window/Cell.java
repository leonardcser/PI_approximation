/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        22:30
 */

package com.leo.application.window;

import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Color;

public class Cell {
    public final DiscreteCoordinates coordinate;
    public final Pixel pixel;
    public final int priority;

    public Cell(DiscreteCoordinates coordinate, char character, Color color, int priority) {
        this(coordinate, new Pixel(character, color, null), priority);
    }

    public Cell(DiscreteCoordinates coordinate, Pixel pixel, int priority) {
        this.coordinate = coordinate;
        this.pixel = pixel;
        this.priority = priority;
    }

    public boolean hasForground() {
        return pixel.forground != null;
    }

    public boolean hasBackground() {
        return pixel.background != null;
    }

    public String getForground() {
        if (hasForground()) {
            return pixel.forground.value;
        }
        return "";
    }

    public String getBackground() {
        if (hasBackground()) {
            return pixel.background.value.replaceFirst("38", "48");
        }
        return "";
    }

    public char getChar() {
        return pixel.character;
    }

    public boolean hasColor() {
        return hasForground() || hasBackground();
    }

    @Override
    public String toString() {
        return "Cell{" + "coordinate=" + coordinate + ", char=" + pixel.character + ", fg=" + pixel.forground + ", bg="
                + pixel.background + '}';
    }

    protected static class Pixel {
        protected final char character;
        protected final Color forground;
        protected final Color background;

        protected Pixel(char character, Color forground, Color background) {
            this.character = character;
            this.forground = forground;
            this.background = background;
        }
    }

}
