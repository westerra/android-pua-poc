package com.westerra.release.extensions

import android.net.Uri
import android.webkit.MimeTypeMap
import android.webkit.URLUtil

fun Uri.guessFileName(): String? {
    return URLUtil.guessFileName(this.toString(), null, getMimeType())
}

fun Uri.getMimeType(): String? {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
        MimeTypeMap.getFileExtensionFromUrl(this.toString()),
    )
}
