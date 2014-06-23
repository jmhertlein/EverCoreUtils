/*
 * Copyright (C) 2014 Joshua M Hertlein
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

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;

/**
 *
 * @author joshua
 */
public class Certs {
    /**
     * Generates an X509 certificate
     *
     * Valid for 1000 years from 1 second ago, signed with SHA512withECDSA
     * @param authorityPrivateKey private key of the certificate authority
     * @param certPubKey pubkey that the certificate will use
     * @param serialNumber serial number of the certificate
     * @param commonName common name of the subject of this certificate
     * @return a new X509 Certificate
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static Certificate newCertificate(PrivateKey authorityPrivateKey, PublicKey certPubKey, long serialNumber, String commonName) throws SignatureException, InvalidKeyException {
        X509V3CertificateGenerator gen = new X509V3CertificateGenerator();

        gen.setPublicKey(certPubKey); //pubkey of cert we're making
        gen.setSignatureAlgorithm("SHA512withECDSA");
        gen.setSerialNumber(BigInteger.valueOf(Math.abs(serialNumber)));
        gen.setIssuerDN(new X500Principal("CN=" + commonName));
        gen.setNotBefore(new Date(System.currentTimeMillis() - 1000L));
        gen.setNotAfter(new Date(System.currentTimeMillis() + 1000*365*24*60*60*1000)); //tfw cert valid for 10x longer than I'll be alive
        gen.setSubjectDN(new X500Principal("CN=" + commonName));

        try {
            return gen.generate(authorityPrivateKey); //ca's private key
        } catch (CertificateEncodingException | IllegalStateException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Certs.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Generates an X509 certificate
     *
     * Valid for 1000 years from 1 second ago, signed with SHA512withECDSA
     * Random serial number
     *
     * @param authorityPrivateKey private key of the certificate authority
     * @param certPubKey pubkey that the certificate will use
     * @param commonName common name of the subject of this certificate
     * @return a new X509 Certificate
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static Certificate newCertificate(PrivateKey authorityPrivateKey, PublicKey certPubKey, String commonName) throws SignatureException, InvalidKeyException {
        return newCertificate(authorityPrivateKey, certPubKey, new Random().nextLong(), commonName);
    }
}
