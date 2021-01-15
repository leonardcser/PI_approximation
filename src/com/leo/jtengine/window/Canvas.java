/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        18:14
 */

package com.leo.jtengine.window;

import com.leo.jtengine.Graphics;
import com.leo.jtengine.Updatable;
import com.leo.jtengine.maths.DiscreteCoordinates;
import com.leo.jtengine.utils.Color;
import com.leo.jtengine.utils.Terminal;
import com.leo.jtengine.utils.TerminalLogger;
import com.leo.jtengine.window.Cell.Pixel;

import java.util.*;

public class Canvas implements Updatable, Graphics {
    private final Terminal terminal;
    private final Cell[][] canvas;
    private final Map<DiscreteCoordinates, Cell> changeRequests = new HashMap<>();
    private final StringBuilder buffer = new StringBuilder();
    private final int width;
    private final int height;
    private final List<Integer> priorityList = new ArrayList<>();
    private Color background = null;

    public Canvas(Terminal terminal) {
        this.terminal = terminal;
        this.width = terminal.getWindow().getWidth();
        this.height = terminal.getWindow().getHeight();
        canvas = new Cell[height][width];
        clearCanvas();
    }

    public void clearCanvas() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                canvas[y][x] = new Cell(new DiscreteCoordinates(x, y), ' ', null, 0);
            }
        }
    }

    public void setBackground(Color background) {
        this.background = background;
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

    public boolean isOutOfBound(int x, int y) {
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
            TerminalLogger.logErr("Setting cell outside of bounds: " + cell.toString());
        }
    }

    public boolean isOutOfBound(Cell cell) {
        int x = cell.coordinate.x;
        int y = cell.coordinate.y;

        return (y < 0) || (x < 0) || (y > height - 1) || (x > width - 1);
    }

    public void requestCellChange(Cell cell) {
        changeRequests.put(new DiscreteCoordinates(cell.coordinate.x, cell.coordinate.y), cell);
        updatePriorityList(cell.priority);
    }

    private void updatePriorityList(int priority) {
        if (!priorityList.contains(priority)) {
            priorityList.add(priority);
        }
    }

    public void requestPixelChange(DiscreteCoordinates coord, Color color, int priority) {
        Color fg = background;
        Color bg = background;

        char pixelType;
        boolean isTopPixel = coord.y % 2 == 0;

        DiscreteCoordinates reScaled = new DiscreteCoordinates(coord.x, coord.y / 2);
        if (changeRequests.get(reScaled) != null) {
            Cell oldCell = changeRequests.get(reScaled);
            boolean hasForground = false;
            boolean hasBackground = false;
            if (color == null) {
                fg = oldCell.pixel.forground;
                bg = null; // bg = null
                if (isTopPixel) {
                    pixelType = '▄';
                } else {
                    pixelType = '▀';

                }
            } else if (isTopPixel) {
                pixelType = '▀';
                fg = color;
                bg = oldCell.pixel.forground;
                hasForground = true;
            } else {
                pixelType = oldCell.pixel.character;
                fg = oldCell.pixel.forground;
                bg = color;
                hasBackground = true;
            }
            if (oldCell.priority <= priority || (hasBackground && oldCell.pixel.background == null)
                    || (hasForground && oldCell.pixel.forground == null)) {
                changeRequests.replace(reScaled,
                        new Cell(reScaled, new Pixel(pixelType, fg, bg), Math.max(priority, oldCell.priority)));
            }
        } else if (color != null) {
            fg = color;
            if (isTopPixel) {
                // Top pixel
                pixelType = '▀';
                // bg = background
            } else {
                // Bottom pixel
                pixelType = '▄';
                // bg = background
            }
            changeRequests.put(reScaled, new Cell(reScaled, new Pixel(pixelType, fg, bg), priority));
        }
        updatePriorityList(priority);
    }

    @Override
    public void render() {
        String tmpBuffer = buffer.toString();
        buffer.setLength(0);
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                Cell cell = canvas[i][j];

                if (cell.hasForground()) {
                    buffer.append(cell.getForground());
                }
                if (cell.hasBackground()) {
                    buffer.append(cell.getBackground());
                }
                buffer.append(cell.getChar());
                if (cell.hasColor()) {
                    buffer.append(Color.RESET.value);
                }
            }
            // Carriage return
            buffer.append("\033[1E");
        }

        if (!buffer.toString().equals(tmpBuffer)) {
            String deletedChars = cleanBuffer(tmpBuffer);

            terminal.write(buffer.toString() + Color.RESET.value);
            terminal.flush();
            clearCanvas();
            buffer.append(deletedChars);
        }
    }

    // TODO: Use canvas instead...
    private String cleanBuffer(String tmpBuffer) {
        String deletedChars = "";
        if (buffer.length() > 0 && tmpBuffer.length() > 0) {
            int bufferIndex = buffer.length() - 1;
            int tmpBufferIndex = tmpBuffer.length() - 1;
            boolean exit = false;
            while (!exit) {
                if (buffer.charAt(bufferIndex) != tmpBuffer.charAt(tmpBufferIndex)) {
                    exit = true;
                } else {
                    --bufferIndex;
                    --tmpBufferIndex;
                }
            }
            int[] bounds = new int[] { bufferIndex < buffer.length() - 21 ? bufferIndex + 20 : buffer.length(),
                    buffer.length() };

            deletedChars = buffer.substring(bounds[0], bounds[1]);
            buffer.delete(bounds[0], bounds[1]);
        }

        return deletedChars;
    }

}