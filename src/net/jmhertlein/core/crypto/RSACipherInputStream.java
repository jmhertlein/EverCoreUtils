package net.jmhertlein.core.crypto;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author joshua
 */
public class RSACipherInputStream extends CipherInputStream {
    private Cipher c;
    public RSACipherInputStream(InputStream is, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        super(is, prepareCipher(privateKey));
        this.c = prepareCipher(privateKey);
    }
    
    public Cipher getCipher() {
        return c;
    }
    
    protected static Cipher prepareCipher(Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, key);
        return c;
    }
    
}
