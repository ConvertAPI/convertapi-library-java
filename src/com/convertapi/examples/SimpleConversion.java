package com.convertapi.examples;

import com.convertapi.Config;
import com.convertapi.ConvertApi;

import static java.lang.System.getenv;

/**
 * Most simple conversion example
 */

public class SimpleConversion {
    public static void main(String[] args) {
        Config.setDefaultSecret(getenv("CONVERTAPI_SECRET"));    //Get your secret at https://www.convertapi.com/a

        // Simplified file to file conversion example
        ConvertApi.convertFile("test-files/test.docx", "/tmp/result.pdf");

        // Simplified file to multiple files conversion example
        ConvertApi.convertFileToDir("test-files/test.docx", "jpg", "/tmp");

        // Simplified web site to pdf conversion example
        ConvertApi.convertUrl("http://example.com", "/tmp/example.pdf");

        // Simplified remote file to local file conversion example
        ConvertApi.convertRemoteFile("https://calibre-ebook.com/downloads/demos/demo.docx", "/tmp/demo.pdf");

        // Simplified remote file to local file conversion example
        ConvertApi.convertRemoteFileToDir("http://www.pdf995.com/samples/pdf.pdf", "jpg", "/tmp");
    }
}