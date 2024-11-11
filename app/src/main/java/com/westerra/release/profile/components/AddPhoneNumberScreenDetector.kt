package com.westerra.release.profile.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.LAST_PROFILE_ADD_SCREEN_KEY

class AddPhoneNumberScreenDetector
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    companion object {
        const val ADD_PHONE_NUMBER_SCREEN = "AddPhoneNumberScreen"
    }
    override fun onAttachedToWindow() {
        DataTransferCache().save(id = LAST_PROFILE_ADD_SCREEN_KEY, obj = ADD_PHONE_NUMBER_SCREEN)
        super.onAttachedToWindow()
    }
}
