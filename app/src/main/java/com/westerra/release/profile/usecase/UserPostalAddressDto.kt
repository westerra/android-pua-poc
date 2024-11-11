package com.westerra.release.profile.usecase

import android.util.Log
import com.backbase.android.identity.journey.userprofile.models.UserPostalAddress
import com.westerra.release.R
import com.westerra.release.WesterraApplication

class UserPostalAddressDto(
    override val addressLine: String,
    override val buildingNumber: String,
    override val country: String,
    override val countrySubdivision: String,
    override val department: String,
    override val postalCode: String,
    override val streetName: String,
    override val subDepartment: String,
    override val townName: String,
) : UserPostalAddress {
    companion object {
        fun convertBackToFrontEnd(postalAddress: UserPostalAddress): UserPostalAddressDto {
            return UserPostalAddressDto(
                addressLine = postalAddress.streetName,
                buildingNumber = "",
                country = postalAddress.country,
                countrySubdivision = lookupState(postalAddress.countrySubdivision),
                department = postalAddress.department,
                postalCode = postalAddress.postalCode,
                streetName = postalAddress.buildingNumber,
                subDepartment = postalAddress.subDepartment,
                townName = postalAddress.townName,
            )
        }

        fun convertFrontToBackEnd(postalAddress: UserPostalAddress): UserPostalAddressDto {
            return UserPostalAddressDto(
                addressLine = "",
                buildingNumber = postalAddress.streetName,
                country = postalAddress.country,
                countrySubdivision = parseStateInitials(postalAddress.countrySubdivision),
                department = postalAddress.department,
                postalCode = postalAddress.postalCode,
                streetName = postalAddress.addressLine,
                subDepartment = postalAddress.subDepartment,
                townName = postalAddress.townName,
            )
        }

        private fun lookupState(initials: String): String {
            val states =
                WesterraApplication.getInstance().applicationContext.resources.getStringArray(
                    R.array.states,
                )
            for (state in states) {
                if (state.lowercase().startsWith(initials.lowercase())) {
                    return state
                }
            }
            Log.w("lookupState", "No state found for initials: $initials")
            return initials
        }

        private fun parseStateInitials(state: String): String {
            return state.substring(0, 2)
        }
    }
}
