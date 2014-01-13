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
package net.jmhertlein.core.persist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A set of String <-> String mappings that saves to a flat file. Don't use spaces in the strings.
 * 
 * @author Joshua Michael Hertlein <jmhertlein@gmail.com>
 * @deprecated use the default Properties class instead, it's better
 */
public class FlatConfiguration {
    private final Map<String, String> config;
    
    public FlatConfiguration() {
        config = new HashMap<>();
    }
    
    public FlatConfiguration(File f) throws FileNotFoundException {
        config = new HashMap<>();
        load(f);
    }
    
    public String getValue(String key) {
        return config.get(key);
    }
    
    public void setValue(String key, String value) {
        config.put(key, value);
    }
    
    public Map<String, String> getMap() {
        return config;
    }
    
    public void save(File f) throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(f)) {
            for(Map.Entry<String, String> entry : config.entrySet()) {
                pw.printf("%s %s\n", entry.getKey(), entry.getValue());
            }
        }
    }
    
    public final void load(File f) throws FileNotFoundException {
        try (Scanner fileScan = new Scanner(f)) {
            while(fileScan.hasNextLine()) {
                String[] split = fileScan.nextLine().split(" ", 2);
                config.put(split[0], split[1]);
            }
        }
    }
}
