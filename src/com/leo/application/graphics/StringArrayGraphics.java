/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        18:10
 */


package com.leo.application.graphics;

import com.leo.application.Updatable;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Colors;
import com.leo.application.window.Canvas;
import com.leo.application.window.Cell;

public class StringArrayGraphics implements Updatable {
    private final int priority = 10;
    private final Canvas canvas;
    private final DiscreteCoordinates coordinates;
    private char[][] array;
    private final Colors color;


    public StringArrayGraphics(Canvas canvas, DiscreteCoordinates coordinates, String[] array) {
        this(canvas, coordinates, array, null);
    }

    public StringArrayGraphics(Canvas canvas, DiscreteCoordinates coordinates, String[] array, Colors color) {
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
                canvas.requestCellChange(new Cell(new DiscreteCoordinates(coordinates.x + j, coordinates.y + i), array[i][j], color, priority));
            }
        }
    }

}
