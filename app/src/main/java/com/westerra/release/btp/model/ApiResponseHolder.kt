package com.westerra.release.btp.model

open class ApiResponseHolder<T>(val data: T?, private val errorMessage: String?) {
    fun isSuccess(): Boolean {
        return data != null
    }

    fun getErrorMessage(): String {
        if (errorMessage?.isNotBlank() == true) {
            return errorMessage
        }
        return "Network failure, please try again."
    }
}
