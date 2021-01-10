package com.leo.application;

import com.leo.application.visualiserapp.AlgorithmVisualiser;

public class Main {
    public static void main(String[] args) {
        new Loop(new AlgorithmVisualiser(159, 45)).start();
    }
}