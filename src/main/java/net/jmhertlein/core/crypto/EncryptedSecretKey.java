/*
 * Copyright (C) 2013 Joshua Michael Hertlein <jmhertlein@gmail.com>
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

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author joshua
 */
public class EncryptedSecretKey implements Serializable {

    private byte[] encoded;
    private boolean twiceEncrypted;

    /**
     * 
     * @param keyToEncrypt
     * @param encryptingKey
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public EncryptedSecretKey(SecretKey keyToEncrypt, PublicKey encryptingKey) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, encryptingKey);
        
        encoded = c.doFinal(keyToEncrypt.getEncoded());
        twiceEncrypted = false;
    }
    
    /**
     * Encrypts the secret key first with the server's private key, to ensure authenticity, then with the client's public key, to ensure privacy
     * @param keyToEncrypt the secret key to be encrypted
     * @param clientKey the client's public key
     * @param serverKey the server's private key
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException 
     */
    public EncryptedSecretKey(SecretKey keyToEncrypt, PublicKey clientKey, PrivateKey serverKey) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher c = Cipher.getInstance("RSA");
        twiceEncrypted = true;
        
        c.init(Cipher.ENCRYPT_MODE, serverKey);
        encoded = c.doFinal(keyToEncrypt.getEncoded());
        
        c.init(Cipher.ENCRYPT_MODE, clientKey);
        encoded = c.doFinal(encoded);
    }

    public byte[] getEncoded() {
        return encoded;
    }
    
    public SecretKey decrypt(PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, key);
        
        byte[] decrypted = c.doFinal(encoded);
        return Keys.getAESSecretKeyFromEncoded(decrypted);
    }
    
    public SecretKey decrypt(PrivateKey clientKey, PublicKey serverKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, clientKey);
        byte[] decrypted = c.doFinal(encoded);
        
        c.init(Cipher.DECRYPT_MODE, serverKey);
        c.doFinal(decrypted);
        
        return Keys.getAESSecretKeyFromEncoded(decrypted);
    }

    public boolean isTwiceEncrypted() {
        return twiceEncrypted;
    }
}
