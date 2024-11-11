package com.westerra.release.firebase.model

import org.junit.Assert
import org.junit.Test

class MinimumVersionConfigTest {

    private val minimumVersionConfig1 = MinimumVersionConfig(
        "0.0.0",
        "Min Title",
        "Min Message"
    )
    private val minimumVersionConfig2 = MinimumVersionConfig(
        "99.99.99",
        "Min Title",
        "Min Message"
    )

    @Test
    fun minimumVersionConfig_test() {
        Assert.assertFalse(minimumVersionConfig1.isUpdateNeeded())
        Assert.assertTrue(minimumVersionConfig2.isUpdateNeeded())
    }
}
