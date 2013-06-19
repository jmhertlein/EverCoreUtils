package net.jmhertlein.core.crypto;

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
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author joshua
 */
public abstract class Keys {

    /**
     * Saves the given key to the given file.
     * This method will NOT clobber existing files- will return false if file exists
     * The file will be created, along with any parent directories needed.
     * 
     * @param file name of file to save to
     * @param key key to save
     * @return true if successfully written, false otherwise
     */
    public static boolean storeKey(String file, Key key) {
        File f = new File(file);
        if (!f.exists()) {
            try {
                if(f.getParentFile() != null)
                    f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else
            return false;
        
        try (FileOutputStream fos = new FileOutputStream(file); PrintStream ps = new PrintStream(fos)) {
            ps.println(new BASE64Encoder().encode(key.getEncoded()));
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
    
    /**
     * Saves the given key to the given file.
     * This method will NOT clobber existing files- will return false if file exists
     * The file will be created, along with any parent directories needed.
     * 
     * @param file name of file to save to
     * @param key key to save
     * @return true if successfully written, false otherwise
     */
    public static boolean storeKey(File f, Key key) {
        return storeKey(f.getAbsolutePath(), key);
    }

    public static PrivateKey loadPrivateKey(String file) {
        try {
            PKCS8EncodedKeySpec spec = getPKCS8KeySpec(file);
            if(spec == null)
                return null;
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static PublicKey loadPubKey(String file) {
        try {
            X509EncodedKeySpec spec = getX509KeySpec(file);
            if(spec == null)
                return null;
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static PublicKey loadPubKey(File f) {
        return loadPubKey(f.getPath());
    }

    public static PrivateKey loadPrivateKey(File f) {
        return loadPrivateKey(f.getPath());
    }

    private static X509EncodedKeySpec getX509KeySpec(String file) {
        byte[] decoded;
        try (Scanner scan = new Scanner(new File(file))) {
            String output = "";
            while (scan.hasNextLine()) {
                output += scan.nextLine();
            }

            BASE64Decoder decoder = new BASE64Decoder();
            decoded = decoder.decodeBuffer(output);

        } catch (IOException ioe) {
            return null;
        }

        return new X509EncodedKeySpec(decoded);
    }

    private static PKCS8EncodedKeySpec getPKCS8KeySpec(String file) {
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
            return null;
        }

        keyPairGen.initialize(bits);

        return keyPairGen.generateKeyPair();
    }
    
    public static SecretKey newAESKey(int bits) {
        KeyGenerator keyGen;
        
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
        
        keyGen.init(bits);
        
        return keyGen.generateKey();
    }
    
    public static PublicKey getPublicKeyFromBASE64X509Encoded(String encodedKey) {
        byte[] decoded;
        
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            decoded = decoder.decodeBuffer(encodedKey);
        } catch (IOException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static String getBASE64ForPublicKey(PublicKey key) {
        return new BASE64Encoder().encode(key.getEncoded());
    }
}
