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

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joshua
 */
public class KeyStores {

    /**
     * Creates an empty key store
     *
     * Technically, since the password is in a string, it'll leak into the string pool
     * But good luck going through your whole program with the password in a char[]. so this is for
     * convenience
     * @param password with which to protect the store
     * @return an empty key store, or null if anything goes wrong
     */
    public static KeyStore getEmptyKeyStore(String password) {
        KeyStore store;
        try {
            store = KeyStore.getInstance(KeyStore.getDefaultType());
            store.load(null, password.toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
            Logger.getLogger(KeyStores.class.getName()).log(Level.SEVERE, null, ex);
            store = null;
        }

        return store;
    }
}
