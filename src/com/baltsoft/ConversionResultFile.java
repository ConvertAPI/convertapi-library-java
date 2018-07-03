package com.baltsoft;

import com.baltsoft.Model.ConversionResponse;
import com.baltsoft.Model.ConversionResponseFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConversionResultFile {
    private final CompletableFuture<ConversionResponse> responseFuture;
    private final int fileIndex;

    public ConversionResultFile(CompletableFuture<ConversionResponse> responseFuture, int fileIndex) {
        this.responseFuture = responseFuture;
        this.fileIndex = fileIndex;
    }

    public ConversionResponseFile fileInfo() throws ExecutionException, InterruptedException {
        return responseFuture.get().Files[fileIndex];
    }

    public CompletableFuture<InputStream> asStream() {
        return responseFuture.thenComposeAsync(r -> Http.get(r.Files[fileIndex].Url));
    }

    public CompletableFuture<Path> saveFile(Path path) {
        return asStream().thenApplyAsync(s -> {
            try {
                Path filePath = Files.isDirectory(path) ? Paths.get(path.toString(), fileInfo().FileName) : path;
                Files.copy(s, filePath, StandardCopyOption.REPLACE_EXISTING);
                return filePath;
            } catch (IOException | ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}