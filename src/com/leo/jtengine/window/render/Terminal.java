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
        enableRawInput();
        saveState();
        write(Xterm.HIDE_CURSOR);
        write(Xterm.ENABLE_MOUSE_EVENTS);
        flush();
        switchProfile("HighPerformance");
        clear();
    }

    public void close() {
        switchProfile("Default");
        resetCursorPos();
        write(Xterm.DISABLE_MOUSE_EVENTS);
        write(Xterm.SHOW_CURSOR);
        flush();
        disableRawInput();
        restoreState();
        printWriter.close();
    }

    

    public void clear() {
        write(Xterm.CLEAR_SCREEN);
    }


    private void switchProfile(String profile) {
        write(Xterm.setProfile(profile));
    }

    private void enableRawInput() {
        executeCmd("stty raw -echo </dev/tty");
    }

    private void disableRawInput() {
        executeCmd("stty -raw echo </dev/tty");
    }

    private void saveState() {
        write(Xterm.ENABLE_SCREEN_BUFFER);
        flush();
        int[] size = getSize();
        if (window.getWidth() == 0 && window.getHeight() == 0) {
            window.setWidth(size[W]);
            window.setHeight(size[H]);
        }
        window.setInitWidth(size[W]);
        window.setInitHeight(size[H]);
        
    }

    private void restoreState() {
        if (window.getInitWidth() > 0 && window.getInitHeight() > 0) {
            setSize(window.getInitWidth(), window.getInitHeight());
        }
        write(Xterm.DISABLE_SCREEN_BUFFER);
        flush();
    }

    private void setFullScreen() {
        setSize(2000, 2000);
        moveToTopLeft();
        sleep(10);
        int[] newSize = getSize();
        window.setWidth(newSize[W]);
        window.setHeight(newSize[H]);
    }

    private void setSize(int width, int height) {
        write(Xterm.resizeWindow(width, height));
        flush();
    }

    private void moveToTopLeft() {
        write(Xterm.moveWindow(0, 0));
        flush();
    }

    public void setTitle(String title) {
        executeCmd("printf '" + Xterm.setTitle(title) + "'");
	}

    private int[] getSize() {
        // save cursor position
        // move to col 5000 row 5000
        // request cursor position
        // restore cursor position
        resetCursorPos();
        write(Xterm.SAVE_CURSOR_POS + Xterm.moveCursor(2000, 2000) + Xterm.REQUEST_CURSOR_POS + Xterm.RESTORE_CURSOR_POS);
        flush();
        String inputClean = System.console().readLine().replaceAll("[^0-9;]", "");
        String[] strSize = inputClean.split(";");
        int[] size = new int[2];
        try {
            for (int i = 0; i < size.length; i++) {
                size[i] = Integer.parseInt(strSize[i]);
            }
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
        write(Xterm.moveUpAtStart(2000));
        flush();
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            TerminalLogger.logErr(e);
            Thread.currentThread().interrupt();
        }
    }
	

}