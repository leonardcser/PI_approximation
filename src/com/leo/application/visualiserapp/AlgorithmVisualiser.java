/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:07
 */

package com.leo.application.visualiserapp;

import com.leo.application.Application;
import com.leo.application.visualiserapp.states.MenuState;
import com.leo.application.window.Window;

public class AlgorithmVisualiser extends Application {

    public AlgorithmVisualiser(Window window) {
        super(window);
        pushState(new MenuState(this));
    }

    @Override
    public void end() {
        // Empty on purpose, do nothing
        super.end();
    }

}