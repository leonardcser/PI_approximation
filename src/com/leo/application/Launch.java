/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        14:48
 */


package com.leo.application;

import com.leo.application.algorithms.sorting.SortingApplication;

public class Launch {

    public static void main(String[] args) {
        new Loop(new SortingApplication(120, 35)).start();
    }
}
