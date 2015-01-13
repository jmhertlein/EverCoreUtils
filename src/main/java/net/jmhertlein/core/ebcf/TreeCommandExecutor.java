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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jmhertlein.core.ebcf.annotation.ExecutableCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author joshua
 */
public class TreeCommandExecutor implements CommandExecutor {

    private CommandNode root;
    private Set<CommandLeaf> leaves;

    /**
     * Creates a new instance of a TreeCommandExecutor
     *
     * It is ready to have leaves added to it and to be set as the executor for a command
     */
    public TreeCommandExecutor() {
        root = new CommandNode(null, null);
        leaves = new HashSet<>();
    }
    
    public void add(final CommandDefinition c) {
        Method[] methods = c.getClass().getMethods();
        
        for(final Method m : methods) {
            final ExecutableCommand cmdInfo = (ExecutableCommand) m.getAnnotation(ExecutableCommand.class);
            if(cmdInfo == null) {
                continue;
            }
                    
            CommandLeaf f = new CommandLeaf(cmdInfo.path(), cmdInfo.requiredArgs()) {
                @Override
                public void execute(CommandSender sender, Command cmd, String[] args) throws InsufficientPermissionException, UnsupportedCommandSenderException {
                    if(cmdInfo.permNode() != null && !sender.hasPermission(cmdInfo.permNode())) {
                        throw new InsufficientPermissionException();
                    }
                    
                    boolean isPlayer = sender instanceof Player;
                    if(      (isPlayer && !cmdInfo.player()) || 
                            (!isPlayer && !cmdInfo.console())) {
                        throw new UnsupportedCommandSenderException(sender);
                    }
                    
                    try {
                        m.invoke(c, sender, args);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(TreeCommandExecutor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                @Override
                public String getMissingRequiredArgsHelpMessage() {
                    return cmdInfo.helpMsg();
                }
            };
            
            add(f);
        }
    }

    /**
     *
     * @param cmd the commandleaf to be added
     *
     * @throws RuntimeException if a duplicate command is added
     */
    public void add(CommandLeaf cmd) {
        CommandNode temp = root;
        for (String s : cmd.getStringNodes()) {
            CommandNode next = temp.getChild(s);

            if (next == null) {
                next = new CommandNode(temp, s);
                temp.addChild(next);
            }

            temp = next;
        }

        if (temp.executable != null)
            throw new RuntimeException("Error: leaf node already has command bound");

        temp.executable = cmd;
        leaves.add(cmd);
    }

    /**
     *
     * @param sender
     * @param command
     * @param label
     * @param args
     *
     * @return
     */
    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandNode cur = root.getChild(label); //TODO: check if this breaks aliases
        if (cur == null)
            cur = getAutoCompletedNextNode(root, label);
        if (cur == null)
            return false;

        int i = 0;
        while (!cur.children.isEmpty() && i < args.length) {
            CommandNode next = cur.getChild(args[i]);
            if (next == null)
                next = getAutoCompletedNextNode(cur, args[i]);

            if (next == null) {
                sendInvalidCommandHelp(sender, root, cur, args[i]);
                return true;
            }
            cur = next;
            i++;
        }

        CommandNode selectedLeaf = cur;
        //once we reach the end, assume the rest of the stuff in args are actually arguments
        if (selectedLeaf.executable == null) {
            sendIncompleteCommandHelp(sender, root, selectedLeaf);
            return true;
        }

        String[] cmdArgs = new String[args.length - i];
        System.arraycopy(args, i, cmdArgs, 0, cmdArgs.length);

        if (cmdArgs.length < selectedLeaf.executable.getNumRequiredArgs()) {
            sender.sendMessage(selectedLeaf.executable.getMissingRequiredArgsHelpMessage());
            return true;
        }
        try {
            selectedLeaf.executable.execute(sender, command, cmdArgs);
        } catch (InsufficientPermissionException ex) {
            if (ex.hasCustomMessage())
                sender.sendMessage(ChatColor.RED + ex.getCustomMessage());
            else
                sender.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
            return true;
        } catch (UnsupportedCommandSenderException ex) {
            sender.sendMessage(ChatColor.RED + "This command does not support being run in your context (you're probably console!).");
            return true;
        }

        return true;
    }

    private static List<String> composeChildNodesString(CommandNode selectedLeaf) {
        List<String> ret = new LinkedList<>();
        for (CommandNode child : selectedLeaf.children.values())
            ret.add(child.nodeString);
        
        return ret;
    }

    private class CommandNode {

        CommandNode parent;
        CommandLeaf executable;
        String nodeString;
        Map<String, CommandNode> children;

        public CommandNode() {
            children = new HashMap<>();
        }

        public CommandNode(CommandNode parent, String nodeString) {
            this.parent = parent;
            this.nodeString = nodeString;
            children = new HashMap<>();
        }

        public void addChild(CommandNode n) {
            children.put(n.nodeString, n);
        }

        public CommandNode getChild(String nodeString) {
            return children.get(nodeString);
        }
    }

    /**
     *
     * @return
     */
    public Set<CommandLeaf> getLeaves() {
        return Collections.unmodifiableSet(leaves);
    }

    private static void sendIncompleteCommandHelp(CommandSender sender, CommandNode root, CommandNode selectedLeaf) {
        //they didn't type a complete command, so tell them what they might want to type next
        //print children node strings
        sender.sendMessage(ChatColor.RED + "Incomplete command: \"/" + composeCommandParentage(root, selectedLeaf) + "\"");
        sender.sendMessage(ChatColor.YELLOW + "Possible completions:");
        for (String s : composeChildNodesString(selectedLeaf)) {
            sender.sendMessage(ChatColor.AQUA + s);
        }
    }

    private static void sendInvalidCommandHelp(CommandSender sender, CommandNode root, CommandNode currentNode, String invalidNodeString) {
        sender.sendMessage(String.format("%sInvalid command: \"%s%s%s\"", ChatColor.RED, ChatColor.DARK_RED, composeCommandParentage(root, currentNode) + " " + invalidNodeString, ChatColor.RED));
        sender.sendMessage(String.format("%sPossible replacements for \"%s%s%s\"", ChatColor.YELLOW, ChatColor.DARK_RED, invalidNodeString, ChatColor.YELLOW));
        for (String s : composeChildNodesString(currentNode)) {
            sender.sendMessage(ChatColor.AQUA + s);
        }
    }

    private static String composeCommandParentage(CommandNode root, CommandNode n) {
        return n.parent == root ? n.nodeString : composeCommandParentage(root, n.parent) + " " + n.nodeString;
    }

    private static CommandNode getAutoCompletedNextNode(CommandNode node, String token) {
        for (CommandNode n : node.children.values()) {
            if (n.nodeString.startsWith(token))
                return n;
        }

        return null;
    }
}
