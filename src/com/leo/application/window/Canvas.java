/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        18:14
 */


package com.leo.application.window;

import com.leo.application.Graphics;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Colors;

public class Canvas implements Graphics {
    private final int width;
    private final int height;
    private final Cell[][] canvas;
    private final StringBuilder builder = new StringBuilder();
    private int noChanges;
    private static final int RENDER_BUFFER = 25;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        canvas = new Cell[height][width];
        clear();
    }

    public void clear() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                canvas[y][x] = new Cell(new DiscreteCoordinates(x, y), ' ', null);
            }
        }
    }

    public int getHeight() {
        return canvas.length;
    }

    public int getWidth() {
        return canvas[0].length;
    }

    public void setCell(Cell cell) {
        canvas[cell.coordinates.y][cell.coordinates.x] = cell;
    }

    public Cell getCell(DiscreteCoordinates coor) {
        return canvas[coor.y][coor.x];
    }

    public boolean isOutOfBound(DiscreteCoordinates coor) {
        return (coor.y < 0) || (coor.x < 0) || (coor.y > height - 1) || (coor.x > width - 1);
    }

    @Override
    public void render() {
        StringBuilder tmpBuilder = new StringBuilder(builder);
        builder.setLength(0);
        for (Cell[] line : canvas) {
            for (Cell cell : line) {
                if (cell.color != null) {
                    builder.append(cell.color.value);
                    builder.append(cell.value);
                    builder.append(Colors.RESET.value);
                } else {
                    builder.append(cell.value);
                }
            }
            // Carriage return
            builder.append("\u001b[1E");
        }

        if (builder.toString().equals(tmpBuilder.toString())) {
            noChanges = noChanges < RENDER_BUFFER ? (noChanges + 1) : noChanges;
        } else {
            noChanges = 0;
        }
        if (noChanges < RENDER_BUFFER) {
            System.out.print(builder);
            clear();
        }
    }
}
