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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * The BugReportingCommandExecutor is an abstract base class that can be
 * extended to report unhandled exceptions to a remote server.
 *
 * Here's how it works:
 *
 * 1. Make a child class of this class 2. Override executeCommand- treat it just
 * like onCommand in CommandExecutor or JavaPlugin (Which is-a ComamndExecutor)
 * 3. Override getPlugin, getEnvOptions, getHostname, and getPort 4. getPlugin,
 * Hostname, and Port should be fairly simple to implement. See their jdocs for
 * more specific info 5. getEnvOptions is your chance to get any blob of
 * information that you think you may need beyond the defaults that BugReport
 * already assembles for you. Just return the String containing whatever you
 * want.
 *
 * The commands are run only when an unhandled exception is thrown during the
 * execution of executeCommand.
 *
 * @author jmhertlein
 */
public abstract class BugReportingCommandExecutor implements CommandExecutor {

    /**
     *
     * @param cs
     * @param cmnd
     * @param string
     * @param strings
     * @return
     */
    @Override
    public final boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        try {
            return executeCommand(cs, cmnd, string, strings);
        } catch (Exception e) {
            reportBug(getPlugin(), e, getEnvOptions());

            //tell the player what happened!
            cs.sendMessage(ChatColor.RED + "An internal error occurred while running the command. A bug report has been automatically sent.");
            return true;
        }


    }

    private void reportBug(Plugin p, Exception e, String envOptions) {
        new Thread(new ReportBugTask(p, e, envOptions, getHostname(), getPort())).start();
    }

    /**
     * Acts just like CommandExecutor#onCommand(4), except any time an unhandled
     * exception occurs, it's sent over a network socket to a remote server.
     *
     * The address and port of the remote server are specified by overriding
     * getHostname and getPort.
     *
     * @param cs
     * @param cmnd
     * @param string
     * @param strings
     * @return
     */
    public abstract boolean executeCommand(CommandSender cs, Command cmnd, String string, String[] strings);

    /**
     * Returns any plugin-specific information the developer may want, to help
     * debugging
     *
     * @return
     */
    protected abstract String getEnvOptions();

    /**
     * Returns the hostname of the server that the remote BugReportDaemon will
     * be running on
     *
     * @return
     */
    protected abstract String getHostname();

    /**
     * Returns the port the remote server will be listening on
     *
     * @return
     */
    protected abstract int getPort();

    /**
     * Returns the plugin whose commands we want to report on (just have this
     * method return a reference to your JavaPlugin)
     *
     * @return
     */
    protected abstract Plugin getPlugin();
}
