# ConvertAPI Java Client
## Convert your files with our online file conversion API

The ConvertAPI helps converting various file formats.
Creating PDF and Images from various sources like Word, Excel, Powerpoint, images, web pages or raw HTML codes.
Merge, Encrypt, Split, Repair and Decrypt PDF files.
And many others files manipulations.
In just few minutes you can integrate it into your application and use it easily.

The ConvertAPI-Java library makes it easier to use the Convert API from your Java 8 projects without having to build your own API calls.
You can get your free API credentials at https://www.convertapi.com/a

## Installation

### Maven

Add the following dependency to your pom.xml:
```xml
<dependency>
    <groupId>com.convertapi.client</groupId>
    <artifactId>convertapi</artifactId>
    <version>2.11</version>
</dependency>
```

## Usage

### Configuration

You can get your credentials at https://www.convertapi.com/a

```java
Config.setDefaultApiCredentials("your-api-credentials");
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
        new Param("scaleimage", "true"),
        new Param("scaleproportions", "true"),
        new Param("imageheight", 300)
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

You can find more advanced examples in the [examples](https://github.com/ConvertAPI/convertapi-java/tree/master/examples/src/main/java/com/convertapi/examples) folder.

#### Converting your first file, full example:

ConvertAPI is designed to make converting file super easy, the following snippet shows how easy it is to get started. Let's convert WORD DOCX file to PDF:

```java
import com.convertapi.ConvertApi;

public class SimpleConversion {
    public static void main(String[] args) {
        ConvertApi.convert("source.docx", "result.pdf", "your-api-credentials");
    }
}
```

This is the bare-minimum to convert a file using the ConvertAPI client, but you can do a great deal more with the ConvertAPI Java library.
Take special note that you should replace `your-api-credentials` with the credentials you obtained in item two of the pre-requisites.

### Issues &amp; Comments
Please leave all comments, bugs, requests, and issues on the Issues page. We'll respond to your request ASAP!

### License
The ConvertAPI Java Library is licensed under the [MIT](http://www.opensource.org/licenses/mit-license.php "Read more about the MIT license form") license.
Refer to the [LICENSE](https://raw.githubusercontent.com/ConvertAPI/convertapi-java/master/LICENSE.txt) file for more information.
