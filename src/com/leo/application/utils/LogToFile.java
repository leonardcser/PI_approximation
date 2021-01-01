/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        20:42
 */

package com.leo.application.utils;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogToFile {
    private static final Logger LOGGER = Logger.getLogger("MYLOG");
    private static final String FILENAME = "logs.log";

    private LogToFile() {
        throw new IllegalStateException("Utility class");
    }

    private static void clearLogs() {
        try {
            PrintWriter writer = new PrintWriter(FILENAME);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void log(String log) {
        FileHandler fh = null;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler(FILENAME);
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            LOGGER.setUseParentHandlers(false);
            LOGGER.info(log);

        } catch (SecurityException | IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        } finally {
            if (fh != null) {
                fh.close();
            }
        }

    }
}
