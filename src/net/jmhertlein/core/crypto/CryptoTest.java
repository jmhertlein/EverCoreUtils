package net.jmhertlein.core.crypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 * @author joshua
 */
public class CryptoTest {

    public static void main(String[] args) {
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoTest.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        keyPairGen.initialize(2048);

        KeyPair pair = keyPairGen.generateKeyPair();

        System.out.println("Private:");
        System.out.println(pair.getPrivate());

        System.out.println("Public:");
        System.out.println(pair.getPublic());

        KeyFactory fact;
        try {
            fact = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoTest.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        String privateKey = new BASE64Encoder().encode(pair.getPrivate().getEncoded());
        String publicKey = new BASE64Encoder().encode(pair.getPublic().getEncoded());
        
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Private Key:");
        System.out.println(privateKey);
        
        System.out.println("Public key:");
        System.out.println(publicKey);
        
        //save
        try(FileOutputStream fos = new FileOutputStream("rsa.pub"); PrintStream ps = new PrintStream(fos)) {
            ps.println(publicKey);
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        
        //read
        byte[] decoded;
        try(Scanner scan = new Scanner(new File("rsa.pub"))) {
            String output = "";
            while(scan.hasNextLine()) {
                output += scan.nextLine();
            }
            
            System.out.println("Input read:");
            System.out.println(output);
            
            BASE64Decoder decoder = new BASE64Decoder();
            decoded = decoder.decodeBuffer(output);
            
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        try {
            
            System.out.println(new BASE64Encoder().encode(KeyFactory.getInstance("RSA").generatePublic(spec).getEncoded()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch(InvalidKeySpecException ikse) {
            ikse.printStackTrace();
            return;
        }
    }
}
