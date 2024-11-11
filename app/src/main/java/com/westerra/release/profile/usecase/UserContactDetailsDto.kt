package com.westerra.release.profile.usecase

import com.backbase.android.identity.journey.userprofile.models.UserContactDetails

class UserContactDetailsDto(
    override val information: String,
    override val isPrimary: Boolean,
    override val key: String,
    override val type: String,
) : UserContactDetails {
    companion object {
        fun convertBackToFrontEnd(contactDetails: UserContactDetails): UserContactDetailsDto {
            return UserContactDetailsDto(
                information = contactDetails.information,
                isPrimary = contactDetails.isPrimary,
                key = contactDetails.key,
                type = contactDetails.type,
            )
        }
    }
}
