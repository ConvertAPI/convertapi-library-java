# ConvertAPI Java Client
## Convert your files with our online file conversion API

The ConvertAPI helps converting various file formats.
Creating PDF and Images from various sources like Word, Excel, Powerpoint, images, web pages or raw HTML codes.
Merge, Encrypt, Split, Repair and Decrypt PDF files.
And many others files manipulations.
In just few minutes you can integrate it into your application and use it easily.

The ConvertAPI-Java library makes it easier to use the Convert API from your Java 8 projects without having to build your own API calls.
You can get your free API token at https://www.convertapi.com/a/authentication

## Installation

### Maven

Add the following dependency to your pom.xml:
```xml
<dependency>
    <groupId>com.convertapi.client</groupId>
    <artifactId>convertapi</artifactId>
    <version>2.13.1</version>
</dependency>
```

## Usage

### Configuration

You can get your token at https://www.convertapi.com/a/authentication

```java
Config.setDefaultApiCredentials("api-token");
```

### File conversion

Example to convert DOCX file to PDF. All supported formats and options can be found
[here](https://www.convertapi.com/conversions).

```java
CompletableFuture<ConversionResult> result = ConvertApi.convert("docx", "pdf", new Param("file", Paths.get("test.docx")));

// save to file
result.get().saveFile(Paths.get("my_file.pdf")).get();
```

Other result operations:

```java
// save all result files to folder
result.get().saveFiles(Paths.get("/tmp"));

// get result file
ConversionResultFile file = result.get().getFile(0);

// get conversion cost
Integer cost = result.get().conversionCost();
```

#### Convert file url

```java
CompletableFuture<ConversionResult> result = ConvertApi.convert("pptx", "pdf",
    new Param("file", "https://cdn.convertapi.com/cara/testfiles/presentation.pptx")
);
```

#### Additional conversion parameters

ConvertAPI accepts extra conversion parameters depending on converted formats. All conversion 
parameters and explanations can be found [here](https://www.convertapi.com/conversions).

```java
CompletableFuture<ConversionResult> result = ConvertApi.convert("pdf", "jpg",
        new Param("file", Paths.get("test.pdf")),
        new Param("pagerange", "1"),
        new Param("imagewidth", 300),
        new Param("imageheight", 300),
        new Param("fittosize", "true")
);
```

### User information

You can always check the remaining conversions amount and other account information by fetching [user information](https://www.convertapi.com/doc/user).

```java
User user = ConvertApi.getUser();
int conversionsTotal = user.ConversionsTotal;
int conversionsConsumed = user.ConversionsConsumed;
```

### Alternative domain

Create `Config` instance with the alternative domain and provide it in `convert` method. Dedicated to the region [domain list](https://www.convertapi.com/doc/servers-location).

```java
Config config = new Config(credentials, "https", "eu-v2.convertapi.com", 0, httpClientBuilder);
```

### More examples

You can find more advanced examples in the [examples](https://github.com/ConvertAPI/convertapi-library-java/tree/main/examples/src/main/java/com/convertapi/examples) folder.

- [Advanced.java](https://github.com/ConvertAPI/convertapi-library-java/blob/main/examples/src/main/java/com/convertapi/examples/Advanced.java) - Configure the HTTP client (proxy, timeouts) and delete the files from the server after a conversion
- [ConversionChaining.java](https://github.com/ConvertAPI/convertapi-library-java/blob/main/examples/src/main/java/com/convertapi/examples/ConversionChaining.java) - Chain two conversions: DOCX to JPG images, then the images to a ZIP
- [ConvertRemoteFile.java](https://github.com/ConvertAPI/convertapi-library-java/blob/main/examples/src/main/java/com/convertapi/examples/ConvertRemoteFile.java) - Convert a remote file (a PPTX at a URL) to PDF
- [ConvertStream.java](https://github.com/ConvertAPI/convertapi-library-java/blob/main/examples/src/main/java/com/convertapi/examples/ConvertStream.java) - Convert a DOCX file passed as a stream to PDF
- [ConvertWebToPdf.java](https://github.com/ConvertAPI/convertapi-library-java/blob/main/examples/src/main/java/com/convertapi/examples/ConvertWebToPdf.java) - Convert a web page URL to PDF
- [ConvertWordToPdfAndPng.java](https://github.com/ConvertAPI/convertapi-library-java/blob/main/examples/src/main/java/com/convertapi/examples/ConvertWordToPdfAndPng.java) - Convert one Word DOCX file to PDF and to images in parallel
- [SimpleConversion.java](https://github.com/ConvertAPI/convertapi-library-java/blob/main/examples/src/main/java/com/convertapi/examples/SimpleConversion.java) - The simplest file-to-file conversion, DOCX to PDF
- [SplitAndMergePdf.java](https://github.com/ConvertAPI/convertapi-library-java/blob/main/examples/src/main/java/com/convertapi/examples/SplitAndMergePdf.java) - Split a PDF and merge its first and last pages into a new PDF
- [UserInformation.java](https://github.com/ConvertAPI/convertapi-library-java/blob/main/examples/src/main/java/com/convertapi/examples/UserInformation.java) - Retrieve your ConvertAPI account information

#### Converting your first file, full example:

ConvertAPI is designed to make converting file super easy, the following snippet shows how easy it is to get started. Let's convert WORD DOCX file to PDF:

```java
import com.convertapi.ConvertApi;

public class SimpleConversion {
    public static void main(String[] args) {
        ConvertApi.convert("source.docx", "result.pdf", "api-token");
    }
}
```

This is the bare-minimum to convert a file using the ConvertAPI client, but you can do a great deal more with the ConvertAPI Java library.
Take special note that you should replace `api-token` with the token you obtained in item two of the pre-requisites.

### Issues &amp; Comments
Please leave all comments, bugs, requests, and issues on the Issues page. We'll respond to your request ASAP!

### License
The ConvertAPI Java Library is licensed under the [MIT](http://www.opensource.org/licenses/mit-license.php "Read more about the MIT license form") license.
Refer to the [LICENSE](https://github.com/ConvertAPI/convertapi-library-java/blob/main/LICENSE.txt) file for more information.
