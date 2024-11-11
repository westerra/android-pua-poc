package com.westerra.release.profile.usecase

import com.backbase.android.identity.journey.userprofile.models.UserContactDetails
import com.backbase.android.identity.journey.userprofile.models.UserContactDetailsWithAddress
import com.backbase.android.identity.journey.userprofile.models.UserPostalAddress

class UserContactDetailsWithAddressDto(
    override val contactDetails: UserContactDetails,
    override val postalAddress: UserPostalAddress,
) : UserContactDetailsWithAddress {
    companion object {
        // BuildingNumber not used on front end, addressLine not used on backend.
        // Backend uses buildingNumber as streetName.
        // Backend uses streetName for second address line.
        fun convertFrontToBackEnd(
            address: UserContactDetailsWithAddress,
        ): UserContactDetailsWithAddressDto {
            val addressDto =
                UserPostalAddressDto.convertFrontToBackEnd(postalAddress = address.postalAddress)
            val contactDetails = address.contactDetails
            val contactDetailsDto =
                UserContactDetailsDto(
                    information = contactDetails.information,
                    isPrimary = contactDetails.isPrimary,
                    key = contactDetails.key,
                    // Note: type is missing/wrong here, use key instead
                    type = contactDetails.key,
                )
            return UserContactDetailsWithAddressDto(
                contactDetails = contactDetailsDto,
                postalAddress = addressDto,
            )
        }

        fun convertBackToFrontEnd(
            address: UserContactDetailsWithAddress,
        ): UserContactDetailsWithAddressDto {
            val addressDto =
                UserPostalAddressDto.convertBackToFrontEnd(
                    postalAddress = address.postalAddress,
                )
            val contactDetailsDto =
                UserContactDetailsDto.convertBackToFrontEnd(
                    contactDetails = address.contactDetails,
                )
            return UserContactDetailsWithAddressDto(
                contactDetails = contactDetailsDto,
                postalAddress = addressDto,
            )
        }
    }
}
