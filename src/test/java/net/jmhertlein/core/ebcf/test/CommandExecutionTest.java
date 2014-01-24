/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jmhertlein.core.ebcf.test;

import java.util.HashSet;
import java.util.Set;
import net.jmhertlein.core.ebcf.CommandLeaf;
import net.jmhertlein.core.ebcf.TreeCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author joshua
 */
public class CommandExecutionTest {

    @Test
    public void testCommandExecution() {
        final boolean[] b = new boolean[]{false};
        TreeCommandExecutor e = new TreeCommandExecutor();
        CommandLeaf leaf = new CommandLeaf("testplugin hello") {
            @Override
            public String getMissingRequiredArgsHelpMessage() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                b[0] = true;
            }
        };

        e.add(leaf);

        e.onCommand(new MockCommandSender(), null, "testplugin", new String[]{"hello"});

        assertTrue(b[0]);
    }
}
