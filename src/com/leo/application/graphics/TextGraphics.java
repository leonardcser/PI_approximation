/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        00:48
 */


package com.leo.application.graphics;

import com.leo.application.Updatable;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Color;
import com.leo.application.window.Canvas;
import com.leo.application.window.Cell;

public class TextGraphics implements Updatable {
    private final int priority = 10;
    private final Canvas canvas;
    private final DiscreteCoordinates coordinates;
    private char[] text;
    private Color color;

    public TextGraphics(Canvas canvas, DiscreteCoordinates coordinates, String text) {
        this(canvas, coordinates, text, null);
    }


    public TextGraphics(Canvas canvas, DiscreteCoordinates coordinates, String text, Color color) {
        this.canvas = canvas;
        this.coordinates = coordinates;
        this.text = text.toCharArray();
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setText(String text) {
        this.text = text.toCharArray();
    }

    @Override
    public void update() {
        for (int i = 0; i < text.length; ++i) {
            canvas.requestCellChange(new Cell(new DiscreteCoordinates(coordinates.x + i, coordinates.y), text[i], color,
                                 priority));
        }
    }

}
