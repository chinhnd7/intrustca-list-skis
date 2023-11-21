package com.example.list_skis;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;


public class P7bKeyIdentifier {
    public String getSKI(String p7bFilePath) {

        try (FileInputStream fileInputStream = new FileInputStream(p7bFilePath)) {
            // Đọc dữ liệu từ file .p7b và chuyển đổi sang dạng byte array
            byte[] p7bData = fileInputStream.readAllBytes();

            // Tạo CertificateFactory và đọc các chứng thư số từ dữ liệu .p7b
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection<Certificate> certificates = (Collection<Certificate>) certificateFactory.generateCertificates(new ByteArrayInputStream(p7bData));

            // Lặp qua từng Cert
            for (Certificate certificate : certificates) {
                if (certificate instanceof X509Certificate) {
                    X509Certificate x509Certificate = (X509Certificate) certificate;

                    byte[] skiExtensionValue = x509Certificate.getExtensionValue("2.5.29.14"); // OID for Subject Key Identifier

                    if (skiExtensionValue != null) {
                        // Lấy giá trị của SKI extension value
                        // Đây là một mảng byte, có thể xử lý nó tùy ý.
                        String result = toHexString(skiExtensionValue);
                        return result;
                    } else {
                        System.out.println("Không tìm thấy Subject Key Identifier (SKI) trong chứng thư số.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X", b));
        }
        // Cắt 8 ký tự đầu, hic có lỗi là cút, sợ
        sb.delete(0, 8);
        return sb.toString();
    }
}