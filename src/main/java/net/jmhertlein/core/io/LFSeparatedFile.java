/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jmhertlein.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A line feed-separated file holding arbitrary Strings.
 *
 * @author joshua
 */
public class LFSeparatedFile {
    private List<String> lines;

    public LFSeparatedFile(List<String> lines) {
        this.lines = lines;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public void writeToFile(File f) throws IOException {
        writeLinesToFile(lines, f);
    }

    public static LFSeparatedFile readFromFile(File f) throws FileNotFoundException {
        return new LFSeparatedFile(getLinesFromFile(f));
    }

    public static List<String> getLinesFromFile(File f) throws FileNotFoundException {
        List<String> lines;
        try (Scanner scan = new Scanner(f)) {
            lines = new ArrayList<>();
            while (scan.hasNextLine())
                lines.add(scan.nextLine());
        }
        return lines;
    }

    public static void writeLinesToFile(List<String> lines, File f) throws IOException {
        if (!f.exists())
            f.createNewFile();

        try (PrintWriter pw = new PrintWriter(f)) {
            for (String l : lines)
                pw.println(l);
        }
    }
}
