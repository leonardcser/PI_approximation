/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        18:14
 */

package com.leo.application.window;

import com.leo.application.Graphics;
import com.leo.application.Updatable;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Colors;
import com.leo.application.utils.Terminal;
import com.leo.application.window.Cell.Pixel;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Canvas implements Updatable, Graphics {
    private static final int RENDER_BUFFER = 5;
    private final int width;
    private final int height;
    private final Cell[][] canvas;
    private final Map<DiscreteCoordinates, Cell> changeRequests = new HashMap<>();
    private final StringBuilder builder = new StringBuilder();
    private int noChanges;
    private Colors background = Colors.WHITE;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        canvas = new Cell[height][width];
        clearCanvas();
    }

    /**
     * @param background the background to set
     */
    public void setBackground(Colors background) {
        this.background = background;
    }

    public void clearCanvas() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                canvas[y][x] = new Cell(new DiscreteCoordinates(x, y), ' ', null, 0);
            }
        }
    }

    public int getHeight() {
        return canvas.length;
    }

    public int getPixelHeight() {
        return canvas.length * 2;
    }

    public int getWidth() {
        return canvas[0].length;
    }

    public Cell getCell(DiscreteCoordinates coor) {
        return canvas[coor.y][coor.x];
    }

    public boolean isOutOfBound(Cell cell) {
        int x = cell.coordinate.x;
        int y = cell.coordinate.y;

        return (y < 0) || (x < 0) || (y > height - 1) || (x > width - 1);
    }

    @Override
    public void update() {
        if (!changeRequests.isEmpty()) {
            do {
                int lowestPriority = Collections.min(changeRequests.values(),
                        Comparator.comparingInt(e -> e.priority)).priority;

                for (Map.Entry<DiscreteCoordinates, Cell> request : changeRequests.entrySet()) {
                    if (request.getValue().priority == lowestPriority) {
                        setCell(request.getValue());
                    }
                }
                changeRequests.entrySet().removeIf(e -> e.getValue().priority == lowestPriority);
            } while (!changeRequests.isEmpty());
        }

    }

    private void setCell(Cell cell) {
        if (!isOutOfBound(cell)) {
            canvas[cell.coordinate.y][cell.coordinate.x] = cell;
        } else {
            Terminal.logErr(new IndexOutOfBoundsException(), "Setting cell outside of bounds...");
        }
    }

    public void requestCellChange(Cell cell) {
        changeRequests.put(new DiscreteCoordinates(cell.coordinate.x, cell.coordinate.y), cell);
    }

    public void requestPixelChange(DiscreteCoordinates coord, Colors color, int priority) {
        Colors fg = background;
        Colors bg = background;
        if (color == null) {
            priority = 0;
            color = background;
        }

        if (coord.y % 2 == 0) {
            fg = color;
        } else {
            bg = color;
        }

        DiscreteCoordinates reScaled = new DiscreteCoordinates(coord.x, coord.y / 2);
        if (changeRequests.get(reScaled) != null) {
            Cell oldCell = changeRequests.get(reScaled);
            if (coord.y % 2 == 0) {
                bg = oldCell.pixel.background;
            } else {
                fg = oldCell.pixel.forground;
            }
            changeRequests.replace(reScaled, new Cell(reScaled, new Pixel('▀', fg, bg), priority));

        } else {
            changeRequests.put(reScaled, new Cell(reScaled, new Pixel('▀', fg, bg), priority));
        }
    }

    @Override
    public void render() {
        StringBuilder tmpBuilder = new StringBuilder(builder);
        builder.setLength(0);
        for (Cell[] line : canvas) {
            for (Cell cell : line) {
                if (cell.hasForground()) {
                    builder.append(cell.getForground());
                }
                if (cell.hasBackground()) {
                    builder.append(cell.getBackground());
                }
                builder.append(cell.getChar());
                if (cell.hasColor()) {
                    builder.append(Colors.RESET.value);
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
            Terminal.write(builder.toString());
            Terminal.flush();
            clearCanvas();
        }
    }

}