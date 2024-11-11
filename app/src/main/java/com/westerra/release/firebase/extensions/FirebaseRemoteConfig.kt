package com.westerra.release.firebase.extensions

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.westerra.release.BuildConfig
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.constants.Constants
import com.westerra.release.firebase.RemoteConfigProvider
import com.westerra.release.firebase.RemoteConfigProvider.Companion.BTP_PRODUCTS
import com.westerra.release.firebase.RemoteConfigProvider.Companion.BTP_PRODUCTS_QA
import com.westerra.release.firebase.RemoteConfigProvider.Companion.INTERCEPT_AD
import com.westerra.release.firebase.RemoteConfigProvider.Companion.INTERCEPT_AD_QA
import com.westerra.release.firebase.RemoteConfigProvider.Companion.MIN_VERSION
import com.westerra.release.firebase.RemoteConfigProvider.Companion.MIN_VERSION_QA
import com.westerra.release.firebase.RemoteConfigProvider.Companion.WAF_CONFIG
import com.westerra.release.firebase.RemoteConfigProvider.Companion.WAF_CONFIG_QA
import com.westerra.release.firebase.model.InterceptAdvertisementConfig
import com.westerra.release.firebase.model.MinimumVersionConfig
import com.westerra.release.firebase.model.WafConfig

fun FirebaseRemoteConfig.getWafConfig(): WafConfig? {
    val key = if (BuildConfig.FLAVOR == Constants.BUILD_FLAVOR_PROD) WAF_CONFIG else WAF_CONFIG_QA
    val json = this[key].asString()
    if (json.isEmpty()) return null
    Log.d(RemoteConfigProvider.TAG, "WafConfigJson: $json")
    val gson = Gson()
    var result: WafConfig? = null
    try {
        result = gson.fromJson(json, WafConfig::class.java)
    } catch (e: JsonSyntaxException) {
        Log.e(RemoteConfigProvider.TAG, "getWafConfig failed: ${e.message}")
    }
    return result
}

fun FirebaseRemoteConfig.getMinimumVersionConfig(): MinimumVersionConfig? {
    val key = if (BuildConfig.FLAVOR == Constants.BUILD_FLAVOR_PROD) MIN_VERSION else MIN_VERSION_QA
    val json = this[key].asString()
    if (json.isEmpty()) return null
    Log.d(RemoteConfigProvider.TAG, "MinVersionConfigJson: $json")
    val gson = Gson()
    var result: MinimumVersionConfig? = null
    try {
        result = gson.fromJson(json, MinimumVersionConfig::class.java)
    } catch (e: JsonSyntaxException) {
        Log.e(RemoteConfigProvider.TAG, "getMinimumVersionConfig failed: ${e.message}")
    }
    return result
}

fun FirebaseRemoteConfig.isInterceptAdvertisementEnabled(): Boolean {
    val key = if (BuildConfig.FLAVOR == Constants.BUILD_FLAVOR_PROD) {
        INTERCEPT_AD
    } else {
        INTERCEPT_AD_QA
    }
    val json = this[key].asString()
    if (json.isEmpty()) return false
    Log.d(RemoteConfigProvider.TAG, "InterceptAdvertisement: $json")
    val gson = Gson()
    var result: InterceptAdvertisementConfig? = null
    try {
        result = gson.fromJson(json, InterceptAdvertisementConfig::class.java)
    } catch (e: JsonSyntaxException) {
        Log.e(RemoteConfigProvider.TAG, "isInterceptAdvertisementEnabled failed: ${e.message}")
        return false
    }
    return result.isEnabled()
}

fun FirebaseRemoteConfig.westerraProductList(): List<WesterraProduct> {
    val key = if (BuildConfig.FLAVOR == Constants.BUILD_FLAVOR_PROD) {
        BTP_PRODUCTS
    } else {
        BTP_PRODUCTS_QA
    }
    val json = this[key].asString()
    if (json.isEmpty()) return listOf()
    Log.d(RemoteConfigProvider.TAG, "westerraProductsList: $json")
    val gson = Gson()
    val result: List<WesterraProduct>?
    val itemType = object : TypeToken<List<WesterraProduct>>() {}.type
    try {
        result = gson.fromJson<List<WesterraProduct>>(json, itemType)
    } catch (e: JsonSyntaxException) {
        Log.e(RemoteConfigProvider.TAG, "westerraProductsList failed: ${e.message}")
        return listOf()
    }
    return result
}
