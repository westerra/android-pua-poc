package com.westerra.release.btp.fragments

import android.annotation.SuppressLint
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.westerra.release.R
import com.westerra.release.analytics.AnalyticsScreenNames.BTP_ACCOUNT_AGREEMENT_PDF
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.btp.viewmodels.DownloadFileViewModel
import com.westerra.release.btp.viewmodels.DownloadFileViewModelFactory
import com.westerra.release.constants.Constants.TEXT_PLAIN
import com.westerra.release.databinding.FragmentBtpPdfViewerBinding
import com.westerra.release.extensions.closeSafe
import com.westerra.release.extensions.fadeIn
import com.westerra.release.extensions.fadeInvisible
import com.westerra.release.extensions.guessFileName
import com.westerra.release.extensions.renderPdfBitmap
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.max

/*
    Pdf Renderer based off of Google code samples:
    https://github.com/android/graphics-samples/tree/main/PdfRendererBasic
 */
class BtpPdfViewerFragment : Fragment() {
    companion object {
        private val TAG = BtpPdfViewerFragment::class.java.simpleName
        const val PDF_VIEWER_URL_KEY = "pdf_viewer_url_key"
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentBtpPdfViewerBinding? = null
    val binding get() = _binding!!

    private var url: String? = null

    private val downloadFileViewModel: DownloadFileViewModel by viewModels {
        DownloadFileViewModelFactory()
    }

    private var pdfFileDescriptor: ParcelFileDescriptor? = null
    private var pdfRenderer: PdfRenderer? = null
    private var currentPdfPage: PdfRenderer.Page? = null
    private var currentPdfPageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
    }

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordBtpScreenView(screenName = BTP_ACCOUNT_AGREEMENT_PDF)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBtpPdfViewerBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            goBack()
        }
        binding.shareClicker.setOnClickListener {
            shareLink()
        }
        binding.lastPageClicker.setOnClickListener {
            showLastPdfPage()
        }
        binding.nextPageClicker.setOnClickListener {
            showNextPdfPage()
        }
        binding.retryButton.setOnClickListener {
            binding.retryButton.isEnabled = false
            loadPdfFromUrl()
        }
        downloadFileViewModel.responseFile.observe(viewLifecycleOwner) { responseHolder ->
            if (responseHolder.isSuccess()) {
                responseHolder.data?.let { file ->
                    renderPdf(file = file)
                }
            } else {
                Log.i(
                    TAG,
                    "Download pdf file attempt failed: ${responseHolder.getErrorMessage()}",
                )
                renderPdfError()
            }
        }
        arguments?.getString(PDF_VIEWER_URL_KEY, null) ?. let {
            url = it
            loadPdfFromUrl()
        }
        return binding.root
    }

    private fun renderLoading() {
        hidePdfComponents()
        binding.progressText.text = getString(R.string.loading_document)
        binding.progressSpinner.fadeIn()
        binding.progressContainer.fadeIn()
    }

    private fun loadPdfFromUrl() {
        url ?. let {
            renderLoading()
            makePdfDownloadAttempt(it)
        }
    }

    private fun shareLink() {
        url ?. let { urlLink ->
            context ?. let { context ->
                ShareCompat.IntentBuilder(context)
                    .setType(TEXT_PLAIN)
                    .setChooserTitle(context.getString(R.string.share_url))
                    .setText(urlLink)
                    .startChooser()
            }
        }
    }

    private fun makePdfDownloadAttempt(url: String) {
        val fileName = Uri.parse(url).guessFileName()
        if (fileName.isNullOrBlank()) {
            renderPdfError()
            return
        }
        context ?. let { context ->
            val file = File(context.filesDir, fileName)
            if (file.exists() && file.isFile) {
                renderPdf(file = file)
            } else {
                downloadFileViewModel.downloadFile(url = url, file = file)
            }
        }
    }

    private fun renderPdf(file: File) {
        if (openPdfRenderer(file = file)) {
            renderPdfPage(pageIndex = currentPdfPageIndex)
        } else {
            renderPdfError()
        }
    }

    private fun openPdfRenderer(file: File): Boolean {
        try {
            if (!file.exists()) {
                Log.i(TAG, "Could not render pdf. File does not exist: ${file.name}")
                return false
            }
            closeRenderer()
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY) ?. let {
                pdfRenderer = PdfRenderer(it)
            }
        } catch (e: FileNotFoundException) {
            Log.i(TAG, "FileNotFoundException rendering pdf file: ${file.name}")
            return false
        } catch (e: IOException) {
            Log.i(TAG, "IOException rendering pdf file: ${file.name}")
            return false
        }
        binding.toolbarTitleText.text = file.toUri().guessFileName() ?: ""
        return true
    }

    private fun goBack() {
        binding.shareClicker.isEnabled = false
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun closeRenderer() {
        currentPdfPage?.closeSafe()
        pdfRenderer?.closeSafe()
        pdfFileDescriptor?.closeSafe()
    }

    private fun isOnLastPdfPage(): Boolean {
        val renderer = pdfRenderer ?: return true
        return (currentPdfPageIndex + 1 >= renderer.pageCount)
    }

    private fun showNextPdfPage() {
        if (isOnLastPdfPage()) {
            return
        }
        currentPdfPageIndex += 1
        renderPdfPage(currentPdfPageIndex)
    }

    private fun showLastPdfPage() {
        if (currentPdfPageIndex <= 0) return
        currentPdfPageIndex -= 1
        renderPdfPage(currentPdfPageIndex)
    }

    private fun renderPdfPage(pageIndex: Int) {
        val renderer = pdfRenderer
        if (renderer == null || pageIndex >= renderer.pageCount) {
            renderPdfError()
            return
        }
        if (pageIndex <= 0) {
            binding.lastPageClicker.isEnabled = false
            binding.lastPageImage.alpha = 0.45f
        } else {
            binding.lastPageClicker.isEnabled = true
            binding.lastPageImage.alpha = 1f
        }
        if (pageIndex >= renderer.pageCount - 1) {
            binding.nextPageClicker.isEnabled = false
            binding.nextPageImage.alpha = 0.45f
        } else {
            binding.nextPageClicker.isEnabled = true
            binding.nextPageImage.alpha = 1f
        }
        context ?. let { context ->
            binding.pageCounterText.text = context.getString(
                R.string.page_counter_formatted,
                currentPdfPageIndex + 1,
                renderer.pageCount,
            )
        }
        currentPdfPage?.closeSafe()
        currentPdfPage = renderer.openPage(pageIndex)
        currentPdfPage ?. let { page ->
            renderPdfPageWhenViewHasSize(page = page, limit = 200)
        }
    }

    private fun renderPdfPageWhenViewHasSize(page: PdfRenderer.Page, limit: Int) {
        if (limit < 0) {
            Log.e(TAG, "Unexpected, no pdf layout size detected within limit.")
            renderPdfError()
            return
        }
        if (binding.pdfViewerDisplayLayout.width == 0 ||
            binding.pdfViewerDisplayLayout.height == 0
        ) {
            binding.pdfViewerDisplayLayout.postDelayed({
                renderPdfPageWhenViewHasSize(page = page, limit = limit - 1)
            }, 5)
        } else {
            renderPdfPage(page = page)
        }
    }

    private fun renderPdfPage(page: PdfRenderer.Page) {
        val pdfSizeRatio = page.width.toFloat() / page.height.toFloat()
        val maxWidth = max(
            binding.pdfViewerDisplayLayout.width,
            binding.pdfViewerDisplayLayout.measuredWidth,
        )
        // Note: May need to subtract padding from maxWidth
        val maxHeight = max(
            binding.pdfViewerDisplayLayout.height,
            binding.pdfViewerDisplayLayout.measuredHeight,
        )
        val maxRatio = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight
        if (maxRatio > pdfSizeRatio) {
            finalWidth = (maxHeight.toFloat() * pdfSizeRatio).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / pdfSizeRatio).toInt()
        }
        renderPdfBitmapHelper(page = page, width = finalWidth, height = finalHeight)
    }

    private fun renderPdfBitmapHelper(page: PdfRenderer.Page, width: Int, height: Int) {
        val bitmap = page.renderPdfBitmap(width, height)
        if (bitmap == null) {
            renderPdfError()
        } else {
            binding.pdfViewerDisplayImage.setImageBitmap(bitmap)
            showPdfComponents()
        }
    }

    private fun showPdfComponents() {
        binding.progressContainer.fadeInvisible()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.pdfViewerDisplayLayout.fadeIn()
                binding.pageCounterText.fadeIn()
                binding.lastPageClicker.fadeIn()
                binding.lastPageClicker.isEnabled = true
                binding.nextPageClicker.fadeIn()
                binding.nextPageClicker.isEnabled = true
            },
            500,
        )
    }

    private fun renderPdfError() {
        hidePdfComponents()
        binding.progressSpinner.fadeInvisible()
        binding.progressText.text = getString(R.string.network_failure_message)
        binding.retryButton.isEnabled = true
        binding.retryButton.fadeIn()
    }

    private fun hidePdfComponents() {
        binding.pdfViewerDisplayLayout.fadeInvisible()
        binding.pageCounterText.fadeInvisible()
        binding.lastPageClicker.fadeInvisible()
        binding.lastPageClicker.isEnabled = false
        binding.nextPageClicker.fadeInvisible()
        binding.nextPageClicker.isEnabled = false
    }
}
