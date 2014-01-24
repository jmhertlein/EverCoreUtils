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
public class InvalidCommandTest {
    @Test
    public void testInvalidCommand() {
        TreeCommandExecutor e = new TreeCommandExecutor();
        final boolean[] passed = new boolean[]{false};

        CommandLeaf testLeaf = new CommandLeaf("testplugin hello") {
            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                passed[0] = true;
            }

            @Override
            public String getMissingRequiredArgsHelpMessage() {
                return "Error: Not enough args";
            }
        };

        e.add(testLeaf);

        assertFalse(e.onCommand(new MockCommandSender(), null, "testnotplugin", new String[0]));
        assertFalse(passed[0]);
        assertTrue(e.onCommand(new MockCommandSender(), null, "testplugin", new String[]{"nope"}));
        assertFalse(passed[0]);
    }

}
