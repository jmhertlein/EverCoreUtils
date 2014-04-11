/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jmhertlein.core.crypto.persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import net.jmhertlein.core.crypto.Keys;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * An object representing a KeyPair with an associated label.
 *
 * This is handy for writing a KeyPair with a label to disk (the keys are saved as Base64 encodings)
 *
 * @author joshua
 */
public class LabeledKeyPair {
    private KeyPair keypair;
    private String label;

    public LabeledKeyPair(String label, KeyPair keypair) {
        this.label = label;
        this.keypair = keypair;
    }

    public String saveToYamlString() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        Map<String, Object> m = new HashMap<>();
        m.put("label", label);
        m.put("public", Keys.getBASE64ForKey(keypair.getPublic()));
        m.put("private", Keys.getBASE64ForKey(keypair.getPrivate()));

        return yaml.dump(m);
    }

    public void writeToYamlFile(File f) throws IOException {
        if(!f.exists())
            f.createNewFile();

        try (PrintWriter pw = new PrintWriter(f)) {
            pw.write(saveToYamlString());
        }

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static LabeledKeyPair readFromYamlFile(File f) throws IOException {
        String s;

        try(Scanner scan = new Scanner(f)) {
            scan.useDelimiter("\\A");
            s = scan.next();
        }

        return readFromYamlString(s);
    }

    public static LabeledKeyPair readFromYamlString(String s) {
        Yaml yaml = new Yaml();
        Map<String, Object> m = (Map<String, Object>) yaml.load(s);

        return new LabeledKeyPair((String) m.get("label"),
                                                 new KeyPair(Keys.getPublicKeyFromBASE64X509Encoded((String) m.get("public")),
                                                             Keys.getPrivateKeyFromBASE64PKCS8Encoded((String) m.get("private"))));
    }

    public KeyPair getKeypair() {
        return keypair;
    }



    public static List<LabeledKeyPair> loadKeyPairsInDir(File dir) throws IOException {
        List<LabeledKeyPair> ret = new LinkedList<>();

        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".yml"))
                ret.add(readFromYamlFile(f));
        }

        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.keypair);
        hash = 37 * hash + Objects.hashCode(this.label);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final LabeledKeyPair other = (LabeledKeyPair) obj;

        if(!other.keypair.getPrivate().equals(keypair.getPrivate()))
            return false;
        if(!other.keypair.getPublic().equals(keypair.getPublic()))
            return false;
        if(!other.label.equals(label))
            return false;
        return true;
    }


    public String toVerboseString() {
        StringBuilder b = new StringBuilder();
        b.append("=====LabeledKeyPair=======\n");
        b.append("Pubkey:\n");
        b.append(Keys.getBASE64ForKey(keypair.getPublic()));
        b.append("\nPrivate key:\n");
        b.append(Keys.getBASE64ForKey(keypair.getPrivate()));
        b.append("\n==========================");
        return b.toString();
    }

    @Override
    public String toString() {
        return label;
    }
}
