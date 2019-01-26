# pdfviewer
Simple Android PDF Viewer (initialy) displayed in a recycler view library using https://developer.android.com/reference/android/graphics/pdf/PdfRenderer 


## How to use

``` kotlin
    PdfViewer.Builder(rootView)
      .build()
      .load(file)
```
### All available options:
``` kotlin
    PdfViewer.Builder(rootView)
      .view(view)
      .quality(PdfPageQuality)
      .setZoomEnabled(true)
      .setMaxZoom(3f) //zoom multiplier
      .setOnPageChangedListener(onPageChangedListener)
      .setOnErrorListener(onErrorListener)
      .build()
      .load(file)
```

