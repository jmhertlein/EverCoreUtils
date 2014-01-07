/*
 * Copyright (C) 2013 Joshua M Hertlein <jmhertlein@gmail.com>
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

package net.jmhertlein.core.persist.metrics;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author joshua
 */
public class ReportMetricsTask implements Runnable {
    private final String hostName;
    private final int port;
    private final Plugin p;

    public ReportMetricsTask(String hostName, int port, Plugin p) {
        this.hostName = hostName;
        this.port = port;
        this.p = p;
    }
    
    @Override
    public void run() {
        Socket s;
        try {
            s = new Socket(hostName, port);
        } catch (IOException ex) {
            Logger.getLogger(ReportMetricsTask.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error opening connection to metrics server.");
            return;
        }
        String reportString;
        try (Scanner scan = new Scanner(s.getInputStream())) {
            reportString = scan.nextLine();
        } catch (IOException ex) {
            Logger.getLogger(ReportMetricsTask.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error receiving metrics report.");
            return;
        }
    }
    
}
