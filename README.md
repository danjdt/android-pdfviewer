# Android PDFViewer
A Android PDF Viewer that render pdf using `PdfRenderer` + `kotlin-coroutines` and displays it in a `RecyclerView`.

**- This code will not be published as a library, but you can use it as you want in your code.**

**- Requires API Level 21**

## How to use

``` kotlin
    PdfViewer.Builder(rootView)
      .build()
      .load(file)
```

### Setup options
``` kotlin
    PdfViewer.Builder(rootView)
      .view(view)
      .quality(PdfPageQuality)
      .setZoomEnabled(true)
      .setMaxZoom(3f)
      .setOnPageChangedListener(onPageChangedListener)
      .setOnErrorListener(onErrorListener)
      .build()
      .load(file)
```

### File load options 
``` kotlin
    pdfViewer.load(file : File)
    pdfViewer.load(url : String)
    pdfViewer.load(@RawRes resId : Int)
    pdfViewer.load(inputStream : InputStream)
    pdfViewer.load(uri : Uri)
```

### Sample gifs/images

|     Scroll    |     Zoom      |
| ------------- | ------------- |
| ![](gif1.gif) |![](git2.gif)  |

