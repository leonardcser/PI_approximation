/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        16:00
 */

package com.leo.application.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Terminal {
    private static Logger logger;
    private static final String FILENAME = "logs.log";
    private static FileHandler fh = null;

    private static final PrintWriter printWriter = new PrintWriter(
            new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), 512));

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

    public static void write(String str) {
        printWriter.write(str);
    }

    public static void flush() {
        printWriter.flush();
    }

    public static void resetCursorPos() {
        write("\033[1000F\033[1E");
        flush();
    }

    public static void bip(Audio audio) {
        new Thread(() -> executeCmd("afplay bin/sounds/" + audio.filename)).start();
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
            write(line);
            flush();
        }
    }

    public static void writePID() {
        // Use the engine management bean in java to find out the pid
        // and to write to a file
        
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        if (pid.indexOf("@") != -1) {
            pid = pid.substring(0, pid.indexOf("@"));
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("app.pid"))) {
            writer.write(pid);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            logErr(e);
        }
    }

}

class LoggerPrintStream extends PrintStream {

    public LoggerPrintStream(OutputStream out) {
        super(out, true);
    }

    @Override
    public void print(String s) {
        Terminal.logErr(s);
        super.print("");
    }
}