/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
