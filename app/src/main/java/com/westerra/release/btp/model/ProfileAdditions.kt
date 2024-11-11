package com.westerra.release.btp.model

data class ProfileAdditions(
    val customerCode: String?,
    val ssn: String?,
    var warningCodePresent: String? = null,
    var warningCodes: List<String>? = null,
) : java.io.Serializable {
    constructor() : this(null, null, null, null)
    override fun toString(): String {
        return "ProfileAdditions{ customerCode: XXX, " +
            "ssn: XXX, warningCodePresent: $warningCodePresent, warningCodes: $warningCodes}"
    }

    fun isEligible(): Boolean {
        if (warningCodePresent != null && warningCodePresent == "true") return false
        warningCodes ?. let {
            if (it.isNotEmpty()) return false
        }
        return true
    }
}
