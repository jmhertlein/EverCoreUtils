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
public class CommandPassedCorrectArgumentsTest {
    @Test
    public void commandPassedCorrectArgumentsTest() {
        TreeCommandExecutor e = new TreeCommandExecutor();
        CommandLeaf leaf = new CommandLeaf("test method", 2) {
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
        e.add(leaf);

        e.onCommand(new MockCommandSender(), null, "test", new String[]{"method", "arg1", "arg2", "arg3opt", "arg4opt"});

    }
}
