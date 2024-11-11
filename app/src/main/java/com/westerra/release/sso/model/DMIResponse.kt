package com.westerra.release.sso.model

data class DMIResponse(
    override var errorMessage: String?,
    override var error: String? = null,
    var location: String? = null,
    var status: Int? = null,
) : NetworkResponse()
