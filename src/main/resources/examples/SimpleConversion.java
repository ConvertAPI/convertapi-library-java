package com.convertapi.examples;

import com.convertapi.ConvertApi;

import static java.lang.System.getenv;

/**
 * Most simple conversion example
 */

public class SimpleConversion {
    public static void main(String[] args) {
        ConvertApi.convert("test-files/test.docx", "result.pdf", getenv("CONVERTAPI_SECRET"));
    }
}