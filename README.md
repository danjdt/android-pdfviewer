# pdfviewer
Simple Android PDF Viewer (initialy) displayed in a recycler view library using https://developer.android.com/reference/android/graphics/pdf/PdfRenderer 


## How to use
``` kotlin

    PdfViewer.Builder(rootView)
      .view(view)
      .quality(PdfPageQuality)
      .setMaxZoom(3f)
      .setZoomEnabled(true)
      .setOnPageChangedListener(onPageChangedListener)
      .setOnErrorListener(onErrorListener)
      .build()
      .load(file)
```

