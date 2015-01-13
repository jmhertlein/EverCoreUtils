/*
 * Copyright (C) 2015 Joshua Michael Hertlein <jmhertlein@gmail.com>
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

        leaves.add(new CommandLeaf("test method0", 0) {
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

        leaves.add(new CommandLeaf("test method1", 1) {
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

        leaves.add(new CommandLeaf("test method2", 2) {
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

        leaves.add(new CommandLeaf("test method3", 2) {
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
