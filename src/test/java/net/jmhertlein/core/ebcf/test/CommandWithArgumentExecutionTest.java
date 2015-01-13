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
        final boolean[] passed = new boolean[]{false};
        CommandLeaf leaf = new CommandLeaf("test method3", 2) {
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

        e.onCommand(new MockCommandSender(), null, "test", new String[]{"method3", "arg1", "arg2", "arg3"});

        assertTrue(passed[0]);
    }

}
