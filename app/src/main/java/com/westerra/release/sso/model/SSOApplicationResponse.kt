package com.westerra.release.sso.model

import com.squareup.moshi.Json

data class SSOApplicationResponse(
    override var errorMessage: String?,
    override var error: String? = null,

    @Json(name = "name")
    var name: String = "",

    @Json(name = "ssourl")
    var ssoUrl: String = "",

    var status: Int? = null,
) : NetworkResponse()
