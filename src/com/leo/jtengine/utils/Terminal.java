/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        16:00
 */

package com.leo.jtengine.utils;

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
    private static final int W = 1;
    private static final int H = 0;
    private static Logger logger;
    private static final String FILENAME = "logs.log";
    private static FileHandler fh = null;
    private static int initWidth = 0;
    private static int initHeight = 0;
    private static int width = 0;
    private static int height = 0;

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

    public static void init() {
        switchProfile("HighPerformance");
        enableRawInput();
        saveState();
        hideCursor();
        clear();
    }

    public static void close() {
        switchProfile("Default");
        resetCursorPos();
        showCursor();
        disableRawInput();
        restoreState();
    }

    public static void resetCursorPos() {
        write("\033[1000F\033[1E");
        flush();
    }

    public static int[] getSize() {
        // save cursor position
        // move to col 5000 row 5000
        // request cursor position
        // restore cursor position
        write("\033[s\033[5000;5000H\033[6n\033[u");
        flush();
        String inputClean = System.console().readLine().replaceAll("[^0-9;]", "");
        String[] strSize = inputClean.split(";");
        int[] size = new int[2];
        try {
            for (int i = 0; i < size.length; i++) {
                size[i] = Integer.parseInt(strSize[i]);
            }
            size[H] -= 1;
            return size;
        } catch (NumberFormatException e) {
            return new int[] {height, width};
        }
    }

    private static void saveInitSize() {
        int[] initSize = getSize();
        initWidth = initSize[W];
        initHeight = initSize[H];
        width = initWidth;
        height = initHeight;
    }

    public static void setSize(int width, int height) {
        write("\033[8;" + (height + 1) + ";" + width + "t");
        flush();
    }

    public static void clear() {
        executeCmd("clear");
    }

    public static void moveToTopLeft() {
        write("\033[3;0;0t");
        flush();
    }

    private static void hideCursor() {
        write("\033[?25l");
        flush();
    }

    private static void showCursor() {
        write("\033[?25h");
        flush();
    }

    private static void switchProfile(String profile) {
        write("\033]50;SetProfile=" + profile + "\007");
    }

    private static void enableRawInput() {
        executeCmd("stty raw -echo </dev/tty");
    }

    private static void disableRawInput() {
        executeCmd("stty -raw echo </dev/tty");
    }

    private static void saveState() {
        saveInitSize();
        executeCmd("tput smcup");
    }

    private static void restoreState() {
        executeCmd("tput rmcup");
        if (initWidth > 0 && initHeight > 0) {
            setSize(initWidth, initHeight);
        }
    }

    public static void setFullScreen() {
        setSize(2000, 2000);
        moveToTopLeft();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            logErr(e);
            Thread.currentThread().interrupt();
        }
        int[] newSize = getSize();
        width = newSize[W];
        height = newSize[H];
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
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