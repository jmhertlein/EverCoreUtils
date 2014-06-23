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

package net.jmhertlein.core.crypto.test;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.Security;
import java.security.SignatureException;
import net.jmhertlein.core.crypto.Certs;
import net.jmhertlein.core.crypto.Keys;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author joshua
 */
public class ECDSAKeyTest {

    @Before
    public void setup() {
        if(Security.getProvider("BC") == null)
            Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void testECDSAKeyGeneration() {
        assertNotNull(Keys.newECDSAKeyPair());
        assertNotNull(Keys.newECDSAKeyPair("BC"));
        assertNotNull(Keys.newECDSAKeyPair("secp521r1", "BC"));
    }

    @Test
    public void testECDSACert() throws SignatureException, InvalidKeyException {
        KeyPair pair = Keys.newECDSAKeyPair();
        assertNotNull(pair);
        assertNotNull(Certs.newCertificate(pair.getPrivate(), pair.getPublic(), 1, "junit"));
    }

}
