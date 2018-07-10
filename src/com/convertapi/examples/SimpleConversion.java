package com.convertapi.examples;

import com.convertapi.ConvertApi;

/**
 * Most simple conversion example
 */

public class SimpleConversion {
    public static void main(String[] args) {
        ConvertApi.convert("test-files/test.docx", "result.pdf", "YOUR API SECRET");
    }
}