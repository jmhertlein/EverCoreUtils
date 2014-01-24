/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jmhertlein.core.ebcf.test;

import com.avaje.ebeaninternal.server.core.OnBootupClassSearchMatcher;
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
public class AutoCompleteTest {
    @Test
    public void autoCompleteTest() {
        TreeCommandExecutor e = new TreeCommandExecutor();
        final boolean[] passed = new boolean[]{false};

        CommandLeaf testLeaf = new CommandLeaf("test method quite long ? ?") {
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

        assertTrue(e.onCommand(new MockCommandSender(), null, "test", new String[]{"method", "quite", "long"}));
        assertTrue(passed[0]);
        passed[0] = false;

        assertTrue(e.onCommand(new MockCommandSender(), null, "te", new String[]{"me", "qu", "lo"}));
        assertTrue(passed[0]);
        passed[0] = false;

        assertTrue(e.onCommand(new MockCommandSender(), null, "t", new String[]{"m", "q", "l"}));
        assertTrue(passed[0]);
        passed[0] = false;
    }
}
