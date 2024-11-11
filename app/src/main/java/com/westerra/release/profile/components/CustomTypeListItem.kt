package com.westerra.release.profile.components

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.backbase.android.design.button.BackbaseRadioButton
import com.backbase.android.identity.journey.userprofile.models.UserPersonalInformation
import com.westerra.release.R
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.LAST_GET_MY_INFO_KEY
import com.westerra.release.custom.DataTransferCacheKeys.LAST_PROFILE_ADD_SCREEN_KEY
import com.westerra.release.extensions.backbase.isAddressTypeUsed
import com.westerra.release.extensions.backbase.isPhoneTypeUsed
import com.westerra.release.profile.components.AddAddressScreenDetector.Companion.ADD_ADDRESS_SCREEN
import com.westerra.release.profile.components.AddPhoneNumberScreenDetector.Companion.ADD_PHONE_NUMBER_SCREEN

class CustomTypeListItem
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        renderLayout()
    }

    private fun renderLayout() {
        val lastMyInfo = DataTransferCache().retrieve(id = LAST_GET_MY_INFO_KEY)
        val lastScreen = DataTransferCache().retrieve(id = LAST_PROFILE_ADD_SCREEN_KEY)
        if (
            lastMyInfo is UserPersonalInformation &&
            lastScreen is String
        ) {
            renderLayout(myInfo = lastMyInfo, lastScreen = lastScreen)
        }
    }

    private fun renderLayout(myInfo: UserPersonalInformation, lastScreen: String) {
        var textView: TextView? = null
        var radioButton: BackbaseRadioButton? = null
        this.children.forEach { child ->
            when (child) {
                is BackbaseRadioButton -> {
                    radioButton = child
                }
                is TextView -> {
                    textView = child
                }
            }
        }
        textView ?. let { text ->
            radioButton ?. let { radio ->
                renderLayout(
                    myInfo = myInfo,
                    textView = text,
                    radioButton = radio,
                    lastScreen = lastScreen,
                )
            }
        }
    }

    private fun renderLayout(
        myInfo: UserPersonalInformation,
        textView: TextView,
        radioButton: BackbaseRadioButton,
        lastScreen: String,
    ) {
        val text = textView.text?.toString()
        if (text == null || text.isEmpty()) {
            return
        }
        if (
            (lastScreen == ADD_PHONE_NUMBER_SCREEN && myInfo.isPhoneTypeUsed(text)) ||
            (lastScreen == ADD_ADDRESS_SCREEN && myInfo.isAddressTypeUsed(text))
        ) {
            renderDisabled(textView = textView, radioButton = radioButton)
        } else {
            renderEnabled(textView = textView, radioButton = radioButton)
        }
    }

    private fun renderEnabled(textView: TextView, radioButton: BackbaseRadioButton) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
        radioButton.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
        radioButton.isClickable = true
        radioButton.isFocusable = true
        radioButton.isEnabled = true
        this.isClickable = true
        this.isFocusable = true
        this.isEnabled = true
    }

    private fun renderDisabled(textView: TextView, radioButton: BackbaseRadioButton) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.disabledText))
        radioButton.setTextColor(ContextCompat.getColor(context, R.color.disabledText))
        radioButton.isClickable = false
        radioButton.isFocusable = false
        radioButton.isEnabled = false
        this.isClickable = false
        this.isFocusable = false
        this.isEnabled = false
    }
}
