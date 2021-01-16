/**
 * Author:     Leonard Cseres
 * Date:       Fri Jan 08 2021
 * Time:       15:27:23
 */


package com.leo.jtengine.io;

import com.leo.jtengine.utils.TerminalLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsciiFileReader {
    private static final String[] FILE_NOT_FOUND = new String[]{"Ascii not found :("};
    private final String filename;
    private String[] fileContent;

    public AsciiFileReader(String filename) {
        this.filename = filename;
        try {
            readFile();
        } catch (IOException e) {
            TerminalLogger.logErr(e);
            fileContent = FILE_NOT_FOUND;
        }
    }

    private void readFile() throws IOException {
        List<String> content = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("./art/" + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.add(line);
            }
        }
        fileContent = content.toArray(new String[0]);
    }

    public String[] toArray() {
        return fileContent;
    }
}