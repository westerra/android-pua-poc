package com.westerra.release.extensions

import com.backbase.deferredresources.DeferredBoolean

fun Boolean.toDeferredBoolean(): DeferredBoolean {
    return DeferredBoolean.Constant(this)
}
