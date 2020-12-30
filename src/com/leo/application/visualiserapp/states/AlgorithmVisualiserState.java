/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        16:01
 */


package com.leo.application.visualiserapp.states;

import com.leo.application.states.State;
import com.leo.application.visualiserapp.AlgorithmVisualiser;
import com.leo.application.window.Canvas;
import com.leo.application.window.Window;

public abstract class AlgorithmVisualiserState extends State {

    private AlgorithmVisualiser algorithmVisualiser;

    protected AlgorithmVisualiserState(AlgorithmVisualiser algorithmVisualiser) {
        super(algorithmVisualiser);
        this.algorithmVisualiser = algorithmVisualiser;
    }

    public AlgorithmVisualiser getAlgorithmVisualiser() {
        return algorithmVisualiser;
    }

    public Window getWindow() {
        return algorithmVisualiser.getWindow();
    }

    public Canvas getCanvas() {
        return algorithmVisualiser.getCanvas();
    }
}
