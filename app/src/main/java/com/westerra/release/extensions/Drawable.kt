package com.westerra.release.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun Drawable.toMarkerIcon(scalingFactor: Int?): BitmapDescriptor? {
    val canvas = Canvas()
    val scaling = if (scalingFactor == 0) 1 else (scalingFactor ?: 1)
    val width = this.intrinsicWidth / scaling
    val height = this.intrinsicHeight / scaling
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    canvas.setBitmap(bitmap)
    this.setBounds(0, 0, width, height)
    this.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
