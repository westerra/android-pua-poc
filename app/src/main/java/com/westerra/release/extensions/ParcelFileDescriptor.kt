package com.westerra.release.extensions

import android.os.ParcelFileDescriptor
import android.util.Log

fun ParcelFileDescriptor.closeSafe() {
    try {
        this.close()
    } catch (e: IllegalStateException) {
        Log.i(
            "ParcelFileDescriptor.closeSafe",
            "IllegalStateException closing ParcelFileDescriptor: ${e.message}",
        )
    }
}
