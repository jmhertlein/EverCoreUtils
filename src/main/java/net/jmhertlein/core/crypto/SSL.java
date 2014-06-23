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
package net.jmhertlein.core.crypto;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author joshua
 */
public class SSL {
    /**
     * Creates an SSL context for a TLS connection, which trusts the keys in
     * the specified keystore
     *
     * @param store the keys to trust
     * @param keyStorePassword password for keystore
     * @return the ssl context
     * 
     * @throws KeyManagementException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     */
    public static SSLContext getSSLContext(KeyStore store, String keyStorePassword) throws KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        SSLContext ctx;
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(store);
            ctx = SSLContext.getInstance("TLS");
            //key manager factory go!
            KeyManagerFactory keyMgr = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyMgr.init(store, keyStorePassword.toCharArray());

            ctx.init(keyMgr.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SSL.class.getName()).log(Level.SEVERE, null, ex);
            ctx = null;
        }

        return ctx;
    }
}
