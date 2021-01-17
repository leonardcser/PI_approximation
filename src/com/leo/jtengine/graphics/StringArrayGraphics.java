/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        18:10
 */

package com.leo.jtengine.graphics;

import com.leo.jtengine.Updatable;
import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.window.render.Canvas;
import com.leo.jtengine.window.render.Cell;
import com.leo.jtengine.window.render.Color;

public class StringArrayGraphics implements Updatable {
    private final int priority = 10;
    private final boolean transparent = true;
    private final Canvas canvas;
    private DiscreteCoordinates coordinates;
    private Color color;
    private final char[][] array;

    public void setXCentered() {
        coordinates = new DiscreteCoordinates((canvas.getWidth() / 2) - (array[getMaxIndex()].length / 2), coordinates.y);
    }

    public void setYCentered() {
        coordinates = new DiscreteCoordinates(coordinates.x, (canvas.getHeight() / 2) - (array.length / 2));
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private int getMaxIndex() {
        int maxIndex = 0;
        int maxLength = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i].length > maxLength) {
                maxLength = array[i].length;
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public StringArrayGraphics(Canvas canvas, DiscreteCoordinates coordinates, String[] array) {
        this(canvas, coordinates, array, null);
    }

    public StringArrayGraphics(Canvas canvas, DiscreteCoordinates coordinates, String[] array, Color color) {
        this.canvas = canvas;
        this.coordinates = coordinates;
        this.array = new char[array.length][];
        for (int i = 0; i < array.length; ++i) {
            this.array[i] = array[i].toCharArray();
        }
        this.color = color;
    }

    @Override
    public void update() {
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[i].length; ++j) {
                if (!transparent || array[i][j] != ' ') {
                    canvas.requestCellChange(new Cell(new DiscreteCoordinates(coordinates.x + j, coordinates.y + i),
                            array[i][j], color, priority));
                }
            }
        }
    }

}
