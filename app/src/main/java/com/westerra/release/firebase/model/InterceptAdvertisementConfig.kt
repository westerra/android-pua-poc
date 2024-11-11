package com.westerra.release.firebase.model

import com.google.gson.annotations.SerializedName

data class InterceptAdvertisementConfig(
    @SerializedName("is_enabled")
    var isEnabled: Boolean?,
) {
    fun isEnabled(): Boolean {
        return isEnabled ?: false
    }
}
