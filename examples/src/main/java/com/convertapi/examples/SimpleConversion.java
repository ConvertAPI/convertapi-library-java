package com.convertapi.examples;

import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;

import static java.lang.System.getenv;

/**
 * Most simple conversion example
 */
public class SimpleConversion {

    public static void main(String[] args) {
        Config.setDefaultApiCredentials(getenv("CONVERTAPI_CREDENTIALS"));   //Get your api credentials at https://www.convertapi.com/a
        String resourcePath = "files/test.docx";
        String tmpDir = System.getProperty("java.io.tmpdir") + "/";

        // Simplified file to file conversion example
        ConvertApi.convertFile(resourcePath, tmpDir + "/result.pdf");

        System.out.println("PDF file saved to: " + tmpDir + "result.pdf");
    }
}
