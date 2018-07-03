package com.baltsoft;

import com.baltsoft.Model.ConversionResponse;

import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConversionResult {
    private final CompletableFuture<ConversionResponse> responseFuture;

    public ConversionResult(CompletableFuture<ConversionResponse> responseFuture) {
        this.responseFuture = responseFuture;
    }

    public CompletableFuture<ConversionResponse> getResponseFuture() {
        return responseFuture;
    }

    public int fileCount() throws ExecutionException, InterruptedException {
        return responseFuture.get().Files.length;
    }

    public ConversionResultFile file(int index) {
        return new ConversionResultFile(responseFuture, index);
    }

    public Path saveFile(Path file) throws ExecutionException, InterruptedException {
        return new ConversionResultFile(responseFuture, 0).saveFile(file).get();
    }

    public List<Path> saveFiles(Path directory) throws ExecutionException, InterruptedException, NotDirectoryException {
        if (!Files.isDirectory(directory)) throw new NotDirectoryException(directory.toString());
        List<Path> paths = new ArrayList<Path>();
        for (int i = 0; i < responseFuture.get().Files.length; i++) {
            paths.add(new ConversionResultFile(responseFuture, i).saveFile(directory).get());
        }
        return paths;
    }
}