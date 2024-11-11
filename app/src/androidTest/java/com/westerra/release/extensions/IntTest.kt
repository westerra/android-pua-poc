package com.westerra.release.extensions

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.westerra.release.test.R
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class IntTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun int_extensions_test() {
        Assert.assertEquals(
            "WesterraCUTest",
            R.string.app_name.toDeferredText().resolve(context = context)
        )
        Assert.assertEquals(
            "Deposit a check",
            R.string.deposit_check_title.toDeferredText().resolve(context = context)
        )
    }
}
