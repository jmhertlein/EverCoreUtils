/*
 * Copyright (C) 2013 Joshua Michael Hertlein <jmhertlein@gmail.com>
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
package net.jmhertlein.core.chat;

import org.bukkit.ChatColor;

/**
 *
 * @author joshua
 */
public class ChatUtil {

    public static final ChatColor FATAL = ChatColor.DARK_RED,
            ERR = ChatColor.RED,
            WARN = ChatColor.YELLOW,
            SUCC = ChatColor.GREEN,
            INFO = ChatColor.LIGHT_PURPLE,
            INFO_ALT = ChatColor.DARK_BLUE;

    /**
     * Formats the given 2d array of strings with respect to columns.
     *
     * @param strings A 2d array of strings where strings[i] is a line and
     * strings[i][j] is a word on a line
     * @param padding How many spaces to enter between each column
     * @return an array of strings, where each string in the array is a line.
     */
    public static String[] formatStringsForColumns(String[][] strings, boolean header) {
        String[] formattedLines = new String[strings.length];
        for (int i = 0; i < formattedLines.length; i++) {
            formattedLines[i] = "";
        }

        int longestInCol;
        for (int col = 0; col < strings[0].length; col++) {
            longestInCol = getLongestStringInColumn(strings, col);
            for (int line = 0; line < strings.length; line++) {
                formattedLines[line] += strings[line][col];
                for (int i = strings[line][col].length(); i < longestInCol; i++) {
                    formattedLines[line] += " ";
                }
                formattedLines[line] += "|";
            }
        }

        for (int i = 0; i < formattedLines.length; i++) {
            formattedLines[i] = formattedLines[i].substring(0, formattedLines[i].length() - 1);
        }

        if (header) {
            String[] formattedLinesWithHeaderLine = new String[formattedLines.length + 1];
            formattedLinesWithHeaderLine[0] = formattedLines[0];
            formattedLinesWithHeaderLine[1] = "";
            for (int i = 0; i < formattedLines[0].length(); i++) {
                formattedLinesWithHeaderLine[1] += "=";
            }

            System.arraycopy(formattedLines, 1, formattedLinesWithHeaderLine, 2, formattedLines.length - 1);

            return formattedLinesWithHeaderLine;
        }

        return formattedLines;

    }

    /**
     * Gets the longest string in the specified column.
     *
     * @param strings A 2d array of strings where strings[i] is a line and
     * strings[i][j] is a word on a line
     * @param column the index of the column to check
     * @see ChatUtil#formatStringsForColumns(java.lang.String[][], int)
     * @return the length of the longest string in the column, 0 if there were
     * only empty strings, or -1 if the array was empty
     */
    private static int getLongestStringInColumn(String[][] strings, int column) {
        int longest = strings[0][column].length();

        for (int i = 0; i < strings.length; i++) {
            if (strings[i][column].length() > longest) {
                longest = strings[i][column].length();
            }
        }

        return longest;

    }
}
