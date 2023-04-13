package androidpdfviewer.com.danjdt.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidpdfviewer.com.danjdt.sample.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import com.danjdt.pdfviewer.PdfViewer
import com.danjdt.pdfviewer.interfaces.OnErrorListener
import com.danjdt.pdfviewer.interfaces.OnPageChangedListener
import com.danjdt.pdfviewer.utils.PdfPageQuality
import com.danjdt.pdfviewer.view.PdfViewerRecyclerView
import java.io.IOException
import java.lang.Exception

class SampleActivity : AppCompatActivity(), OnPageChangedListener , OnErrorListener{

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        PdfViewer.Builder(binding.rootView)
            .view(PdfViewerRecyclerView(this))
            .setMaxZoom(3f)
            .setZoomEnabled(true)
            .quality(PdfPageQuality.QUALITY_1080)
            .setOnErrorListener(this)
            .setOnPageChangedListener(this)
            .build()
            .load(R.raw.sample)
    }

    override fun onPageChanged(page: Int, total: Int) {
        binding.tvCounter.text = getString(R.string.pdf_page_counter, page, total)
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
