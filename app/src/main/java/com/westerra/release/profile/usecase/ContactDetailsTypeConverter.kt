package com.westerra.release.profile.usecase

import android.util.Log

class ContactDetailsTypeConverter {
    companion object {
        fun convertToApi(type: String): String {
            return if (type.lowercase() == "pobox") {
                "Mailing"
            } else if (type.lowercase() == "residential") {
                "Home"
            } else {
                Log.w("convertTypeFrontToBackEnd", "Unexpected type: $type")
                type
            }
        }
    }
}
