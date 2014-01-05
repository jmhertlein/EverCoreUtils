/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.jmhertlein.core.crypto.persist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import net.jmhertlein.core.crypto.Keys;

/**
 *
 * @author joshua
 */
public class LabeledKeyPair extends LabeledPublicKey {
    private KeyPair keypair;

    public LabeledKeyPair(String label, KeyPair keypair) {
        super(label, keypair.getPublic());
        this.keypair = keypair;
    }

    public KeyPair getKeypair() {
        return keypair;
    }
    
    @Override
    public void writeToFile(File f) throws IOException {
        if(!f.exists())
            f.createNewFile();
        
        try (PrintWriter pw = new PrintWriter(f)) {
            pw.println("#Label:");
            pw.println(getLabel());
            pw.println("#Public key:");
            pw.println(Keys.getBASE64ForKey(keypair.getPublic()));
            pw.println("#Private key:");
            pw.println(Keys.getBASE64ForKey(keypair.getPrivate()));
        }
    }
    
    public static LabeledKeyPair readFromFile(File f) throws FileNotFoundException {
        Scanner scan = new Scanner(f);
        
        scan.nextLine(); //ignore commented line in input
        String label = scan.nextLine().trim();
        scan.nextLine(); //ignore commented line in input
        PublicKey pub = Keys.getPublicKeyFromBASE64X509Encoded(scan.nextLine().trim());
        scan.nextLine(); //ignore commented line in input
        PrivateKey priv = Keys.getPrivateKeyFromBASE64PKCS8Encoded(scan.nextLine().trim());
        
        return new LabeledKeyPair(label, new KeyPair(pub, priv));
    }
    
    @Override
    public void writeToFile(String filename) throws IOException {
        writeToFile(new File(filename));
    }
    
    public static LabeledKeyPair readFromFile(String filename) throws FileNotFoundException {
        return readFromFile(new File(filename));
    }
    
    public static List<LabeledKeyPair> loadKeyPairsInDir(File dir) throws FileNotFoundException {
        List<LabeledKeyPair> ret = new LinkedList<>();
        
        for(File f : dir.listFiles()) {
            if(f.getName().endsWith(".lkp"))
                ret.add(readFromFile(f));
        }
        
        return ret;
    }

    @Override
    public String getExtension() {
        return ".lkp";
    }
}
