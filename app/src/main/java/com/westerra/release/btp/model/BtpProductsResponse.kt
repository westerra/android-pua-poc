package com.westerra.release.btp.model

import com.westerra.release.sso.model.NetworkResponse

data class BtpProductsResponse(
    var products: List<WesterraProduct>? = null,
    var profile: ProfileResponse? = null,
    override var errorMessage: String?,
    override var error: String? = null,
) : NetworkResponse()
