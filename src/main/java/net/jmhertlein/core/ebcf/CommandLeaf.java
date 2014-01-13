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

package net.jmhertlein.core.ebcf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * A CommandLeaf is the executable leaf of a tree of commands, and represents the actual command.
 * @author joshua
 */
public abstract class CommandLeaf {
    private final String[] nodeStrings;
    private final int requiredArgs, optionalArgs;
    
    /**
     * Creates a new CommandLeaf. The constructor takes one String argument,
     * and it should be the slash command exactly as a player would type it,
     * with these differences:
     * - no leading "/"
     * - all arguments must be at the end of the command
     * - Replace required arguments with "!", and optional arguments with "?"
     * - The elements must occur in this order: <command> <required args> <optional args>
     * 
     * Ex: "bank deposit currency !" (where ! will be the amount of currency 
     * the user wants to deposit)
     * @param command the formatted command string, see above
     */
    public CommandLeaf(String command) {
        int required = 0,
            optional = 0;
        
        String[] split = command.split(" ");
        
        int i = split.length-1;
        for(; i >= 0 && split[i].equals("?"); i--)
            optional++;
        for(; i >= 0 && split[i].equals("!"); i--)
            required++;
        
        nodeStrings = new String[i+1];
        System.arraycopy(split, 0, nodeStrings, 0, nodeStrings.length);
        requiredArgs = required;
        optionalArgs = optional;
        
        if(nodeStrings.length == 0)
            throw new RuntimeException("Invalid command: Must have at least one non-argument string.");
    }

    /**
     * 
     * @return how many required arguments the leaf requires
     */
    public int getNumRequiredArgs() {
        return requiredArgs;
    }

    /**
     * how many additional optional arguments the leaf will accept
     * @return
     */
    public int getNumOptionalArgs() {
        return optionalArgs;
    }
    
    /**
     *
     * @param index the index of the string to retrieve (0 is the first string)
     * @return The string at position 'index' of the command string (no arguments included)
     */
    public String getStringAt(int index) {
        return index < nodeStrings.length ? nodeStrings[index] : null;
    }
    
    /**
     *
     * @return an unmodifiable list of all substrings in the command string (space-delimited)
     */
    public List<String> getStringNodes() {
        return Collections.unmodifiableList(Arrays.asList(nodeStrings));
    }
    
    /**
     * The analogue to CommandExecutor::onCommand(). This is called when a
     * player has successfully type the command and supplied enough required args
     * @param sender the CommandSender executing the command
     * @param cmd
     * @param args required arguments and optional arguments, required arguments first.
     * @throws InsufficientPermissionException if the sender doesn't have sufficient permission to run the command
     * @throws UnsupportedCommandSenderException if the sender is not able to run the command (example: sender is console instead of Player)
     */
    public abstract void execute(CommandSender sender, Command cmd, String[] args) throws InsufficientPermissionException,UnsupportedCommandSenderException;
    
    /**
     * 
     * @return the message to be sent to the user if they correctly type the command, but don't supply enough required arguments
     */
    public abstract String getMissingRequiredArgsHelpMessage();
}
