/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        16:00
 */

package com.leo.application.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Terminal {
    private static Logger logger;
    private static final String FILENAME = "logs.log";
    private static FileHandler fh = null;

    private static BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

    private Terminal() {
        throw new IllegalStateException("Utility class");
    }

    public static void clearLogs() {
        if (fh != null) {
            fh.close();
        }
        logger = null;
        initLogger();
    }

    public static void log(String log) {
        initLogger();
        logger.info(log);
    }

    public static void logErr(Exception e, String message) {
        initLogger();
        logger.log(Level.SEVERE, message, e);
    }

    public static void logErr(Exception e) {
        logErr(e, "");
    }
    private static void initLogger() {
        if (logger == null) {
            logger = Logger.getLogger("MYLOG");
            try {
                fh = new FileHandler(FILENAME);
                // This block configure the logger with handler and formatter
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                logger.setUseParentHandlers(false);
            } catch (SecurityException | IOException e) {
                logErr(e);
            }
        }

    }

    public static void write(String str) {
        try {
            bufferedWriter.write(str);
        } catch (IOException e) {
            logErr(e);
        }
    }

    public static void flush() {
        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            logErr(e);
        }
    }

    public static void resetCursorPos() {
        try {
            bufferedWriter.write("\u001b[1000F\u001b[1E");
        } catch (IOException e) {
            logErr(e);
        }
    }

    public static void executeCmd(String command) {
        ProcessBuilder p = new ProcessBuilder("/bin/bash", "-c", command);
        Process p2 = null;
        try {
            p2 = p.start();
        } catch (IOException e) {
            logErr(e, "Could not execute command: " + command);
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
                logErr(e);
            }
            System.out.println(line);
        }
    }

}
