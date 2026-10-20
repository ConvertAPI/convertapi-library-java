package com.convertapi.client;

import com.convertapi.client.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.io.TempDir;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Converts real files through the API. Runs only when CONVERT_API_SECRET holds an API token, and calls
 * CONVERT_API_BASE_URI (CI uses the staging API, https://stag-v2.convertapi.com/), else the production API.
 * Without a token every test here is skipped.
 */
@EnabledIfEnvironmentVariable(named = "CONVERT_API_SECRET", matches = ".+")
class LiveApiTest {

    private static final String REMOTE_PPTX = "https://cdn.convertapi.com/cara/testfiles/presentation.pptx";

    private static Config config;
    private static Path docx;

    @BeforeAll
    static void setUp() throws Exception {
        String baseUri = System.getenv("CONVERT_API_BASE_URI");
        URI uri = URI.create(baseUri == null || baseUri.isEmpty() ? "https://v2.convertapi.com/" : baseUri);
        config = new Config(System.getenv("CONVERT_API_SECRET"), uri.getScheme(), uri.getHost(), 0, b -> b);
        docx = Paths.get(LiveApiTest.class.getResource("/test.docx").toURI());
    }

    private static void assertPdf(Path file) throws Exception {
        byte[] bytes = Files.readAllBytes(file);
        assertTrue(bytes.length > 1000, file + " is only " + bytes.length + " bytes");
        assertEquals("%PDF", new String(bytes, 0, 4, "US-ASCII"), file + " is not a PDF");
    }

    @Test
    void getsUser() {
        User user = ConvertApi.getUser(config);
        assertNotNull(user.Email);
        assertTrue(user.Active);
    }

    @Test
    void convertsFileToPdf(@TempDir Path dir) throws Exception {
        ConversionResult result = ConvertApi.convert("docx", "pdf", new Param[]{new Param("file", docx, config)}, config).get();
        assertEquals(1, result.fileCount());
        assertTrue(result.conversionCost() > 0);
        Path pdf = result.saveFile(dir.resolve("result.pdf")).get();
        assertPdf(pdf);
    }

    @Test
    void convertsStream(@TempDir Path dir) throws Exception {
        try (InputStream stream = Files.newInputStream(docx)) {
            ConversionResult result = ConvertApi.convert("docx", "pdf", new Param[]{new Param("file", stream, "test.docx", config)}, config).get();
            assertPdf(result.saveFile(dir).get());
        }
    }

    @Test
    void convertsFileByPathToFile(@TempDir Path dir) throws Exception {
        Path pdf = dir.resolve("out.pdf");
        ConvertApi.convertFile(docx.toString(), pdf.toString(), config);
        assertPdf(pdf);
    }

    @Test
    void convertsRemoteFile(@TempDir Path dir) throws Exception {
        Path pdf = ConvertApi.convertRemoteFile(REMOTE_PPTX, dir.resolve("presentation.pdf").toString(), config);
        assertPdf(pdf);
    }

    @Test
    void chainsConversions(@TempDir Path dir) throws Exception {
        CompletableFuture<ConversionResult> jpgs = ConvertApi.convert("docx", "jpg", new Param[]{new Param("file", docx, config)}, config);
        ConversionResult zip = ConvertApi.convert("jpg", "zip", new Param[]{new Param("files", jpgs)}, config).get();
        assertEquals(1, zip.fileCount());
        List<Path> saved = zip.saveFilesSync(dir);
        assertTrue(saved.get(0).toString().endsWith(".zip"), saved.toString());
        assertTrue(jpgs.get().fileCount() >= 1);
    }

    @Test
    void rejectsBadToken() {
        Config bad = new Config("not-a-token", config.getScheme(), config.getHost(), 0, b -> b);
        ExecutionException e = assertThrows(ExecutionException.class,
            () -> ConvertApi.convert("web", "pdf", new Param[]{new Param("url", "https://www.convertapi.com")}, bad).get());
        ConversionException cause = assertInstanceOf(ConversionException.class, e.getCause());
        assertEquals(401, cause.getHttpStatusCode());
    }
}
