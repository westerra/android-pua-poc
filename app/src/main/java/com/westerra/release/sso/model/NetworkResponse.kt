package com.westerra.release.sso.model

abstract class NetworkResponse(
    open var errorMessage: String? = null,
    open var errorDescription: String? = null,
    open var error: String? = null,
)

fun NetworkResponse.isError(): Boolean {
    return getErrorMessage()?.isNotEmpty() ?: false
}

fun NetworkResponse.getErrorMessage(): String? {
    return errorDescription ?: errorMessage ?: error
}
