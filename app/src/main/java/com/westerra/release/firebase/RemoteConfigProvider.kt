package com.westerra.release.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.westerra.release.R
import com.westerra.release.alerts.UpdateNeededAlert
import com.westerra.release.firebase.extensions.getMinimumVersionConfig
import com.westerra.release.firebase.extensions.getWafConfig
import com.westerra.release.firebase.extensions.isInterceptAdvertisementEnabled
import com.westerra.release.firebase.model.isRemoteEnabled
import com.westerra.release.identity.WafReadySignal

class RemoteConfigProvider {

    companion object {
        val TAG: String = RemoteConfigProvider::class.java.simpleName
        const val MIN_VERSION = "minimum_version"
        const val MIN_VERSION_QA = "minimum_version_qa"
        const val WAF_CONFIG = "waf_config"
        const val WAF_CONFIG_QA = "waf_config_qa"
        const val INTERCEPT_AD = "intercept_advertisement"
        const val INTERCEPT_AD_QA = "intercept_advertisement_qa"
        const val BTP_PRODUCTS = "btp_westerra_products_list"
        const val BTP_PRODUCTS_QA = "btp_westerra_products_list_qa"
        const val FETCH_INTERVAL_SECONDS = 3600L
        var isInterceptAdEnabled = false
    }

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    fun setupFirebaseRemoteConfig(activity: Activity) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = FETCH_INTERVAL_SECONDS
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(TAG, "Config params updated: $updated")
                    onRemoteConfigUpdate(activity = activity)
                } else {
                    Log.d(TAG, "Config params update failed")
                }
            }
    }

    private fun onRemoteConfigUpdate(activity: Activity) {
        remoteConfig.getMinimumVersionConfig() ?. let { minimumVersionConfig ->
            if (minimumVersionConfig.isUpdateNeeded()) {
                UpdateNeededAlert(activity = activity, config = minimumVersionConfig).show()
            }
        }
        remoteConfig.getWafConfig() ?. let { wafConfig ->
            WafReadySignal.isRemotelyEnabled = wafConfig.isRemoteEnabled()
        }
        isInterceptAdEnabled = remoteConfig.isInterceptAdvertisementEnabled()
    }
}
