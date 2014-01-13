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
public class NotEnoughRequiredArgsTest {
    
    @Test
    public void notEnoughRequiredArgsTest() {
        TreeCommandExecutor e = new TreeCommandExecutor();
        final boolean[] passed = new boolean[] {false, false};
        
        CommandLeaf testLeaf = new CommandLeaf("test method ! ! ?") {
            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                passed[0] = true;
            }
            
            @Override
            public String getMissingRequiredArgsHelpMessage() {
                passed[1] = true;
                return "Error: Not enough args";
            }
        };
        
        e.add(testLeaf);
        
        assertTrue(e.onCommand(new MockCommandSender(), null, "test", new String[]{"method", "one"}));
        assertFalse(passed[0]);
        assertTrue(passed[1]);
    }
    
}
