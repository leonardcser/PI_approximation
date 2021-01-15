/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:18
 */

package com.leo.jtengine.states;

import com.leo.jtengine.Application;
import com.leo.jtengine.Listener;
import com.leo.jtengine.Terminatable;
import com.leo.jtengine.Updatable;
import com.leo.jtengine.utils.Terminal;
import com.leo.jtengine.window.Canvas;
import com.leo.jtengine.window.Window;

public abstract class State implements Updatable, Listener, Terminatable {

    private final Application application;

    protected State(Application application) {
        this.application = application;
    }

    public Terminal getTerminal() {
        return application.getTerminal();
    }

    public Window getWindow() {
        return application.getWindow();
    }

    public Canvas getCanvas() {
        return application.getCanvas();
    }


}
