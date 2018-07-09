package com.convertapi;

import com.convertapi.model.ConversionResponse;
import com.convertapi.model.ConversionResponseFile;

import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConversionResult {
    private final ConversionResponse response;

    public ConversionResult(ConversionResponse responseFuture) {
        this.response = responseFuture;
    }

    public Integer fileCount() throws ExecutionException, InterruptedException {
        return response.Files.length;
    }

    public List<String> urls() {
        List<String> valueList = new ArrayList();
        for (ConversionResponseFile file: response.Files) valueList.add(file.Url);
        return valueList;
    }

    public Integer conversionCost() throws ExecutionException, InterruptedException {
        return response.ConversionCost;
    }

    public ConversionResultFile getFile(int index) {
        return new ConversionResultFile(response.Files[index]);
    }

    public CompletableFuture<Path> saveFile(Path file)  {
        return getFile(0).saveFile(file);
    }

    public List<CompletableFuture<Path>> saveFiles(Path directory) {
        if (!Files.isDirectory(directory)) throw new RuntimeException("Directory expected, but received: " + directory.toString());
        List<CompletableFuture<Path>> paths = new ArrayList();
        for (int i = 0; i < response.Files.length; i++) {
            paths.add(getFile(i).saveFile(directory));
        }
        return paths;
    }
}