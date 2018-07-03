package com.baltsoft;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        Config.setDefaultSecret("1234567890123456");
        ConversionResult rez = ConvertApi.convert("docx", "jpg", new Param[]{new Param("File", Paths.get("/home/jon/trinti/test.docx"))});
        System.out.println("LIAU");
        rez.saveFiles(Paths.get("/tmp"));
//
//        String s = ""; //f.get();
//        String a = s;
//        Thread.sleep(100000);


    }

}
