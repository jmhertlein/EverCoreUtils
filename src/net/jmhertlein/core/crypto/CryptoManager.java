package net.jmhertlein.core.crypto;

import net.jmhertlein.core.crypto.test.CryptoTest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author joshua
 */
public class CryptoManager {

    public void storeKey(String file, Key key) {
        File f = new File(file);
        if (!f.exists())
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(CryptoManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        try (FileOutputStream fos = new FileOutputStream(file); PrintStream ps = new PrintStream(fos)) {
            ps.println(new BASE64Encoder().encode(key.getEncoded()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public PrivateKey loadPrivateKey(String file) {
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(getPKCS8KeySpec(file));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(CryptoManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public PublicKey loadPubKey(String file) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(getX509KeySpec(file));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(CryptoManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public PublicKey loadPubKey(File f) {
        return loadPubKey(f.getPath());
    }

    public PrivateKey loadPrivateKey(File f) {
        return loadPrivateKey(f.getPath());
    }

    private X509EncodedKeySpec getX509KeySpec(String file) {
        byte[] decoded;
        try (Scanner scan = new Scanner(new File(file))) {
            String output = "";
            while (scan.hasNextLine()) {
                output += scan.nextLine();
            }

            //System.out.println("Input read:");
            //System.out.println(output);

            BASE64Decoder decoder = new BASE64Decoder();
            decoded = decoder.decodeBuffer(output);

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        return new X509EncodedKeySpec(decoded);
    }

    private PKCS8EncodedKeySpec getPKCS8KeySpec(String file) {
        byte[] decoded;
        try (Scanner scan = new Scanner(new File(file))) {
            String output = "";
            while (scan.hasNextLine()) {
                output += scan.nextLine();
            }

            BASE64Decoder decoder = new BASE64Decoder();
            decoded = decoder.decodeBuffer(output);

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        return new PKCS8EncodedKeySpec(decoded);
    }

    public static KeyPair newRSAKeyPair(int bits) {
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoTest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        keyPairGen.initialize(bits);

        return keyPairGen.generateKeyPair();
    }
}
