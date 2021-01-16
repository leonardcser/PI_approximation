/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        16:00
 */

package com.leo.jtengine.window.render;

import com.leo.jtengine.Graphics;
import com.leo.jtengine.window.Window;
import com.leo.jtengine.utils.Audio;
import com.leo.jtengine.utils.TerminalLogger;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;

public class Terminal implements Graphics {
    private static final int W = 1;
    private static final int H = 0;
    private final PrintWriter printWriter = new PrintWriter(
            new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), 512));
    private final Window window;

    public Terminal(Window window) {
        this.window = window;
        init();
        if (window.isFullScreen()) {
            setFullScreen();
        } else if (window.getWidth() != 0 && window.getHeight() != 0) {
            setSize(window.getWidth(), window.getHeight());
        }
    }

    public Window getWindow() {
        return window;
    }

    private void init() {
        switchProfile("HighPerformance");
        enableRawInput();
        saveState();
        hideCursor();
        clear();
    }

    public void close() {
        switchProfile("Default");
        resetCursorPos();
        showCursor();
        disableRawInput();
        restoreState();
        printWriter.close();
    }

    public void clear() {
        write(XtermUtils.CLEAR_SCREEN);
    }

    private void hideCursor() {
        write(XtermUtils.HIDE_CURSOR);
        flush();
    }

    private void showCursor() {
        write(XtermUtils.SHOW_CURSOR);
        flush();
    }

    private void switchProfile(String profile) {
        write(XtermUtils.setProfile(profile));
    }

    private void enableRawInput() {
        executeCmd("stty raw -echo </dev/tty");
    }

    private void disableRawInput() {
        executeCmd("stty -raw echo </dev/tty");
    }

    private void saveState() {
        int[] size = getSize();
        if (window.getWidth() == 0 && window.getHeight() == 0) {
            window.setWidth(size[W]);
            window.setHeight(size[H]);
        }
        window.setInitWidth(size[W]);
        window.setInitHeight(size[H]);
        executeCmd("tput smcup");
    }

    private void restoreState() {
        executeCmd("tput rmcup");
        if (window.getInitWidth() > 0 && window.getInitHeight() > 0) {
            setSize(window.getInitWidth(), window.getInitHeight());
        }
    }

    private void setFullScreen() {
        setSize(2000, 2000);
        moveToTopLeft();
        sleep(10);
        int[] newSize = getSize();
        window.setWidth(newSize[W]);
        window.setHeight(newSize[H]);
    }

    public static void sleep(int millis) {
        sleep(millis, Thread.currentThread());
    }

    public static void sleep(int millis, Thread thread) {
        try {
            thread.sleep(millis);
        } catch (InterruptedException e) {
            TerminalLogger.logErr(e);
            thread.interrupt();
        }
    }

    private void setSize(int width, int height) {
        write(XtermUtils.resizeWindow(width, height + 1));
        flush();
    }

    private void moveToTopLeft() {
        write(XtermUtils.moveWindow(0, 0));
        flush();
    }

    public void setTitle(String title) {
        executeCmd("printf '" + XtermUtils.setTitle(title) + "'");
	}

    private int[] getSize() {
        // save cursor position
        // move to col 5000 row 5000
        // request cursor position
        // restore cursor position
        write(XtermUtils.SAVE_CURSOR_POS + XtermUtils.moveCursor(2000, 2000) + XtermUtils.REQUEST_CURSOR_POS + XtermUtils.RESTORE_CURSOR_POS);
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
            return new int[]{window.getHeight(), window.getWidth()};
        }
    }

    public void bip(Audio audio) {
        new Thread(() -> executeCmd("afplay sounds/" + audio.filename)).start();
    }

    public void executeCmd(String command) {
        ProcessBuilder p = new ProcessBuilder("/bin/bash", "-c", command);
        Process p2 = null;
        try {
            p2 = p.start();
        } catch (IOException e) {
            TerminalLogger.logErr(e, "Could not execute command: " + command);
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
                TerminalLogger.logErr(e);
            }
            write(line);
            flush();
        }
    }

    public void write(String str) {
        printWriter.write(str);
    }

    public void flush() {
        printWriter.flush();
    }

    protected void writePID() {
        // Use the engine management bean in java to find out the pid
        // and to write to a file
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        if (pid.contains("@")) {
            pid = pid.substring(0, pid.indexOf("@"));
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("app.pid"))) {
            writer.write(pid);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            TerminalLogger.logErr(e);
        }
    }

    @Override
    public void render() {
        resetCursorPos();
    }

    private void resetCursorPos() {
        write(XtermUtils.moveUpAtStart(2000) + XtermUtils.moveDownAtStart(1));
        flush();
    }

	

}