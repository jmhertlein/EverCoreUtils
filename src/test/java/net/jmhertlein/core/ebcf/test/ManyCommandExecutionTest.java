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

/**
 *
 * @author joshua
 */
public class ManyCommandExecutionTest {

    @Test
    public void testManyCommandsExecution() {
        TreeCommandExecutor e = new TreeCommandExecutor();

        final boolean[] passed = new boolean[]{false, false, false, false};

        for (CommandLeaf l : generateTestLeaves(passed)) {
            e.add(l);
            assertTrue(e.getLeaves().contains(l));
        }

        e.onCommand(new MockCommandSender(), null, "test", new String[]{"method0"});
        e.onCommand(new MockCommandSender(), null, "test", new String[]{"method1", "firstArg"});
        e.onCommand(new MockCommandSender(), null, "test", new String[]{"method2", "firstArg", "secondArg"});
        e.onCommand(new MockCommandSender(), null, "test", new String[]{"method3", "firstArg", "secondArg", "thirdArgOpt"});

        for (boolean b : passed) {
            assertTrue(b);
        }
    }

    private Set<CommandLeaf> generateTestLeaves(final boolean[] passed) {
        Set<CommandLeaf> leaves = new HashSet<>();

        leaves.add(new CommandLeaf("test method0") {
            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                System.out.println("I'm method0 and I have " + args.length + " args.");
                passed[0] = true;
            }

            @Override
            public String getMissingRequiredArgsHelpMessage() {
                return "Error: not enough args. Need " + getNumRequiredArgs() + ".";
            }
        });

        leaves.add(new CommandLeaf("test method1 !") {
            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                System.out.println("I'm method1 and I have " + args.length + " args.");
                System.out.println("My argument is: " + args[0]);
                passed[1] = true;
            }

            @Override
            public String getMissingRequiredArgsHelpMessage() {
                return "Error: not enough args. Need " + getNumRequiredArgs() + ".";
            }
        });

        leaves.add(new CommandLeaf("test method2 ! !") {
            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                System.out.println("I'm method2 and I have " + args.length + " args.");
                passed[2] = true;
            }

            @Override
            public String getMissingRequiredArgsHelpMessage() {
                return "Error: not enough args. Need " + getNumRequiredArgs() + ".";
            }
        });

        leaves.add(new CommandLeaf("test method3 ! ! ?") {
            @Override
            public void execute(CommandSender sender, Command cmd, String[] args) {
                System.out.println("I'm method3 and I have " + args.length + " args.");
                passed[3] = true;
                System.out.println("My args are: " + args[0] + "|" + args[1] + "|" + args[2]);
            }

            @Override
            public String getMissingRequiredArgsHelpMessage() {
                return "Error: not enough args. Need " + getNumRequiredArgs() + ".";
            }
        });

        return leaves;
    }

}
