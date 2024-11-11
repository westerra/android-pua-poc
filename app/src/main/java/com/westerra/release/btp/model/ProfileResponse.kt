package com.westerra.release.btp.model

import com.westerra.release.sso.model.NetworkResponse

data class ProfileResponse(
    override var errorMessage: String?,
    override var error: String? = null,
    var fullName: String? = null,
    var additions: ProfileAdditions? = null,
) : NetworkResponse() {
    override fun toString(): String {
        return "ProfileResponse{ errorMessage: $errorMessage, " +
            "error: $error, fullName: $fullName, additions: $additions}"
    }
    fun isEligible(): Boolean {
        additions ?. let {
            return it.isEligible()
        }
        return true
    }
}
