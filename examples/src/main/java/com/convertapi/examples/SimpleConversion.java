package com.convertapi.examples;

import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;

import java.io.File;
import java.io.IOException;
import static java.lang.System.getenv;

/**
 * Most simple conversion example
 */
public class SimpleConversion {
    public static void main(String[] args) throws IOException {
        Config.setDefaultSecret(getenv("CONVERTAPI_SECRET"));
        String resourcePath = new File(AlternativeConverter.class.getClassLoader().getResource("test.docx").getFile()).getCanonicalPath();
        String tmpDir = System.getProperty("java.io.tmpdir") + "/";

        // Simplified file to file conversion example
        ConvertApi.convert(resourcePath, tmpDir + "/result.pdf");
    }
}
