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
package net.jmhertlein.core.command;

import static net.jmhertlein.core.chat.ChatUtil.ERR;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A wrapper object for a CommandSender lending easy introspection into whether it's a player or an op
 * @author joshua
 */
public class LocalSender {

    public static final String INSUF_PERM_MSG = "Insufficient permission.";
    protected CommandSender sender;
    protected Player player;
    protected boolean console;

    public LocalSender(CommandSender sender) {
        this.sender = sender;

        player = (sender instanceof Player ? (Player) sender : null);

        console = !(sender instanceof Player);
    }

    /**
     * Returns the player representation of the sender.
     *
     * @return the Player, or null if the sender is not a player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the CommandSender that this class wraps
     *
     * @return
     */
    public CommandSender getSender() {
        return sender;
    }

    //**************************************************************************
    //PERMISSIONS-RELATED METHODS
    //**************************************************************************
    /**
     * This checks whether a player has a permission node as defined by Bukkit's default permission framework,
     * but giving deference to the Console sender and ops.
     * 
     * @param node
     * @return true if the player is the console, an op, or has the permission node
     */
    public boolean hasExternalPermissions(String node) {
        return (!(this.getSender() instanceof Player)) || this.getSender().isOp() || this.getSender().hasPermission(node);
    }

    /**
     *
     * @return whether or not the CommandSender is the console
     */
    public boolean isConsole() {
        return console;
    }

    /**
     * Sends the user a message indicating that he does not have sufficent
     * permission.
     */
    public void notifyInsufPermissions() {
        sender.sendMessage(ERR + INSUF_PERM_MSG);
    }

    /**
     * Sends the sender a message.
     *
     * @param msg The message to be send
     */
    public void sendMessage(String msg) {
        sender.sendMessage(msg);
    }
}
