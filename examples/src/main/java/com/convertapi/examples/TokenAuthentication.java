package com.convertapi.examples;

import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;

import static java.lang.System.getenv;

/**
 * Most simple conversion example with token authentication
 */
public class TokenAuthentication {

    public static void main(String[] args) {
        Config.setDefaultToken(getenv("CONVERTAPI_TOKEN"));   // Generate your token: https://www.convertapi.com/doc/auth
        Config.setDefaultApiKey(getenv("CONVERTAPI_APIKEY"));  // Get your api key: https://www.convertapi.com/a
        String resourcePath = "files/test.docx";
        String tmpDir = System.getProperty("java.io.tmpdir") + "/";

        // Simplified file to file conversion example
        ConvertApi.convertFile(resourcePath, tmpDir + "/result.pdf");

        System.out.println("PDF file saved to: " + tmpDir + "result.pdf");
    }
}
