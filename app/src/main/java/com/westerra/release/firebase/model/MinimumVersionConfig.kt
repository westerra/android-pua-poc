package com.westerra.release.firebase.model

import com.google.gson.annotations.SerializedName
import com.westerra.release.BuildConfig
import com.westerra.release.extensions.compareVersion

data class MinimumVersionConfig(
    @SerializedName("minimum_version_android")
    var minVersion: String?,

    @SerializedName("minimum_version_title")
    var minTitle: String?,

    @SerializedName("minimum_version_message")
    var minMessage: String?,
) {
    fun isUpdateNeeded(): Boolean {
        minVersion ?. let { version ->
            return compareBuildVersion(otherVersion = version)
        }
        return false
    }

    private fun compareBuildVersion(otherVersion: String): Boolean {
        return BuildConfig.VERSION_NAME.compareVersion(otherVersion = otherVersion)
    }
}
