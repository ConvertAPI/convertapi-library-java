#### 1. Install the ConvertAPI library

Go to [ConvertAPI Java client](https://github.com/ConvertAPI/convertapi-java) page and download JAR file.
Place JAR in to your project library directory.


#### 2.a. Simple conversion methods

```java
import com.convertapi.*;

Config.setDefaultSecret("your-api-secret");

// Simplified file to file conversion example
ConvertApi.convertFile("test.docx", "/tmp/result.pdf");

// Simplified web site to pdf conversion example
ConvertApi.convertUrl("http://example.com", "/tmp/example.pdf");

// Simplified remote file to local file conversion example
ConvertApi.convertRemoteFile("https://cdn.convertapi.com/cara/testfiles/document.docx", "/tmp/demo.pdf");
```


#### 2.b. Convert local file

```java
import com.convertapi.*;

Config.setDefaultSecret("your-api-secret");

//Set input and output formats and pass file parameter. 
//Word to PDF API. Read more https://www.convertapi.com/docx-to-pdf
ConvertApi.convert("docx", "pdf",
    new Param("file", Paths.get("test.docx"))
).get().saveFile(Paths.get("/tmp")).get();
```


#### 2.c. Convert remote file and set additional parameters

```java
import com.convertapi.*;

Config.setDefaultSecret("your-api-secret");

//Set input and output formats and pass file parameter. 
//PowerPoint to PNG API. Read more https://www.convertapi.com/pptx-to-png
ConvertApi.convert("pdf", "png",
        new Param("file", "https://cdn.convertapi.com/cara/testfiles/presentation.pptx"),
        new Param("scaleimage", "true"),
        new Param("scaleproportions", "true"),
        new Param("imageheight", 300)
).get().saveFile(Paths.get("/tmp")).get();
```


#### 2.d. Convert from a stream

```java
import com.convertapi.*;

Config.setDefaultSecret("your-api-secret");

// Create stream from HTML string
String streamContent = "<!DOCTYPE html><html><body><h1>My First Heading</h1><p>My first paragraph.</p></body></html>";
InputStream stream = new ByteArrayInputStream(streamContent.getBytes());

// Html to PDF API. Read more https://www.convertapi.com/html-to-pdf
CompletableFuture<ConversionResult> result = ConvertApi.convert("html", "pdf",
        new Param("file", stream, "test.html")
);

// PDF as stream
InputStream resultStream = result.get().asStream().get();
```


#### 2.e. Conversions chaining

```java
// Split PDF document and merge first and last pages to new PDF
import com.convertapi.*;

Config.setDefaultSecret("your-api-secret");

// Set input and output formats and pass file parameter. 
// Split PDF API. Read more https://www.convertapi.com/pdf-to-split
CompletableFuture<ConversionResult> splitResult = ConvertApi.convert("pdf", "split",
        new Param("file", Paths.get("test.pdf"))
);

// Get result of the first conversion and use it in Merge conversion.
// Chains are executed on server without moving files.
// Merge PDF API. Read more https://www.convertapi.com/pdf-to-merge
CompletableFuture<ConversionResult> mergeResult = ConvertApi.convert("pdf", "merge",
        new Param("files", splitResult, 0),
        new Param("files", splitResult, -1)
);

mergeResult.get().saveFile(Paths.get("/tmp")).get();
```


#### 3. Read account status

```java
import com.convertapi.*;
import com.convertapi.model.*;

Config.setDefaultSecret("your-api-secret");

// Read full account data
User user = ConvertApi.getUser();

// Find out how much seconds left
int secondsLeft = user.SecondsLeft;
```


#### 4. Exception handling (asynchronous)

```java
import com.convertapi.*;

Config.setDefaultSecret("your-api-secret");

// Creating an exception
CompletableFuture<ConversionResult> resultFuture = ConvertApi.convert("pdf", "pptx",
        new Param("file", Paths.get("test-files/test.pdf")),
        new Param("AutoRotate","WrongParameter")
).exceptionally(t -> {
    // Handling and exception
    System.out.println("Error message: " + t.getMessage());
    return null;
});

ConversionResult result = resultFuture.get();
// Saving file if there was no exception 
if (result != null) {
    result.saveFile(Paths.get("/tmp")).get();
}
```


#### 5. Supported file formats, conversions and actions

https://www.convertapi.com/doc/supported-formats

#### 6. GitHub 

https://github.com/ConvertAPI/convertapi-java