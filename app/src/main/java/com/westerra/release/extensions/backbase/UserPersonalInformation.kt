package com.westerra.release.extensions.backbase

import com.backbase.android.identity.journey.userprofile.models.UserPersonalInformation
import com.westerra.release.profile.usecase.ContactDetailsTypeConverter

fun UserPersonalInformation.isPhoneTypeUsed(type: String): Boolean {
    phoneNumbers.forEach {
        if (it.type.equals(type, true)) {
            return true
        }
    }
    return false
}

fun UserPersonalInformation.isAddressTypeUsed(type: String): Boolean {
    postalAddresses.forEach { address ->
        val converted = ContactDetailsTypeConverter.convertToApi(type = address.type)
        if (
            address.type.equals(type, true) ||
            converted.equals(type, true)
        ) {
            return true
        }
    }
    return false
}
