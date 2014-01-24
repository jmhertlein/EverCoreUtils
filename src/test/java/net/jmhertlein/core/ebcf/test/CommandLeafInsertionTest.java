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
public class CommandLeafInsertionTest {

    @Test
    public void testCommandInsertion() {
        TreeCommandExecutor e = new TreeCommandExecutor();
        CommandLeaf testLeaf = new CommandLeaf("testplugin hello") {
            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                System.out.println("Hello World");
            }

            @Override
            public String getMissingRequiredArgsHelpMessage() {
                return "Error: Not enough args";
            }
        };

        e.add(testLeaf);

        assertTrue(e.getLeaves().contains(testLeaf));
    }

}
