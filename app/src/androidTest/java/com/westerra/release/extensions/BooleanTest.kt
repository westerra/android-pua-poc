package com.westerra.release.extensions

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BooleanTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun boolean_extensions_test() {
        Assert.assertEquals(true, true.toDeferredBoolean().resolve(context = context))
        Assert.assertEquals(false, false.toDeferredBoolean().resolve(context = context))
    }
}
