package com.example.detail_cert;

import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.MessageDigest;

public class CertificateInfo {
    public static void main(String[] args) {
        try {
            // file path
            String certificateFilePath = "./cert-files/chinhnd_issueCert.crt";

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            // Read cert from file
            FileInputStream fis = new FileInputStream(certificateFilePath);
            Certificate certificate = certificateFactory.generateCertificate(fis);
            fis.close();

            X509Certificate x509Certificate = (X509Certificate) certificate;

            // Get Issuer data
            String issuer = x509Certificate.getIssuerX500Principal().getName();

            // ValidFrom data
            String validFrom = x509Certificate.getNotBefore().toString();

            // ValidTo data
            String validTo = x509Certificate.getNotAfter().toString();

            // Thumbprint data
            byte[] thumbprintBytes = getThumbprint(x509Certificate);
            String thumbPrint = bytesToHex(thumbprintBytes);

            // Subject DN data
            String subjectDN = x509Certificate.getSubjectX500Principal().getName();

            System.out.println("Issuer: " + issuer);
            System.out.println("Valid From: " + validFrom);
            System.out.println("Valid To: " + validTo);
            System.out.println("Thumbprint: " + thumbPrint);
            System.out.println("Subject DN: " + subjectDN);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get Thumbprint From X.509 cert
    private static byte[] getThumbprint(X509Certificate certificate) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = certificate.getEncoded();
        md.update(der);
        return md.digest();
    }

    // Convert byte data to hex
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
}