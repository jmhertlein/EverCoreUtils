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
package net.jmhertlein.core.crypto.test;

import java.security.KeyPair;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import net.jmhertlein.core.crypto.EncryptedSecretKey;
import net.jmhertlein.core.crypto.Keys;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joshua
 */
public class EncryptedSecretKeyTest {
    @Test
    public void testTwoPassEncryption() {
        SecretKey key = Keys.newAESKey(128);

        KeyPair serverKeys = Keys.newRSAKeyPair(1024),
                clientKeys = Keys.newRSAKeyPair(1024);
        try {
            EncryptedSecretKey wrapper = new EncryptedSecretKey(key, clientKeys.getPublic(), serverKeys.getPrivate());

            SecretKey decryptedKey = wrapper.decrypt(clientKeys.getPrivate(), serverKeys.getPublic());

            assertNotNull(decryptedKey);
            assertEquals(key, decryptedKey);
        } catch (Exception ex) {
            Logger.getLogger(EncryptedSecretKeyTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue(false);
        }
    }
}
