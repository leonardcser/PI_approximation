/*
 *	Author:      Leonard Cseres
 *	Date:        28.12.20
 *	Time:        11:35
 */


package com.leo.application.visualiserapp.algorithms.sorting;

import com.leo.application.utils.Color;

public class QuickSort extends Sort {

    public QuickSort(Integer[] arr) {
        super(arr);
    }

    protected void sort(Integer[] arr, int start, int end) {
        // partition
        if (start < end) {
            int index = partition(arr, start, end);
            clearStates();
            sort(arr, start, index - 1);
            modifyStatesAt(index, null);

            sort(arr, index + 1, end);

        }
    }

    @Override
    public void run() {
        super.run();
        sort(getArr(), getStart(), getEnd());
        reset();
    }

    private int partition(Integer[] arr, int start, int end) {
        for (int i = start; i <= end; ++i) {
            modifyStatesAt(i, Color.DARK_CYAN);
        }
        int pivotIndex = start;
        int pivotValue = arr[end];
        modifyStatesAt(pivotIndex, Color.RED);
        for (int i = start; i < end; ++i) {
            if (arr[i] < pivotValue) {
                swap(arr, i, pivotIndex);
                modifyStatesAt(pivotIndex, null);
                ++pivotIndex;
                modifyStatesAt(pivotIndex, Color.RED);
            }
        }
        swap(arr, pivotIndex, end);

        for (int i = start; i < end; ++i) {
            if (i != pivotIndex) {
                getStates()[pivotIndex] = null;
            }
        }
        return pivotIndex;
    }

    @Override
    public String toString() {
        return "Quick Sort";
    }
}
