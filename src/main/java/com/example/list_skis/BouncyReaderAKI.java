package com.example.list_skis;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BouncyReaderAKI {
    public static void main(String[] args) {
        String pdfFilePath = "./pdf-files/contract-ihd.pdf";

        try (PDDocument document = PDDocument.load(new java.io.File(pdfFilePath))) {
            List<PDSignature> signatures = document.getSignatureDictionaries();
            for (PDSignature signature : signatures) {
                String aki = getAKIFromSignatureContents(signature);
                System.out.println("Authority Key Identifier (AKI): " + aki);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAKIFromSignatureContents(PDSignature signature) {
        try {
            // Lấy nội dung của chữ ký số PKCS#7
            byte[] signatureBytes = signature.getContents();
            CMSSignedData cmsSignedData = new CMSSignedData(signatureBytes);

            // Lấy thông tin chữ ký số và signer information
            SignerInformationStore signerInformationStore = cmsSignedData.getSignerInfos();
            Collection<SignerInformation> signers = signerInformationStore.getSigners();
            for (SignerInformation signer : signers) {
                // Lấy chứng chỉ của signer
                Collection<X509CertificateHolder> certificateHolders = cmsSignedData.getCertificates().getMatches(signer.getSID());
                if (!certificateHolders.isEmpty()) {
                    Iterator<X509CertificateHolder> iterator = certificateHolders.iterator();
                    X509CertificateHolder certificateHolder = iterator.next();
                    X509Certificate certificate = new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider()).getCertificate(certificateHolder);

                    byte[] akiExtensionValue = certificate.getExtensionValue("2.5.29.35"); // OID for Authority Key Identifier
                    if (akiExtensionValue != null) {
                        ASN1InputStream asn1InputStream = new ASN1InputStream(akiExtensionValue);
                        DEROctetString akiOctetString = (DEROctetString) asn1InputStream.readObject();
                        byte[] akiValue = akiOctetString.getOctets();

                        // Chuyển đổi AKI từ mảng byte sang chuỗi hexa và trả về
                        return toHexString(akiValue);
                    } else {
                        System.out.println("Không tìm thấy Authority Key Identifier (AKI) trong chứng thư số.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X", b));
        }
        sb.delete(0, 8);
        return sb.toString();
    }
}