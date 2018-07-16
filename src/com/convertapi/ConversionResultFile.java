package com.convertapi;

import com.convertapi.model.ConversionResponseFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

public class ConversionResultFile {
    private final ConversionResponseFile conversionResponseFile;

    public ConversionResultFile(ConversionResponseFile conversionResponseFile) {
        this.conversionResponseFile = conversionResponseFile;
    }

    @SuppressWarnings("WeakerAccess")
    public String getName() {
        return conversionResponseFile.FileName;
    }

    public int getSize() {
        return conversionResponseFile.FileSize;
    }

    @SuppressWarnings("WeakerAccess")
    public String getUrl() {
        return conversionResponseFile.Url;
    }

    @SuppressWarnings("WeakerAccess")
    public CompletableFuture<InputStream> asStream() {
        return Http.requestGet(getUrl());
    }

    @SuppressWarnings("WeakerAccess")
    public CompletableFuture<Path> saveFile(Path path) {
        return asStream().thenApplyAsync(s -> {
            try {
                Path filePath = Files.isDirectory(path) ? Paths.get(path.toString(), getName()) : path;
                Files.copy(s, filePath, StandardCopyOption.REPLACE_EXISTING);
                return filePath;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    public CompletableFuture delete() {
        return Http.requestDelete(getUrl());
    }
}