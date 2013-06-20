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
package net.jmhertlein.core.reporting;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

/**
 * The BugReport class represents a collection of information a developer would
 * want when trying to fix an unhandled exception. It is serializable, and is
 * intended to be sent over a network to a remote report-handing server.
 *
 * @author joshua
 */
public class BugReport implements Serializable {

    private static final long serialVersionUID = 1L;
    //CB stuff
    private String message;
    private StackTraceElement[] stackTrace;
    private String ip;
    private String options;
    private String bukkitVersion;
    private String pluginVersion;
    //java stuff
    private String jreVendor;
    private String jreVersion;
    //OS stuff
    private String osName, osArch, osVersion;

    /**
     * Constructs a new bug report based on provided environment variables
     *
     * @param plugin the plugin whose command caused an unhandled exception
     * @param s the Bukkit server the exception ocurred on
     * @param e the Exception object itself
     * @param env any plugin-specific environment data necessary to help
     * diagnose the problem
     */
    public BugReport(Plugin plugin, Server s, Exception e, String env) {
        ip = s.getIp();
        stackTrace = e.getStackTrace();
        message = e.getMessage();
        this.options = env;
        bukkitVersion = s.getBukkitVersion();
        pluginVersion = plugin.getDescription().getVersion();

        Properties p = System.getProperties();

        jreVendor = p.getProperty("java.vendor");
        jreVersion = p.getProperty("java.version");

        osArch = p.getProperty("os.arch");
        osVersion = p.getProperty("os.version");
        osName = p.getProperty("os.name");

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("=====================Begin====================\n");
        sb.append("IP:");
        sb.append(ip);
        sb.append('\n');

        sb.append("CB Version:");
        sb.append(bukkitVersion);
        sb.append('\n');

        sb.append("MCTVersion:");
        sb.append(pluginVersion);
        sb.append('\n');

        sb.append("Config:");
        sb.append(options.toString());
        sb.append('\n');

        sb.append("ErrMessage:");
        sb.append(message);
        sb.append('\n');

        sb.append("JRE Vendor:");
        sb.append(jreVendor);
        sb.append('\n');

        sb.append("JRE Version:");
        sb.append(jreVersion);
        sb.append('\n');

        sb.append("OS Name:");
        sb.append(osName);
        sb.append('\n');

        sb.append("OS Version:");
        sb.append(osVersion);
        sb.append('\n');

        sb.append("OS Arch:");
        sb.append(osArch);
        sb.append('\n');

        sb.append('\n');
        sb.append("Call Stack:\n");
        for (StackTraceElement e : stackTrace) {
            sb.append(e.toString());
            sb.append('\n');
        }
        sb.append('\n');

        sb.append("=====================End======================\n");

        return sb.toString();

    }

    /**
     * It's not uncommon for the Bukkit server to be unable to reliably
     * determine what the machine's external IP address is. This permits the IP
     * to be updated by the remote server as the report is received.
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.message);
        hash = 61 * hash + Arrays.deepHashCode(this.stackTrace);
        hash = 61 * hash + Objects.hashCode(this.ip);
        hash = 61 * hash + Objects.hashCode(this.options);
        hash = 61 * hash + Objects.hashCode(this.bukkitVersion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BugReport other = (BugReport) obj;
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (!Arrays.deepEquals(this.stackTrace, other.stackTrace)) {
            return false;
        }
        if (!Objects.equals(this.ip, other.ip)) {
            return false;
        }
        if (!Objects.equals(this.options, other.options)) {
            return false;
        }
        if (!Objects.equals(this.bukkitVersion, other.bukkitVersion)) {
            return false;
        }
        return true;
    }
}
