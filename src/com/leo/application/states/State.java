/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:18
 */


package com.leo.application.states;

import com.leo.application.Application;
import com.leo.application.Graphics;
import com.leo.application.Updatable;

public abstract class State implements Updatable, Graphics {

    protected Application application;

    protected State(Application application) {
        this.application = application;
    }
}
