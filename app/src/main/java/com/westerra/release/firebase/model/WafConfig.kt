package com.westerra.release.firebase.model

import com.google.gson.annotations.SerializedName

data class WafConfig(
    @SerializedName("is_enabled")
    var isEnabled: Boolean?,
)

fun WafConfig.isRemoteEnabled(): Boolean {
    return isEnabled ?: return true
}
