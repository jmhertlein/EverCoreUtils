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
import java.security.PublicKey;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import net.jmhertlein.core.crypto.Keys;

/**
 *
 * @author joshua
 */
public class LabeledPublicKey {
    private String label;
    private PublicKey pubkey;

    public LabeledPublicKey(String label, PublicKey pubkey) {
        this.label = label;
        this.pubkey = pubkey;
    }

    public LabeledPublicKey(String label) {
        this.label = label;
        this.pubkey = null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PublicKey getPubkey() {
        return pubkey;
    }

    public void setPubkey(PublicKey pubkey) {
        this.pubkey = pubkey;
    }

    public void writeToFile(File f) throws IOException {
        if (!f.exists())
            f.createNewFile();

        try (PrintWriter pw = new PrintWriter(f)) {
            pw.println("#Label:");
            pw.println(label);
            if (pubkey != null) {
                pw.println("#Public key:");
                pw.println(Keys.getBASE64ForKey(pubkey));
            }
        }
    }

    public static LabeledPublicKey readFromFile(File f) throws FileNotFoundException {
        Scanner scan = new Scanner(f);

        scan.nextLine(); //ignore commented line in input
        String label = scan.nextLine().trim();
        if (scan.hasNextLine()) {
            scan.nextLine(); //ignore commented line in input
            PublicKey pub = Keys.getPublicKeyFromBASE64X509Encoded(scan.nextLine().trim());
            return new LabeledPublicKey(label, pub);
        } else
            return new LabeledPublicKey(label);
    }

    public void writeToFile(String filename) throws IOException {
        writeToFile(new File(filename));
    }

    public static LabeledPublicKey readFromFile(String filename) throws FileNotFoundException {
        return readFromFile(new File(filename));
    }

    public final void writeToFileInDir(File dir) throws IOException {
        writeToFile(new File(dir, getFilename()));
    }

    public String getFilename() {
        return label + getExtension();
    }

    public static List<LabeledPublicKey> loadKeysInDir(File dir) throws FileNotFoundException {
        List<LabeledPublicKey> ret = new LinkedList<>();

        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".lpk"))
                ret.add(readFromFile(f));
        }

        return ret;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getExtension() {
        return ".lpk";
    }
}
