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

import com.leo.application.graphics.TextGraphics;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.maths.Orientation;
import com.leo.application.snakegame.SnakeGame;
import com.leo.application.utils.Audio;
import com.leo.application.utils.Color;
import com.leo.application.utils.Terminal;
import com.leo.application.window.Keyboard;

public class PlayState extends SnakeGameState {
    private static final int DEATH_TIMEOUT = 60;
    private static final int SNAKE_FRAME_SPEED = 2;
    private static final int FOOD_LENGTH = 2;
    private final Random random = new Random();
    private final DiscreteCoordinates spawnCoordinates;
    private final Deque<List<DiscreteCoordinates>> snake = new ArrayDeque<>();
    private Orientation currentOrientation = null;
    private Orientation desiredOrientation = null;
    private List<DiscreteCoordinates> foodPosition = new ArrayList<>();
    private int frameCount = 0;
    private int timeoutCount = 0;
    private boolean isDead = false;
    private boolean isFoodEaten = false;
    private int foodLengthCouter = 0;
    private int snakeLength = 0;
    private int hightscore = 0;
    private final TextGraphics scoreText;
    private final TextGraphics highScoreText;

    public PlayState(SnakeGame snakegame) {
        super(snakegame);
        spawnCoordinates = new DiscreteCoordinates(getCanvas().getWidth() / 2, getCanvas().getHeight());
        snake.push(getOccupationList(spawnCoordinates));
        scoreText = new TextGraphics(getCanvas(), new DiscreteCoordinates(1, getCanvas().getHeight() - 2), "Score: 0");
        highScoreText = new TextGraphics(getCanvas(), new DiscreteCoordinates(getCanvas().getWidth() - 16, getCanvas().getHeight() - 2), "");
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
        for (int x = 2; x < getCanvas().getWidth() - 2; x += 2) {
            for (int y = 2; y < getCanvas().getHeight() * 2 - 8; y += 2) {
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

    private void reset() {
        if (snakeLength > hightscore) {
            hightscore = snakeLength;
            highScoreText.setText("High Score: " + hightscore);
        }
        snakeLength = 0;
        scoreText.setText("Score: 0");
        snake.clear();
        snake.push(getOccupationList(spawnCoordinates));
        desiredOrientation = null;
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
                currentOrientation = null;
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void update() {
        // Switch orientation
        if (!isDead) {
            if ((snake.peek().get(0).x % 2 == 0) && (snake.peek().get(0).y % 2 == 0)) {
                currentOrientation = desiredOrientation;
            }

            ++frameCount;
            if (frameCount >= SNAKE_FRAME_SPEED) {
                moveSnake();
                frameCount = 0;
            }

            if (Objects.equals(snake.peek(), foodPosition)) {
                isFoodEaten = true;
                placeRandomFood();
                ++snakeLength;
                scoreText.setText("Score: " + snakeLength);
            }

            // CHECK IF COLLISION WITH ITSELF
            List<DiscreteCoordinates> totalSnake = new ArrayList<>();
            for (List<DiscreteCoordinates> coords : snake) {
                totalSnake.addAll(coords);
            }
            if (countHeadMatches(totalSnake) > 8) {
                isDead = true;
            }
        } else {
            ++timeoutCount;
            if (timeoutCount >= DEATH_TIMEOUT) {
                timeoutCount = 0;
                isDead = false;
                reset();
            }
        }

        for (List<DiscreteCoordinates> coords : snake) {
            Color color = null;
            int priority = 2;
            boolean isHead = Objects.equals(coords, snake.peek());
            if (isDead) {
                color = Color.DARK_RED;
                priority = 5;
            } else if (isHead) {
                color = Color.GREEN;
                priority = 5;
            } else {
                color = Color.DARK_GREEN;
            }

            for (DiscreteCoordinates coord : coords) {
                getCanvas().requestPixelChange(new DiscreteCoordinates(coord.x, coord.y), color, priority);
            }
        }

        for (DiscreteCoordinates foodCoord : foodPosition) {
            getCanvas().requestPixelChange(new DiscreteCoordinates(foodCoord.x, foodCoord.y), Color.RED, 1);
        }

        // draw border
        for (int x = 0; x < getCanvas().getWidth(); ++x) {
            getCanvas().requestPixelChange(new DiscreteCoordinates(x, 0), Color.WHITE, 10);
            getCanvas().requestPixelChange(new DiscreteCoordinates(x, getCanvas().getHeight() * 2 - 7), Color.WHITE,
                    10);
        }
        for (int y = 0; y < getCanvas().getHeight() * 2 - 7; ++y) {
            getCanvas().requestPixelChange(new DiscreteCoordinates(0, y), Color.WHITE, 10);
            getCanvas().requestPixelChange(new DiscreteCoordinates(getCanvas().getWidth() - 1, y), Color.WHITE, 10);
        }
        

        scoreText.update();
        highScoreText.update();
    }

    private int countHeadMatches(List<DiscreteCoordinates> coords) {
        int count = 0;
        for (DiscreteCoordinates coord : coords) {
            if (snake.peek().contains(coord)) {
                ++count;
            }
        }

        return count;

    }

    private void moveSnake() {
        if (currentOrientation != null) {
            List<DiscreteCoordinates> newHeadCoords = snake.peek();
            boolean touchedWalls = false;
            for (DiscreteCoordinates headCoord : newHeadCoords) {
                if (headCoord.x < 2 || headCoord.x > getCanvas().getWidth() - 2 || headCoord.y < 2
                        || headCoord.y > getCanvas().getHeight() * 2 - 8) {
                    touchedWalls = true;
                    break;
                }
            }
            if (!touchedWalls) {
                snake.push(getOccupationList(
                        newHeadCoords.get(0).jump(currentOrientation.coord.x, currentOrientation.coord.y)));
                if (!isFoodEaten) {
                    snake.removeLast();
                } else {
                    ++foodLengthCouter;
                    if (foodLengthCouter >= FOOD_LENGTH) {
                        foodLengthCouter = 0;
                        isFoodEaten = false;
                    }
                }
            } else {
                isDead = true;
            }
        }
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub
    }

}
