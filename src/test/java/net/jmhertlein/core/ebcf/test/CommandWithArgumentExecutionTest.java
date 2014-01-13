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
public class CommandWithArgumentExecutionTest {
    @Test
    public void testCommandWithArgsTest() {
        final boolean[] passed = new boolean[] {false};
        CommandLeaf leaf = new CommandLeaf("test method3 ! ! ?") {
            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                System.out.println("I'm method3 and I have " + args.length + " args.");
                passed[0] = true;
                System.out.println("My args are: " + args[0] + "|" + args[1] + "|" + args[2]);
            }

            @Override
            public String getMissingRequiredArgsHelpMessage() {
                return "Error: not enough args. Need " + getNumRequiredArgs() + ".";
            }
        };
        
        TreeCommandExecutor e = new TreeCommandExecutor();
        e.add(leaf);
        
        e.onCommand(new MockCommandSender(), null, "test", new String[] {"method3", "arg1", "arg2", "arg3"});
        
        assertTrue(passed[0]);
    }
    
}
