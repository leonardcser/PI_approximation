/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        15:18
 */


package com.leo.application.states;

import com.leo.application.Application;
import com.leo.application.Listener;
import com.leo.application.Updatable;

public abstract class State implements Updatable, Listener {

    protected Application application;

    protected State(Application application) {
        this.application = application;
    }
}
