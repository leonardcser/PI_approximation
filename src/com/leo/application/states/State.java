/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:18
 */

package com.leo.application.states;

import com.leo.application.Application;
import com.leo.application.Listener;
import com.leo.application.Terminatable;
import com.leo.application.Updatable;
import com.leo.application.window.Canvas;
import com.leo.application.window.Window;

public abstract class State implements Updatable, Listener, Terminatable {

    private Application application;

    protected State(Application application) {
        this.application = application;
    }

    public Window getWindow() {
        return application.getWindow();
    }

    public Canvas getCanvas() {
        return application.getCanvas();
    }

}
