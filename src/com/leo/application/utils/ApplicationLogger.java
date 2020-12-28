/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        20:42
 */


package com.leo.application.utils;

import java.io.*;

public class ApplicationLogger {
    private static final String filename = "logs.txt";

    public ApplicationLogger() {
        try {
            File myObj = new File(filename);
            if (!myObj.createNewFile()) {
                clearLogs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearLogs() {
        try {
            PrintWriter writer = new PrintWriter(filename);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void write(String log) {
        File originalFile = new File(filename);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(originalFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Construct the new file that will later be renamed to the original
        // filename.
        File tmpFile = new File("tmp" + filename);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(tmpFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = null;
        // Read from the original file and write to the new
        // unless content matches data to be removed.
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            pw.println(line);
            pw.flush();
        }
        pw.println(log);
        pw.close();
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Delete the original file
        if (!originalFile.delete()) {
            System.out.println("Could not delete file");
            return;
        }

        // Rename the new file to the filename the original file had.
        if (!tmpFile.renameTo(originalFile))
            System.out.println("Could not rename file");

    }
}
