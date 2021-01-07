package com.leo.application.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.leo.application.utils.Terminal;

public class AsciiFileReader {
    private final String filename;
    private String[] fileContent;
    private static final String[] FILE_NOT_FOUND = new String[]{"Ascii not found :("};

    public AsciiFileReader(String filename) {
        this.filename = filename;
        try {
            readFile();
        } catch (IOException e) {
            Terminal.logErr(e);
            fileContent = FILE_NOT_FOUND;
        }
    }

    private void readFile() throws IOException {
        List<String> content = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("./bin/art/" + filename))) {
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