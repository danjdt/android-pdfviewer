package androidpdfviewer.com.danjdt.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danjdt.pdfviewer.PdfViewer
import com.danjdt.pdfviewer.interfaces.OnErrorListener
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.utils.PdfPageQuality
import com.danjdt.pdfviewer.view.PdfViewerRecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.lang.Exception

class SampleActivity : AppCompatActivity(), OnPageChangedListener , OnErrorListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PdfViewer.Builder(rootView)
            .view(PdfViewerRecyclerView(this))
            .setMaxZoom(3f)
            .setZoomEnabled(true)
<<<<<<< Updated upstream
            .quality(PdfPageQuality.QUALITY_AUTO)
=======
            .quality(PdfPageQuality.QUALITY_1080)
>>>>>>> Stashed changes
            .setOnErrorListener(this)
            .setOnPageChangedListener(this)
            .build()
            .load(R.raw.sample)
    }

<<<<<<< Updated upstream
    override fun onPageChanged(page: Int) {

=======
    override fun onPageChanged(page: Int, total: Int) {
        tvCounter.text = getString(R.string.pdf_page_counter, page, total)
>>>>>>> Stashed changes
    }

    override fun onFileLoadError(e: Exception) {
        //Handle error ...
        e.printStackTrace()
    }

    override fun onAttachViewError(e: Exception) {
        //Handle error ...
        e.printStackTrace()
    }

    override fun onPdfRendererError(e: IOException) {
        //Handle error ...
        e.printStackTrace()
    }
}
