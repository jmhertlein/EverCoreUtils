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

import net.jmhertlein.core.ebcf.TreeCommandExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joshua
 */
public class CommandDefinitionTest {
    private TreeCommandExecutor e;
    private SampleCommandDefinition d;
    
    
    @Before
    public void setUp() {
        e = new TreeCommandExecutor();
        d = new SampleCommandDefinition();
        e.add(d);
    }
    
    @After
    public void tearDown() {
        e = null;
        d = null;
    }
    
    @Test
    public void testSimple() {
        e.onCommand(new MockCommandSender(), new MockCommand("sayone"), "sayone", new String[0]);
        assertEquals(d.getRan(), "sayone");
    }
    
    @Test
    public void testLongs() {
        e.onCommand(new MockCommandSender(), new MockCommand("this"), "this", new String[]{"one", "is", "really",  "pretty", "long"});
        assertEquals(d.getRan(), "longcmd");
        
        e.onCommand(new MockCommandSender(), new MockCommand("this"), "this", new String[]{"one", "is", "really",  "pretty", "different"});
        assertEquals(d.getRan(), "longcmddiff");
    }
    
    @Test
    public void testEchoNoArgs() {
        e.onCommand(new MockCommandSender(), new MockCommand("echo"), "echo", new String[0]);
        assertNull(d.getRan());
    }
    
    @Test
    public void testEchoWithArgs() {
        e.onCommand(new MockCommandSender(), new MockCommand("echo"), "echo", new String[]{"echo", "this,", "machine!"});
        assertEquals(d.getRan(), "echo");
    }
    
    @Test
    public void testLongWithArgs() {
        e.onCommand(new MockCommandSender(), new MockCommand("long"), "long", new String[]{"command", "path", "arg1", "arg2", "arg3"});
        assertEquals(d.getRan(), "longcmdwithargs");
        assertEquals(d.getArgsPassed(), 3);
    }
    
    @Test
    public void testInvalidParams() {
        boolean exceptionThrown = false;
        e.add(new SampleInvalidCommandDefinition());
        try {
            e.onCommand(new MockCommandSender(), new MockCommand("invalid"), "invalid", new String[0]);
        } catch(RuntimeException ex) {
            System.out.println("testInvalidParams() correctly threw an exception!");
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);        
    }
}
