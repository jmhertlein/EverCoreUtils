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
    }
    
    @Test
    public void testSimple() {
        e.onCommand(new MockCommandSender(), null, "sayone", new String[0]);
        assertEquals(d.getRan(), "sayone");
    }
    
    @Test
    public void testLongs() {
        e.onCommand(new MockCommandSender(), null, "this", new String[]{"one", "is", "really",  "pretty", "long"});
        assertEquals(d.getRan(), "longcmd");
        
        e.onCommand(new MockCommandSender(), null, "this", new String[]{"one", "is", "really",  "pretty", "different"});
        assertEquals(d.getRan(), "longcmddiff");
    }
    
    @Test
    public void testEchoNoArgs() {
        e.onCommand(new MockCommandSender(), null, "echo", new String[0]);
        assertNull(d.getRan());
    }
    
    @Test
    public void testEchoWithArgs() {
        e.onCommand(new MockCommandSender(), null, "echo", new String[]{"echo", "this,", "machine!"});
        assertEquals(d.getRan(), "echo");
    }
}
