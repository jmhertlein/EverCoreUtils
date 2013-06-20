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
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author joshua
 */
public abstract class Keys {

    /**
     * Saves the given key to the given file. This method will NOT clobber
     * existing files- will return false if file exists The file will be
     * created, along with any parent directories needed.
     *
     * @param file name of file to save to
     * @param key key to save
     * @return true if successfully written, false otherwise
     */
    public static boolean storeKey(String file, Key key) {
        File f = new File(file);
        if (!f.exists()) {
            try {
                if (f.getParentFile() != null) {
                    f.getParentFile().mkdirs();
                }
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return false;
        }

        try (FileOutputStream fos = new FileOutputStream(file); PrintStream ps = new PrintStream(fos)) {
            ps.println(Base64.encodeBase64String(key.getEncoded()));
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    /**
     * Saves the given key to the given file. This method will NOT clobber
     * existing files- will return false if file exists The file will be
     * created, along with any parent directories needed.
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
            if (spec == null) {
                return null;
            }
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static PublicKey loadPubKey(String file) {
        try {
            X509EncodedKeySpec spec = getX509KeySpec(file);
            if (spec == null) {
                return null;
            }
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

            decoded = Base64.decodeBase64(output);

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

            decoded = Base64.decodeBase64(output);

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
        byte[] decoded = Base64.decodeBase64(encodedKey);

        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String getBASE64ForPublicKey(PublicKey key) {
        return Base64.encodeBase64String(key.getEncoded());
    }
}
