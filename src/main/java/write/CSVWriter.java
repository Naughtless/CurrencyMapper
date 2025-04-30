package main.java.write;



import main.java.common.CSV;

import java.io.*;
import java.util.List;

public class CSVWriter<T extends CSV> {
    private void ensureDirectoryExists(String path) {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    public void writeHeader(String path, String header) throws IOException {
        ensureDirectoryExists(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, false))) {
            writer.write(header);
            writer.newLine();
        }
    }

    public void writeBody(String path, List<T> data, boolean append) throws IOException {
        ensureDirectoryExists(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, append))) {
            for (T item : data) {
                writer.write(item.buildCSVLine());
                writer.newLine();
            }
        }
    }
}