/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        00:48
 */


package com.leo.jtengine.graphics;

import com.leo.jtengine.Listener;
import com.leo.jtengine.Updatable;
import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.utils.TerminalLogger;
import com.leo.jtengine.window.render.Canvas;
import com.leo.jtengine.window.render.Cell;
import com.leo.jtengine.window.render.Color;

public class TextGraphics implements Updatable, Listener {
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

    @Override
    public boolean mouseHover(DiscreteCoordinates hover) {
        for (int i = 0; i < text.length; ++i) {
            if (hover.equals(new DiscreteCoordinates(coordinates.x + i, coordinates.y))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClick(DiscreteCoordinates click) {
        for (int i = 0; i < text.length; ++i) {
            if (click.equals(new DiscreteCoordinates(coordinates.x + i, coordinates.y))) {
                return true;
            }
        }
        return false;
    }

}
