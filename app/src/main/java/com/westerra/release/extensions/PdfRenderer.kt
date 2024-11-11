package com.westerra.release.extensions

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.util.Log

fun PdfRenderer.closeSafe() {
    try {
        this.close()
    } catch (e: IllegalStateException) {
        Log.i(
            "PdfRenderer.closeSafe",
            "IllegalStateException closing PdfRenderer: ${e.message}",
        )
    }
}

fun PdfRenderer.Page.closeSafe() {
    try {
        this.close()
    } catch (e: IllegalStateException) {
        Log.i(
            "PdfRenderer.Page.closeSafe",
            "IllegalStateException closing PdfRenderer.Page: ${e.message}",
        )
    }
}

fun PdfRenderer.Page.renderPdfBitmap(width: Int, height: Int): Bitmap? {
    val result: Bitmap?
    try {
        result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    } catch (e: IllegalArgumentException) {
        Log.i(
            "PdfRenderer.Page.renderPdfBitmap",
            "IllegalArgumentException creating Bitmap: ${e.message}",
        )
        return null
    }
    if (result == null) {
        Log.e("PdfRenderer.Page.renderPdfBitmap", "Bitmap creation failed unexpectedly.")
        return null
    }
    try {
        this.render(result, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    } catch (e: Exception) {
        Log.e(
            "PdfRenderer.Page.renderPdfBitmap",
            "Render bitmap for display attempt failed unexpectedly.",
        )
        return null
    }
    return result
}
