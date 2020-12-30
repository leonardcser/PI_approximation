/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:53
 */


package com.leo.application.visualiserapp.states;

import com.leo.application.graphics.StringArrayGraphics;
import com.leo.application.graphics.TextGraphics;
import com.leo.application.maths.DiscreteCoordinates;
import com.leo.application.utils.Colors;
import com.leo.application.utils.Terminal;
import com.leo.application.visualiserapp.AlgorithmVisualiser;
import com.leo.application.visualiserapp.algorithms.sorting.SortingAlgorithm;
import com.leo.application.window.Keyboard;

public class MenuState extends AlgorithmVisualiserState {
    private static final String[]
            MAIN_TITLE_STRING = new String[]{"    ___    __                 _ __  __                  ",
                                             "   /   |  / /___ _____  _____(_) /_/ /_  ____ ___  _____",
                                             "  / /| | / / __ `/ __ \\/ ___/ / __/ __ \\/ __ `__ \\/ ___/",
                                             " / ___ |/ / /_/ / /_/ / /  / / /_/ / / / / / / / (__  ) ",
                                             "/_/  |_/_/\\__, /\\____/_/  /_/\\__/_/ /_/_/ /_/ /_/____/  ",
                                             "         /____/                                         "};
    private static final String[] MENU_TITLES_TEXT =
            new String[]{"[1] Sorting Algorithms", "[2] Maze generation", "[3] Pathfinding Algorithms"};
    private static final String[] MENU_OPTIONS_SORTING_TEXT = new String[]{"- Bubble Sort", "- Quick Sort"};
    private static final String[] MENU_OPTIONS_MAZE_TEXT = new String[]{"- Recursive Back Tracking"};

    private static final String[] SORTING_THUMBNAIL_STRING = new String[]{"█",
                                                                          "█       █",
                                                                          "█    █  █",
                                                                          "█ █  █  █ █    █",
                                                                          "███ ██ ██ █  █ █",
                                                                          "███ ███████ ██ █",
                                                                          "████████████████"};

    private static final String[] MAZE_THUMBNAIL_STRING = new String[]{"████████████████",
                                                                       "     █       ███",
                                                                       "███  █  ███  ███",
                                                                       "███     ███    █",
                                                                       "██████  █████  █",
                                                                       "██      █      █",
                                                                       "█████████  █████"};
    private static final String[] DIVIDER_STRING = new String[]{
            "── Controls ─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────"};

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
        mainTitle = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(
                (getCanvas().getWidth() / 2) - (MAIN_TITLE_STRING[0].length() / 2), 0), MAIN_TITLE_STRING, Colors.BLUE);
        // Menu Titles
        for (int i = 0; i < menuTitles.length; ++i) {
            menuTitles[i] =
                    new TextGraphics(getCanvas(), new DiscreteCoordinates(24 + (i * 40), 17), MENU_TITLES_TEXT[i]);
        }
        // SORTING ALGORITHMS
        // ASCII art graph
        sortingThumbnail = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(28, 9),
                                                   SORTING_THUMBNAIL_STRING);
        // menu options
        for (int i = 0; i < menuOptionsSorting.length; ++i) {
            menuOptionsSorting[i] =
                    new TextGraphics(getCanvas(), new DiscreteCoordinates(25, 19 + i), MENU_OPTIONS_SORTING_TEXT[i]);
        }
        // MAZE GENERATION
        // ASCII art maze
        mazeThumbnail = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(68, 9), MAZE_THUMBNAIL_STRING);
        // menu options
        for (int i = 0; i < menuOptionsMaze.length; ++i) {
            menuOptionsMaze[i] =
                    new TextGraphics(getCanvas(), new DiscreteCoordinates(65, 19 + i), MENU_OPTIONS_MAZE_TEXT[i]);
        }


        divider = new StringArrayGraphics(getCanvas(), new DiscreteCoordinates(3, 38), DIVIDER_STRING);
        helpArrows =
                new TextGraphics(getCanvas(), new DiscreteCoordinates(3, 40), "(↑ ↓ ← →)  Use arrow keys to navigate");
        helpEnter = new TextGraphics(getCanvas(), new DiscreteCoordinates(54, 40),
                                     "(ENTER) to select and play/pause animations");
        helpReset = new TextGraphics(getCanvas(), new DiscreteCoordinates(3, 42), "(SPACE) to reset animations");
        helpEsc = new TextGraphics(getCanvas(), new DiscreteCoordinates(54, 42),
                                   "(ESC) to go back to menu/quit application");
    }

    @Override
    public void render() {

    }

    @Override
    public void update() {

        // Check for user input
        if (getWindow().getKeyListener().keyIsDown(Keyboard.DOWN)) {
            int max = 0;
            if (currentColumn == 0) {
                max = menuOptionsSorting.length - 1;
            } else if (currentColumn == 1) {
                max = menuOptionsMaze.length - 1;
            }
            currentSelection = increment(currentSelection, max);

        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.UP)) {
            currentSelection = decrement(currentSelection);

        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.LEFT)) {
            currentSelection = 0;
            currentColumn = decrement(currentColumn);

        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.RIGHT)) {
            currentSelection = 0;
            currentColumn = increment(currentColumn, menuTitles.length - 2);

        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.ENTER)) {
            if (currentColumn == 0) {
                if (currentSelection == 0) {
                    getAlgorithmVisualiser().getStates()
                                            .push(new SortingState(getAlgorithmVisualiser(),
                                                                   SortingAlgorithm.BUBBLE_SORT));

                } else if (currentSelection == 1) {
                    getAlgorithmVisualiser().getStates()
                                            .push(new SortingState(getAlgorithmVisualiser(),
                                                                   SortingAlgorithm.QUICK_SORT));

                }
            } else if (currentColumn == 1) {
                getAlgorithmVisualiser().getStates().push(new MazeState(getAlgorithmVisualiser()));
            }
        } else if (getWindow().getKeyListener().keyIsDown(Keyboard.ESC)) {
            Terminal.resetCursorPos();
            System.exit(0);
        }

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
        return toIncrement < max ? toIncrement + 1 : toIncrement;
    }

    private int decrement(int toDecrement) {
        return toDecrement > 0 ? toDecrement - 1 : toDecrement;
    }

    private void updateMenuOptions(TextGraphics[] options, int columnPosition) {
        for (int i = 0; i < options.length; ++i) {
            if (i == currentSelection && currentColumn == columnPosition) {
                options[i].setColor(Colors.BOLD);
            } else {
                options[i].setColor(null);
            }
            options[i].update();
        }
    }
}
