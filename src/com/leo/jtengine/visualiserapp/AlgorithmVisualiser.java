/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:07
 */

package com.leo.jtengine.visualiserapp;

import com.leo.jtengine.Application;
import com.leo.jtengine.visualiserapp.states.MenuState;
import com.leo.jtengine.window.Window;

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