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
import java.security.Signature;
import java.security.SignatureException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;

/**
 * An object representing an AES secret key that has been encrypted by an RSA public key and optionally cryptographically signed by an RSA private key.
 * 
 * It is Serializable, and safe to write to streams and files.
 * 
 * @author joshua
 */
public final class EncryptedSecretKey implements Serializable {

    private final byte[] encoded, signature;

    /**
     * Constructs an object representing an encrypted AES secret key.
     * 
     * The key is encrypted with the given RSA public key.
     * @param keyToEncrypt
     * @param encryptingKey
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public EncryptedSecretKey(SecretKey keyToEncrypt, PublicKey encryptingKey) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, encryptingKey);
        
        encoded = c.doFinal(keyToEncrypt.getEncoded());
        signature = null;
    }
    
    /**
     * Constructs an object representing an encrypted AES secret key.
     * They key is encrypted with the given RSA public key.
     * 
     * This constructor also cryptographically signs the key using the specified RSA PrivateKey.
     * 
     * @param keyToEncrypt the secret key to be encrypted
     * @param clientKey the client's public key
     * @param signingKey the server's private key
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException 
     */
    public EncryptedSecretKey(SecretKey keyToEncrypt, PublicKey clientKey, PrivateKey signingKey) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, ShortBufferException, SignatureException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, clientKey);
        
        encoded = c.doFinal(keyToEncrypt.getEncoded());
        
        Signature s = Signature.getInstance("SHA256withRSA");
        s.initSign(signingKey);
        for(byte b : encoded)
            s.update(b);
        signature = s.sign();
    }

    /**
     * 
     * @return the (encrypted) encoded form of the key
     */
    public byte[] getEncoded() {
        return encoded;
    }
    
    /**
     * Decrypts the AES secret key using the given RSA private key.
     * 
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public SecretKey decrypt(PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, key);
        
        byte[] decrypted = c.doFinal(encoded);
        return Keys.getAESSecretKeyFromEncoded(decrypted);
    }
    
    /**
     * Decrypts the AES secret key using the given RSA private key, and verifies the signature using the given RSA public key.
     * 
     * @param clientKey the private key to decrypt the secret key
     * @param serverKey the public key to verify the signature
     * @return the decrypted SecretKey, or null if the signature verification fails
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws ShortBufferException
     * @throws SignatureException 
     */
    public SecretKey decrypt(PrivateKey clientKey, PublicKey serverKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ShortBufferException, SignatureException {
        if(!isSigned())
            throw new RuntimeException("This secret key is not signed, but signature checking was requested.");
        
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, clientKey);
        
        byte[] decrypted = c.doFinal(encoded);
        
        Signature s = Signature.getInstance("SHA256withRSA");
        s.initVerify(serverKey);
        
        for(byte b : encoded)
            s.update(b);
        
        if(s.verify(signature))
            return Keys.getAESSecretKeyFromEncoded(decrypted);
        else
            return null;
    }
    
    /**
     * 
     * @return true if this key was signed cryptographically, false otherwise
     */
    public boolean isSigned() {
        return signature != null;
    }
}
