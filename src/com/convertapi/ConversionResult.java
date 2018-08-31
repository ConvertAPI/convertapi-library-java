package com.convertapi;

import com.convertapi.model.ConversionResponse;
import com.convertapi.model.ConversionResponseFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class ConversionResult {
    private final ConversionResponse response;

    @SuppressWarnings("unused")
    public ConversionResult(ConversionResponse responseFuture) {
        this.response = responseFuture;
    }

    @SuppressWarnings("unused")
    public Integer fileCount() {
        return response.Files.length;
    }

    public List<String> urls() {
        @SuppressWarnings("unchecked") List<String> valueList = new ArrayList();
        for (ConversionResponseFile file: response.Files) valueList.add(file.Url);
        return valueList;
    }

    @SuppressWarnings("unused")
    public Integer conversionCost() {
        return response.ConversionCost;
    }

    public ConversionResultFile getFile(int index) {
        if (index < 0) index = response.Files.length + index;
        return new ConversionResultFile(response.Files[index]);
    }

    public CompletableFuture<InputStream> asStream() {
        return getFile(0).asStream();
    }

    public CompletableFuture<Path> saveFile(Path file) {
        return getFile(0).saveFile(file);
    }

    @SuppressWarnings("WeakerAccess")
    public List<CompletableFuture<Path>> saveFiles(Path directory) {
        if (!Files.isDirectory(directory)) throw new RuntimeException("Directory expected, but received: " + directory.toString());
        List<CompletableFuture<Path>> paths = new ArrayList<>();
        for (int i = 0; i < response.Files.length; i++) {
            paths.add(getFile(i).saveFile(directory));
        }
        return paths;
    }

    @SuppressWarnings("unused")
    public List<Path> saveFilesSync(Path directory) {
        List<CompletableFuture<Path>> futures = saveFiles(directory);
        return futures.stream().map(p -> {
            try {
                return p.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("WeakerAccess")
    public List<CompletableFuture> delete() {
        List<CompletableFuture> futures = new ArrayList<>();
        for (int i = 0; i < response.Files.length; i++) {
            futures.add(getFile(i).delete());
        }
        return futures;
    }

    @SuppressWarnings("WeakerAccess")
    public void deleteSync() {
        delete().forEach(d -> {
            try {
                d.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}