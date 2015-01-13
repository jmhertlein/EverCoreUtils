/*
 * Copyright (C) 2015 Joshua Michael Hertlein <jmhertlein@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
