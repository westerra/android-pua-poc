package com.westerra.release.alerts

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.westerra.release.custom.MySharedPreferences
import com.westerra.release.databinding.AlertInterceptAdvertisementBinding
import com.westerra.release.firebase.RemoteConfigProvider

class InterceptAdvertisementAlert(activity: Activity) : AlertDialog.Builder(activity) {

    val binding = AlertInterceptAdvertisementBinding.inflate(activity.layoutInflater)

    init {
        setView(binding.root)
        setCancelable(false)
    }

    fun showCustom() {
        val result = create()
        result.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.ctaButton.setOnClickListener {
            result.dismiss()
        }
        binding.closeClicker.setOnClickListener {
            result.dismiss()
        }
        if (RemoteConfigProvider.isInterceptAdEnabled &&
            MySharedPreferences.shouldShowInterceptAdvertisement()
        ) {
            MySharedPreferences.disableInterceptAdvertisement()
            result.show()
        }
    }
}
