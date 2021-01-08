/**
 *  Author:     Leonard Cseres
 *  Date:       Fri Jan 08 2021
 *  Time:       15:36:10
 */

package com.leo.application.snakegame.states;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.maths.Orientation;
import com.leo.application.snakegame.SnakeGame;
import com.leo.application.utils.Audio;
import com.leo.application.utils.Color;
import com.leo.application.utils.Terminal;
import com.leo.application.window.Keyboard;

public class PlayState extends SnakeGameState {
    private static final int SNAKE_FRAME_SPEED = 3;
    private final Random random = new Random();
    private final DiscreteCoordinates spawnCoordinates;
    private final Deque<List<DiscreteCoordinates>> snake = new ArrayDeque<>();
    private Orientation currentOrientation = null;
    private Orientation desiredOrientation = null;
    private List<DiscreteCoordinates> foodPosition = new ArrayList<>();
    private int frameCount = 0;
    private boolean isFoodEaten = false;

    public PlayState(SnakeGame snakegame) {
        super(snakegame);
        spawnCoordinates = new DiscreteCoordinates(getCanvas().getWidth() / 2, getCanvas().getHeight());
        snake.push(getOccupationList(spawnCoordinates));
        placeRandomFood();
    }

    private List<DiscreteCoordinates> getOccupationList(DiscreteCoordinates coord) {
        List<DiscreteCoordinates> toReturn = new ArrayList<>();
        toReturn.add(new DiscreteCoordinates(coord.x, coord.y));
        toReturn.add(new DiscreteCoordinates(coord.x, coord.y + 1));
        toReturn.add(new DiscreteCoordinates(coord.x + 1, coord.y));
        toReturn.add(new DiscreteCoordinates(coord.x + 1, coord.y + 1));

        return toReturn;
    }

    private List<DiscreteCoordinates> getEmptyCoordinates() {
        List<DiscreteCoordinates> emptyCoords = new ArrayList<>();
        for (int x = 0; x < getCanvas().getWidth(); x += 2) {
            for (int y = 0; y < getCanvas().getHeight() * 2; y += 2) {
                DiscreteCoordinates coord = new DiscreteCoordinates(x, y);
                boolean isEmpty = true;
                for (List<DiscreteCoordinates> snakeCoord : snake) {
                    if (snakeCoord.contains(coord)) {
                        isEmpty = false;
                    }
                }
                if (isEmpty) {
                    emptyCoords.add(coord);
                }

            }
        }

        return emptyCoords;
    }

    private void placeRandomFood() {
        List<DiscreteCoordinates> emptyCoords = getEmptyCoordinates();

        DiscreteCoordinates randCoord = emptyCoords.get(random.nextInt(emptyCoords.size()));
        foodPosition = getOccupationList(randCoord);
    }

    private void eatFood() {
        snake.addLast(foodPosition);
        placeRandomFood();

    }

    private void reset() {
        snake.clear();
        snake.push(getOccupationList(spawnCoordinates));
        currentOrientation = null;
        placeRandomFood();
    }

    @Override
    public boolean keyDown(Keyboard key) {
        switch (key) {
            case ESC:
                Terminal.bip(Audio.MENU_CLICK);
                super.getSnakeGame().end();
                break;
            case UP:
                desiredOrientation = Orientation.UP;
                break;
            case DOWN:
                desiredOrientation = Orientation.DOWN;
                break;
            case LEFT:
                desiredOrientation = Orientation.LEFT;
                break;
            case RIGHT:
                desiredOrientation = Orientation.RIGHT;
                break;
            case SPACE:
                desiredOrientation = null;
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void update() {
        // Switch orientation
        if (snake.peek().get(0).x % 2 == 0 && snake.peek().get(0).y % 2 == 0) {
            currentOrientation = desiredOrientation;
        }

        ++frameCount;
        if (frameCount >= SNAKE_FRAME_SPEED) {
            moveSnake();
            frameCount = 0;
        }

        if (Objects.equals(snake.peek(), foodPosition)) {
            eatFood();
        }

        for (List<DiscreteCoordinates> coords : snake) {
            boolean isHead = Objects.equals(coords, snake.peek());
            Color color = null;
            int priority = 4;
            if (isHead) {
                color = Color.RED;
                priority = 3;
            } else {
                color = Color.WHITE;
            }
            for (DiscreteCoordinates coord : coords) {
                getCanvas().requestPixelChange(new DiscreteCoordinates(coord.x, coord.y), color, priority);
            }
        }

        for (DiscreteCoordinates foodCoord : foodPosition) {
            getCanvas().requestPixelChange(new DiscreteCoordinates(foodCoord.x, foodCoord.y), Color.GREEN, 1);
        }
    }

    private void moveSnake() {
        if (currentOrientation != null) {
            List<DiscreteCoordinates> newHeadCoords = snake.peek();
            boolean touchedWalls = false;
            for (DiscreteCoordinates headCoord : newHeadCoords) {
                if (getCanvas().isOutOfBound(headCoord.x, headCoord.y / 2)) {
                    touchedWalls = true;
                    break;
                }
            }
            if (!touchedWalls) {
                snake.push(getOccupationList(
                        newHeadCoords.get(0).jump(currentOrientation.coord.x, currentOrientation.coord.y)));
                snake.removeLast();
            } else {
                reset();
            }
        }
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub

    }

}
