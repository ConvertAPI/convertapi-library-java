package com.convertapi.client;

import com.convertapi.client.model.ConversionResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests that need no network and no API token.
 */
class OfflineTest {

    private static final String RESPONSE = "{\"ConversionCost\":2,\"Files\":["
        + "{\"FileName\":\"a.jpg\",\"FileSize\":10,\"Url\":\"https://v2.convertapi.com/d/1/a.jpg\"},"
        + "{\"FileName\":\"b.jpg\",\"FileSize\":20,\"Url\":\"https://v2.convertapi.com/d/2/b.jpg\"}]}";

    private static ConversionResult result() {
        return new ConversionResult(new Gson().fromJson(RESPONSE, ConversionResponse.class));
    }

    @Test
    void defaultConfigTargetsProductionApi() {
        Config.setDefaultApiCredentials("token");
        Config config = Config.defaults();
        assertEquals("https", config.getScheme());
        assertEquals("v2.convertapi.com", config.getHost());
        assertEquals("token", config.getApiCredentials());
        assertEquals("other", Config.defaults("other").getApiCredentials());
    }

    @Test
    void paramNamesAreLowerCaseAndValuesAreStrings() throws Exception {
        assertEquals("pagerange", new Param("PageRange", "1-2").getName());
        assertEquals(Collections.singletonList("300"), new Param("ImageHeight", 300).getValue());
        assertEquals(Collections.singletonList("1.5"), new Param("Scale", new BigDecimal("1.5")).getValue());
    }

    @Test
    void paramsConcatenateInOrder() {
        Param a = new Param("a", "1");
        Param b = new Param("b", "2");
        Param c = new Param("c", "3");
        assertArrayEquals(new Param[]{a, b, c}, Param.concat(new Param[]{a}, new Param[]{b, c}));
    }

    @Test
    void conversionResultExposesFiles() {
        ConversionResult result = result();
        assertEquals(2, result.fileCount());
        assertEquals(2, result.conversionCost());
        assertEquals(Arrays.asList("https://v2.convertapi.com/d/1/a.jpg", "https://v2.convertapi.com/d/2/b.jpg"), result.urls());
        assertEquals("a.jpg", result.getFile(0).getName());
        assertEquals(20, result.getFile(1).getSize());
        // A negative index counts from the end.
        assertEquals("b.jpg", result.getFile(-1).getName());
    }

    @Test
    void conversionResultBecomesParamValues() throws Exception {
        assertEquals(result().urls(), new Param("files", result()).getValue());
        assertEquals(Collections.singletonList("https://v2.convertapi.com/d/2/b.jpg"), new Param("file", result(), 1).getValue());
        assertEquals(result().urls(), new Param("files", CompletableFuture.completedFuture(result())).getValue());
    }

    @Test
    void conversionExceptionKeepsStatusCode() {
        ConversionException e = new ConversionException("{\"Code\":4013}", 401);
        assertEquals(401, e.getHttpStatusCode());
        assertEquals("{\"Code\":4013}", e.getMessage());
    }
}
