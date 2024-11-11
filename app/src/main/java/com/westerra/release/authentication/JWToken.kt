package com.westerra.release.authentication

import android.util.Base64
import org.json.JSONObject

class JWToken(tokenString: String) {
    private var payload: JSONObject
    private var signature: String
    private var encodedHeader: JSONObject
    private var exp: Long = 0L
    private var iat: Long = 0L

    companion object {
        private const val KEY_IAT = "iat"
        private const val KEY_EXP = "exp"
    }

    init {
        val parts = tokenString.split(' ').last().split('.')
        encodedHeader = JSONObject(decode(parts[0]))
        payload = JSONObject(decode(parts[1]))
        signature = decode(parts[2])
        iat = payload.getLong(KEY_IAT)
        exp = payload.getLong(KEY_EXP)
    }

    private val current: Long
        get() = (System.currentTimeMillis() / 1000L)

    val isExpired: Boolean
        get() = secondsUntilExpired <= 0L

    val secondsUntilExpired: Long
        get() = exp - current

    val secondsSinceIssued: Long
        get() = current - iat

    private fun encode(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.URL_SAFE)

    private fun decode(encodedString: String): String =
        String(Base64.decode(encodedString, Base64.URL_SAFE))
}
