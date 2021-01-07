/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        18:14
 */

package com.leo.application.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leo.application.Graphics;
import com.leo.application.Updatable;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Colors;
import com.leo.application.utils.Terminal;
import com.leo.application.window.Cell.Pixel;

public class Canvas implements Updatable, Graphics {
    private final int width;
    private final int height;
    private final Cell[][] canvas;
    private final Map<DiscreteCoordinates, Cell> changeRequests = new HashMap<>();
    private final StringBuilder builder = new StringBuilder();
    private Colors background = Colors.WHITE;
    private List<Integer> priorityList = new ArrayList<>();

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        canvas = new Cell[height][width];
        clearCanvas();
    }

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
        Collections.sort(priorityList);
        if (!changeRequests.isEmpty()) {
            do {
                int minPriority = priorityList.get(0);
                for (Map.Entry<DiscreteCoordinates, Cell> request : changeRequests.entrySet()) {
                    if (request.getValue().priority == minPriority) {
                        setCell(request.getValue());
                    }
                }
                changeRequests.entrySet().removeIf(e -> e.getValue().priority == minPriority);
                priorityList.remove(0);
            } while (!changeRequests.isEmpty());
        }
    }

    private void setCell(Cell cell) {
        if (!isOutOfBound(cell)) {
            canvas[cell.coordinate.y][cell.coordinate.x] = cell;
        } else {
            Terminal.logErr("Setting cell outside of bounds: " + cell.toString());
        }
    }

    public void requestCellChange(Cell cell) {
        changeRequests.put(new DiscreteCoordinates(cell.coordinate.x, cell.coordinate.y), cell);
        updatePriorityList(cell.priority);
    }

    public void requestPixelChange(DiscreteCoordinates coord, Colors color, int priority) {
        Colors fg = background;
        Colors bg = background;
        if (color == null) {
            priority = 0;
            color = background;
        }

        boolean isEven = coord.y % 2 == 0;
        if (isEven) {
            fg = color;
        } else {
            bg = color;
        }

        DiscreteCoordinates reScaled = new DiscreteCoordinates(coord.x, coord.y / 2);
        if (changeRequests.get(reScaled) != null) {
            Cell oldCell = changeRequests.get(reScaled);
            if (isEven) {
                bg = oldCell.pixel.background;
            } else {
                fg = oldCell.pixel.forground;
            }
            changeRequests.replace(reScaled, new Cell(reScaled, new Pixel('▀', fg, bg), priority));

        } else {
            changeRequests.put(reScaled, new Cell(reScaled, new Pixel('▀', fg, bg), priority));
        }
        updatePriorityList(priority);
    }

    private void updatePriorityList(int priority) {
        if (!priorityList.contains(priority)) {
            priorityList.add(priority);
        }
    }

    @Override
    public void render() {
        String tmpBuilder = builder.toString();
        builder.setLength(0);
        for (Cell[] line : canvas) {
            for (Cell cell : line) {
                if (!cell.hasForground() || cell.hasBackground()) {
                    builder.append(Colors.RESET.value);
                }
                if (cell.hasForground()) {
                    builder.append(cell.getForground());
                }
                if (cell.hasBackground()) {
                    builder.append(cell.getBackground());
                }
                builder.append(cell.getChar());

            }
            // Carriage return
            builder.append("\033[1E");
        }

        if (!builder.toString().equals(tmpBuilder.toString())) {
            String deletedChars = cleanBuilder(tmpBuilder);
            
            Terminal.write(builder.toString() + Colors.RESET.value);
            Terminal.flush();
            clearCanvas();
            builder.append(deletedChars);
        }
    }

    private String cleanBuilder(String tmpBuilder) {
        String deletedChars = "";
        if (builder.length() > 0 && tmpBuilder.length() > 0) {
            int builderIndex = builder.length() - 1;
            int tmpBuilderIndex = tmpBuilder.length() - 1;
            boolean exit = false;
            while (!exit) {
                if (builder.charAt(builderIndex) != tmpBuilder.charAt(tmpBuilderIndex)) {
                    exit = true;
                } else {
                    --builderIndex;
                    --tmpBuilderIndex;
                }
            }
            int[] bounds = new int[] { builderIndex < builder.length() - 16 ? builderIndex + 15 : builder.length(),
                    builder.length() };

            deletedChars = builder.substring(bounds[0], bounds[1]);
            builder.delete(bounds[0], bounds[1]);
        }

        return deletedChars;
    }

}