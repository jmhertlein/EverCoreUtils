/*
 * Copyright (C) 2014 Joshua M Hertlein
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

package net.jmhertlein.core.crypto.persist.test;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import net.jmhertlein.core.crypto.Keys;
import net.jmhertlein.core.crypto.persist.LabeledKeyPair;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author joshua
 */
public class LabeledKeyPairTest {
    @Test
    public void testPersistance() {
        KeyPair pair = Keys.newRSAKeyPair(2048);
        LabeledKeyPair p = new LabeledKeyPair("label", pair);

        String yaml = p.saveToYamlString();

        LabeledKeyPair read = LabeledKeyPair.readFromYamlString(yaml);
        System.out.println(p);
        System.out.println(read);
        assertTrue(read.equals(p));
    }

    @Test
    public void testEquals() {
        final int KEY_LENGTH = 2048;
        KeyPair a = Keys.newRSAKeyPair(KEY_LENGTH),
                b = Keys.newRSAKeyPair(KEY_LENGTH),
                c = Keys.newRSAKeyPair(KEY_LENGTH);

        LabeledKeyPair w = new LabeledKeyPair("a", a),
                       x = new LabeledKeyPair("a", a),
                       y = new LabeledKeyPair("b", b),
                       z = new LabeledKeyPair("c", c);

        assertTrue(w.equals(w));
        assertTrue(w.equals(x));
        assertFalse(w.equals(y));
        assertFalse(w.equals(z));

        assertFalse(y.equals(z));
    }

    @Test
    public void testFileSaving() throws IOException {
        File f = File.createTempFile("ECU-LKP-Test", ".yml");

        LabeledKeyPair p = new LabeledKeyPair("test", Keys.newRSAKeyPair(2048));
        p.writeToYamlFile(f);

        LabeledKeyPair read = LabeledKeyPair.readFromYamlFile(f);
        assertTrue(p.equals(read));
        assertTrue(read.equals(p));
    }
}
