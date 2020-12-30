/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        16:00
 */


package com.leo.application.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Terminal {

    private Terminal() {
        throw new IllegalStateException("Terminal class");
    }

    public static void resetCursorPos() {
        System.out.print("\u001b[1000F\u001b[1E");
    }

    public static void executeCmd(String command) {
        ProcessBuilder p = new ProcessBuilder("/bin/bash", "-c", command);
        Process p2 = null;
        try {
            p2 = p.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not execute command: " + command);
            Thread.currentThread().interrupt();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        String line = null;

        while (true) {
            try {
                if ((line = br.readLine()) == null) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(line);
        }
    }

}
