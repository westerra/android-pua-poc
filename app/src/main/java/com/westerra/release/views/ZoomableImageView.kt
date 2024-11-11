package com.westerra.release.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import kotlin.math.abs
import kotlin.math.min

// https://stackoverflow.com/questions/6650398/android-imageview-zoom-in-and-zoom-out
class ZoomableImageView(context: Context, attr: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attr) {
    var isEmbeddedInRecyclerView = true
    var recyclerViewScrollsVertically = true

    private var zoomMatrix = Matrix()
    private var mode: Int = NONE
    private var last: PointF = PointF()
    private var start: PointF = PointF()
    private var minScale: Float = 1f
    private var maxScale: Float = 4f
    private var m: FloatArray

    private var redundantXSpace: Float = 0f
    private var redundantYSpace: Float = 0f
    private var width: Float = 0f
    private var height: Float = 0f
    private var saveScale: Float = 1f
    private var right: Float = 0f
    private var bottom: Float = 0f
    private var origWidth: Float = 0f
    private var origHeight: Float = 0f
    private var bmWidth: Float = 0f
    private var bmHeight: Float = 0f

    private var mScaleDetector: ScaleGestureDetector

    init {
        super.setClickable(true)
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        zoomMatrix.setTranslate(1f, 1f)
        m = FloatArray(9)
        imageMatrix = zoomMatrix
        scaleType = ScaleType.MATRIX

        setOnTouchListener { view, event ->
            // Logic here added to make the zooming work when embedded in a RecyclerView
            // https://stackoverflow.com/questions/31584754/recyclerview-with-imageviews-zoom-on-touch
            var result = true
            if (isEmbeddedInRecyclerView) {
                val notAtEdge = if (recyclerViewScrollsVertically) {
                    view.canScrollVertically(1) && canScrollVertically(-1)
                } else {
                    view.canScrollHorizontally(1) && canScrollHorizontally(-1)
                }
                if (event.pointerCount >= 2 || notAtEdge) {
                    // multi-touch event
                    result = when (event.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            // Disallow RecyclerView to intercept touch events.
                            parent.requestDisallowInterceptTouchEvent(true)
                            // Disable touch on view
                            false
                        }
                        MotionEvent.ACTION_UP -> {
                            // Allow RecyclerView to intercept touch events.
                            parent.requestDisallowInterceptTouchEvent(false)
                            true
                        }
                        else -> true
                    }
                }
            }

            mScaleDetector.onTouchEvent(event)
            zoomMatrix.getValues(m)
            val x = m[Matrix.MTRANS_X]
            val y = m[Matrix.MTRANS_Y]
            val curr = PointF(event.x, event.y)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    last[event.x] = event.y
                    start.set(last)
                    mode = DRAG
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    last[event.x] = event.y
                    start.set(last)
                    mode = ZOOM
                }

                MotionEvent.ACTION_MOVE ->
                    // if the mode is ZOOM or
                    // if the mode is DRAG and already zoomed
                    if (mode == ZOOM || (mode == DRAG && saveScale > minScale)) {
                        var deltaX = curr.x - last.x // x difference
                        var deltaY = curr.y - last.y // y difference
                        val scaleWidth = Math.round(origWidth * saveScale)
                            .toFloat() // width after applying current scale
                        val scaleHeight = Math.round(origHeight * saveScale)
                            .toFloat() // height after applying current scale
                        // if scaleWidth is smaller than the views width
                        // in other words if the image width fits in the view
                        // limit left and right movement
                        if (scaleWidth < width) {
                            deltaX = 0f
                            if (y + deltaY > 0) {
                                deltaY = -y
                            } else if (y + deltaY < -bottom) {
                                deltaY = -(y + bottom)
                            }
                        } else if (scaleHeight < height) {
                            deltaY = 0f
                            if (x + deltaX > 0) {
                                deltaX = -x
                            } else if (x + deltaX < -right) {
                                deltaX = -(x + right)
                            }
                        } else {
                            if (x + deltaX > 0) {
                                deltaX = -x
                            } else if (x + deltaX < -right) {
                                deltaX = -(x + right)
                            }

                            if (y + deltaY > 0) {
                                deltaY = -y
                            } else if (y + deltaY < -bottom) {
                                deltaY = -(y + bottom)
                            }
                        }
                        // move the image with the matrix
                        zoomMatrix.postTranslate(deltaX, deltaY)
                        // set the last touch location to the current
                        last[curr.x] = curr.y
                    }

                MotionEvent.ACTION_UP -> {
                    mode = NONE
                    val xDiff = abs((curr.x - start.x).toDouble()).toInt()
                    val yDiff = abs((curr.y - start.y).toDouble()).toInt()
                    if (xDiff < CLICK && yDiff < CLICK) performClick()
                }

                MotionEvent.ACTION_POINTER_UP -> mode = NONE
            }
            imageMatrix = zoomMatrix
            invalidate()
            result
        }
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        bmWidth = bm.width.toFloat()
        bmHeight = bm.height.toFloat()
    }

    fun setMaxZoom(x: Float) {
        maxScale = x
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = detector.scaleFactor
            val origScale = saveScale
            saveScale *= mScaleFactor
            if (saveScale > maxScale) {
                saveScale = maxScale
                mScaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                mScaleFactor = minScale / origScale
            }
            right = width * saveScale - width - (2 * redundantXSpace * saveScale)
            bottom = height * saveScale - height - (2 * redundantYSpace * saveScale)
            if (origWidth * saveScale <= width || origHeight * saveScale <= height) {
                zoomMatrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2)
                if (mScaleFactor < 1) {
                    zoomMatrix.getValues(m)
                    val x = m[Matrix.MTRANS_X]
                    val y = m[Matrix.MTRANS_Y]
                    if (Math.round(origWidth * saveScale) < width) {
                        if (y < -bottom) {
                            zoomMatrix.postTranslate(0F, -(y + bottom))
                        } else if (y > 0) {
                            zoomMatrix.postTranslate(0F, -y)
                        }
                    } else {
                        if (x < -right) {
                            zoomMatrix.postTranslate(-(x + right), 0F)
                        } else if (x > 0) {
                            zoomMatrix.postTranslate(-x, 0F)
                        }
                    }
                }
            } else {
                zoomMatrix.postScale(mScaleFactor, mScaleFactor, detector.focusX, detector.focusY)
                zoomMatrix.getValues(m)
                val x = m[Matrix.MTRANS_X]
                val y = m[Matrix.MTRANS_Y]
                if (mScaleFactor < 1) {
                    if (x < -right) {
                        zoomMatrix.postTranslate(-(x + right), 0F)
                    } else if (x > 0) {
                        zoomMatrix.postTranslate(-x, 0F)
                    }
                    if (y < -bottom) {
                        zoomMatrix.postTranslate(0F, -(y + bottom))
                    } else if (y > 0) {
                        zoomMatrix.postTranslate(0F, -y)
                    }
                }
            }
            return true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        height = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        // Fit to screen.
        val scale: Float
        val scaleX = width / bmWidth
        val scaleY = height / bmHeight
        scale = min(scaleX.toDouble(), scaleY.toDouble()).toFloat()
        zoomMatrix.setScale(scale, scale)
        imageMatrix = zoomMatrix
        saveScale = 1f

        // Center the image
        redundantYSpace = height - (scale * bmHeight)
        redundantXSpace = width - (scale * bmWidth)
        redundantYSpace /= 2f
        redundantXSpace /= 2f

        zoomMatrix.postTranslate(redundantXSpace, redundantYSpace)

        origWidth = width - 2 * redundantXSpace
        origHeight = height - 2 * redundantYSpace
        right = width * saveScale - width - (2 * redundantXSpace * saveScale)
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale)
        imageMatrix = zoomMatrix
    }

    companion object {
        const val NONE: Int = 0
        const val DRAG: Int = 1
        const val ZOOM: Int = 2
        const val CLICK: Int = 3
    }
}
