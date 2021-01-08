/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:53
 */

package com.leo.application.visualiserapp.states;

import com.leo.application.graphics.StringArrayGraphics;
import com.leo.application.graphics.TextGraphics;
import com.leo.application.io.AsciiFileReader;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Audio;
import com.leo.application.utils.Color;
import com.leo.application.utils.Terminal;
import com.leo.application.visualiserapp.AlgorithmVisualiser;
import com.leo.application.visualiserapp.algorithms.sorting.SortingAlgorithm;
import com.leo.application.window.Keyboard;

public class MenuState extends AlgorithmVisualiserState {
    private static final String[] MENU_TITLES_TEXT = new String[] { "[1] Sorting Algorithms", "[2] Maze generation",
            "[3] Pathfinding Algorithms" };
    private static final String[] MENU_OPTIONS_SORTING_TEXT = new String[] { "- Bubble Sort", "- Quick Sort" };
    private static final String[] MENU_OPTIONS_MAZE_TEXT = new String[] { "- Recursive Back Tracking" };

    private final StringArrayGraphics mainTitle;
    private final TextGraphics[] menuTitles = new TextGraphics[MENU_TITLES_TEXT.length];
    private final TextGraphics[] menuOptionsSorting = new TextGraphics[MENU_OPTIONS_SORTING_TEXT.length];
    private final TextGraphics[] menuOptionsMaze = new TextGraphics[MENU_OPTIONS_MAZE_TEXT.length];
    private final StringArrayGraphics sortingThumbnail;
    private final StringArrayGraphics mazeThumbnail;
    private final StringArrayGraphics divider;
    private final TextGraphics helpArrows;
    private final TextGraphics helpReset;
    private final TextGraphics helpEnter;
    private final TextGraphics helpEsc;
    private int currentSelection = 0;
    private int currentColumn = 0;

    public MenuState(AlgorithmVisualiser algorithmVisualiser) {
        super(algorithmVisualiser);
        // ASCII Title
        String[] titleArray = new AsciiFileReader("algorithmvisualiser/menu_title.txt").toArray();
        mainTitle = new StringArrayGraphics(getCanvas(),
                new DiscreteCoordinates((getCanvas().getWidth() / 2) - (titleArray[0].length() / 2), 0), titleArray,
                Color.BLUE);
        // Menu Titles
        for (int i = 0; i < menuTitles.length; ++i) {
            menuTitles[i] = new TextGraphics(getCanvas(), new DiscreteCoordinates(24 + (i * 40), 18),
                    MENU_TITLES_TEXT[i]);
        }
        // SORTING ALGORITHMS
        // ASCII art graph
        sortingThumbnail = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(28, 10),
                new AsciiFileReader("algorithmvisualiser/menu_sorting_thumbnail.txt").toArray());
        // menu options
        for (int i = 0; i < menuOptionsSorting.length; ++i) {
            menuOptionsSorting[i] = new TextGraphics(getCanvas(), new DiscreteCoordinates(25, 20 + i),
                    MENU_OPTIONS_SORTING_TEXT[i]);
        }
        // MAZE GENERATION
        // ASCII art maze
        mazeThumbnail = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(68, 10),
                new AsciiFileReader("algorithmvisualiser/menu_maze_thumbnail.txt").toArray());
        // menu options
        for (int i = 0; i < menuOptionsMaze.length; ++i) {
            menuOptionsMaze[i] = new TextGraphics(getCanvas(), new DiscreteCoordinates(65, 20 + i),
                    MENU_OPTIONS_MAZE_TEXT[i]);
        }

        divider = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(3, 38),
                new AsciiFileReader("algorithmvisualiser/menu_divider.txt").toArray());

        helpArrows = new TextGraphics(getCanvas(), new DiscreteCoordinates(3, 40),
                "(↑ ↓ ← →)  Use arrow keys to navigate");
        helpEnter = new TextGraphics(getCanvas(), new DiscreteCoordinates(54, 40),
                "(ENTER) to select and play/pause animations");
        helpReset = new TextGraphics(getCanvas(), new DiscreteCoordinates(3, 42), "(SPACE) to reset animations");
        helpEsc = new TextGraphics(getCanvas(), new DiscreteCoordinates(54, 42),
                "(ESC) to go back to menu/quit application");
    }

    @Override
    public boolean keyDown(Keyboard key) {
        switch (key) {
            case ESC:
                // TODO: find better way of closing app
                Terminal.bip(Audio.MENU_CLICK);
                super.getAlgorithmVisualiser().end();
                break;
            case DOWN:
                int max = 0;
                if (currentColumn == 0) {
                    max = menuOptionsSorting.length - 1;
                } else if (currentColumn == 1) {
                    max = menuOptionsMaze.length - 1;
                }
                currentSelection = increment(currentSelection, max);
                break;
            case UP:
                currentSelection = decrement(currentSelection);
                break;
            case LEFT:
                currentSelection = 0;
                currentColumn = decrement(currentColumn);
                break;
            case RIGHT:
                currentSelection = 0;
                currentColumn = increment(currentColumn, menuTitles.length - 2);
                break;
            case ENTER:
                // TODO: will have to use Deque for better menu organisation...
                Terminal.bip(Audio.MENU_CLICK);
                if (currentColumn == 0) {
                    if (currentSelection == 0) {
                        getAlgorithmVisualiser()
                                .pushState(new SortingState(getAlgorithmVisualiser(), SortingAlgorithm.BUBBLE_SORT));

                    } else if (currentSelection == 1) {
                        getAlgorithmVisualiser()
                                .pushState(new SortingState(getAlgorithmVisualiser(), SortingAlgorithm.QUICK_SORT));

                    }
                } else if (currentColumn == 1) {
                    getAlgorithmVisualiser().pushState(new MazeState(getAlgorithmVisualiser()));
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void update() {

        // Update Graphics
        mainTitle.update();
        for (TextGraphics menuTitle : menuTitles) {
            menuTitle.update();
        }

        updateMenuOptions(menuOptionsSorting, 0);
        updateMenuOptions(menuOptionsMaze, 1);

        sortingThumbnail.update();
        mazeThumbnail.update();
        divider.update();
        helpArrows.update();
        helpEnter.update();
        helpReset.update();
        helpEsc.update();
    }

    private int increment(int toIncrement, int max) {
        if (toIncrement < max) {
            Terminal.bip(Audio.MENU_CLICK);
            return ++toIncrement;
        }
        return toIncrement;
    }

    private int decrement(int toDecrement) {
        if (toDecrement > 0) {
            Terminal.bip(Audio.MENU_CLICK);
            return --toDecrement;
        }
        return toDecrement;
    }

    private void updateMenuOptions(TextGraphics[] options, int columnPosition) {
        for (int i = 0; i < options.length; ++i) {
            if (i == currentSelection && currentColumn == columnPosition) {
                options[i].setColor(Color.BOLD);
            } else {
                options[i].setColor(null);
            }
            options[i].update();
        }
    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
    }
}
