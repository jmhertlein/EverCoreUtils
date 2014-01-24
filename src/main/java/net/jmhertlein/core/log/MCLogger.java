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
package net.jmhertlein.core.log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A logger with easy functions to log severe, warning, and info events.
 *
 * @author Joshua
 * @deprecated Functionality is duplicated in JavaPlugin now
 */
public class MCLogger {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static String PREFIX;
    private static boolean DEBUGGING;

    public MCLogger() {
        PREFIX = "";
        DEBUGGING = false;
    }

    public MCLogger(String prefix) {
        PREFIX = prefix;
        DEBUGGING = false;
    }

    public void setDebugging(boolean debugging) {
        DEBUGGING = debugging;
    }

    public void logAssert(boolean bool, String desc) {
        if (!bool)
            logDebug("WARNING: ASSERTION FAILED: " + desc);
    }

    public void logSevere(String msg) {
        log.log(Level.SEVERE, PREFIX + msg);
    }

    public void logInfo(String msg) {
        log.log(Level.INFO, PREFIX + msg);
    }

    public void logDebug(String msg) {
        if (DEBUGGING)
            logInfo("[DEBUG]:" + msg);
    }
}
