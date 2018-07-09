package com.convertapi;

import com.convertapi.Model.ConversionResponse;

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

    public CompletableFuture<Integer> fileCount() throws ExecutionException, InterruptedException {
        return responseFuture.thenApplyAsync(r -> r.Files.length);
    }

    public CompletableFuture<ConversionResultFile> getFile(int index) {
        return responseFuture.thenApplyAsync(r -> new ConversionResultFile(r.Files[index]));
    }

    public Path saveFile(Path file) throws ExecutionException, InterruptedException {
        return getFile(0).thenCompose(f -> f.saveFile(file)).get();
    }

    public List<Path> saveFiles(Path directory) throws ExecutionException, InterruptedException, NotDirectoryException {
        if (!Files.isDirectory(directory)) throw new NotDirectoryException(directory.toString());
        List<Path> paths = new ArrayList<Path>();
        for (int i = 0; i < responseFuture.get().Files.length; i++) {
            paths.add(getFile(i).thenCompose(f -> f.saveFile(directory)).get());
        }
        return paths;
    }
}