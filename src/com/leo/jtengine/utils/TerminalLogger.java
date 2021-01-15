/*
 *	Author:      Leonard Cseres
 *	Date:        15.01.21
 *	Time:        22:44
 */


package com.leo.jtengine.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TerminalLogger {
    private static Logger logger;
    private static final String FILENAME = "logs.log";
    private static FileHandler fh = null;

    private TerminalLogger() {
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

    public static void logErr(String message) {
        initLogger();
        logger.log(Level.SEVERE, message);
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

    public static void redirectErr() {
        System.setErr(new LoggerPrintStream(System.err));
    }
}
