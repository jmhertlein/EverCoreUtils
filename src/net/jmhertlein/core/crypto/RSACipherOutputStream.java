package net.jmhertlein.core.crypto;

import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author joshua
 */
public class RSACipherOutputStream extends CipherOutputStream {

    private Cipher c;

    public RSACipherOutputStream(OutputStream os, PublicKey pubkey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        super(os, prepareCipher(pubkey));
        this.c = prepareCipher(pubkey);
    }

    public Cipher getCipher() {
        return c;
    }

    protected static Cipher prepareCipher(Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, key);
        return c;
    }
}
