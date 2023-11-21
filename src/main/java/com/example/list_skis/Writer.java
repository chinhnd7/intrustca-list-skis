package com.example.list_skis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Writer {
    public List<String> go() {
        String websiteUrl = "https://rootca.gov.vn/crt/";
        String targetFolder = "./all-files-all-types";
        List<String> skis = new ArrayList<String>();

        File folder = new File(targetFolder);
        folder.mkdirs();

        try {
            Document doc = Jsoup.connect(websiteUrl).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String href = link.attr("abs:href");

                // Trên web có duy nhất 1 file crt, đấy là CA Root
                if (href.endsWith(".p7b") || href.endsWith(".crt")) {
                    String ski = downloadAndGetSKI(href, targetFolder);
                    skis.add(ski);
                }
            }

            System.out.println("Download and get completed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return skis;
    }

    private String downloadAndGetSKI(String fileUrl, String targetFolder) throws IOException {
        URL url = new URL(fileUrl);
        BufferedInputStream in = new BufferedInputStream(url.openStream());

        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        String filePath = targetFolder + File.separator + fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        byte[] dataBuffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }

        fileOutputStream.close();
        in.close();

        P7bKeyIdentifier p7bKeyIdentifier = new P7bKeyIdentifier();
        String ski = p7bKeyIdentifier.getSKI(filePath);
        return ski;
    }
}