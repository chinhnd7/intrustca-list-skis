package com.example.detail_cert;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.MessageDigest;
import java.util.Base64;

public class CertificateInfo {
    public static void main(String[] args) {
        try {
//            // file path
//            String certificateFilePath = "./cert-files/chinhnd_issueCert.crt";
//
//            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//
//            // Read cert from file
//            FileInputStream fis = new FileInputStream(certificateFilePath);
//            Certificate certificate = certificateFactory.generateCertificate(fis);
//            fis.close();
//
//            X509Certificate x509Certificate = (X509Certificate) certificate;

            // Certificate data in base64 string
            String base64CertificateData = "MIIDmTCCAoGgAwIBAgIUaph30Kxq8Ch8d+hGuJYCiSXX+AowDQYJKoZIhvcNAQELBQAwGTEXMBUGA1UEAwwOSW50cnVzdENBIFRlc3QwHhcNMjMxMTA5MDM1ODM5WhcNMjUxMTA4MDM1ODM4WjBcMQswCQYDVQQGEwJWTjEUMBIGA1UEChMLSW50cnVzdG51bGwxGDAWBgNVBAMMD1BoYW4gVsSDbiBExaluZzEdMBsGCgmSJomT8ixkAQETDTEyMzQ1Njc4OTEyMzQwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCS98GRA5lYCSKIpefic6q7kc2XXNSkAJ9wQergFlSaA5odXV7cjPZrx+UoQG8D4gauS9s4e2NVIEIO0fF03g8VjGi0oFXMj95QG0BaKANfrojG8RdKVCujYayKXIGW++mJUkpsT83Ss7KaFoIxkCe+Ju44Jed0PCyUUKToVj3jvIneIVlek7oD1OAtlwiMEnw1A1a1VvNbZU7OvXBnxHn2N6MU0YhaIOyqi101zkyxFnPAYUtdO96+Fqei3nphWP2MDv9brAAhl1fGfaMNV6+YEEi/LBaQtcnXtCfwwDA1QgItgIAB8ZPBzYLmoFoa2mk9HhDdxYJioh2j412mThq1AgMBAAGjgZUwgZIwDAYDVR0TAQH/BAIwADAfBgNVHSMEGDAWgBQfIxoYSfq+Y7KWsAEp2k7EocjiDTAyBgNVHSUEKzApBggrBgEFBQcDAgYIKwYBBQUHAwQGCSqGSIb3LwEBBQYIKwYBBQUHAwEwHQYDVR0OBBYEFIwsZRmOwNYIDiDXKyv5qP9/VWqTMA4GA1UdDwEB/wQEAwIF4DANBgkqhkiG9w0BAQsFAAOCAQEAkt1voO+Z0UYzv3Jgf+lp0j3kNFSlyRJNTuWcuCvr5bwWeEgA6ZL74ScgOCux2qSybH8T4bgbW7YKdwnahbrPvZfbkNcl0MFzHDOT/KF/63jw63RHuUquJWunMGecjR68in1C2AUH/HZlroEXlVnQz9RX0pWb+Jj72QK0jTR78bWkb3KZRB+sICUs+eWjD4jQgtDeHTF/hUwuPs/TFvyi+SPNX+7hTxdBG0KSlHEGwWqMPG4cTk3QJQf0Cj1V9F0wTPxDEYdTP9VrnyW6yXHd0JtohAPPkX+EvOn0LHdrjgk51KYstuz+RtnZ/INSXt7HpecHXftVqpsfxQsBBuJJBg==";

            // Decode base64 string to byte array
            byte[] certificateBytes = Base64.getDecoder().decode(base64CertificateData);

            // Create a CertificateFactory for X.509
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            // Read cert from byte array
            ByteArrayInputStream bis = new ByteArrayInputStream(certificateBytes);
            Certificate certificate = certificateFactory.generateCertificate(bis);
            bis.close();

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