package com.westerra.release.btp.model

import com.westerra.release.sso.model.NetworkResponse

data class AddProductResponse(
    override var errorMessage: String?,
    override var error: String? = null,
    var id: String? = null,
    var status: String? = null,
    var bankStatus: String? = null,
    var exportAllowed: Boolean? = null,
) : NetworkResponse()
