/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jmhertlein.core.ebcf.test;

import net.jmhertlein.core.ebcf.CommandLeaf;
import net.jmhertlein.core.ebcf.TreeCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joshua
 */
public class CommandPassedCorrectArgumentsTest {
    @Test
    public void commandPassedCorrectArgumentsTest() {
        TreeCommandExecutor e = new TreeCommandExecutor();
        CommandLeaf leaf = new CommandLeaf("test method ! ! ? ?") {
            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                assertTrue(args.length == 4);
                assertTrue(args[0].equals("arg1"));
                assertTrue(args[1].equals("arg2"));
                assertTrue(args[2].equals("arg3opt"));
                assertTrue(args[3].equals("arg4opt"));
            }

            @Override
            public String getMissingRequiredArgsHelpMessage() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        assertTrue(leaf.getNumRequiredArgs() == 2);
        assertTrue(leaf.getNumOptionalArgs() == 2);
        e.add(leaf);

        e.onCommand(new MockCommandSender(), null, "test", new String[]{"method", "arg1", "arg2", "arg3opt", "arg4opt"});

    }
}
