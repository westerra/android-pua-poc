package com.westerra.release.profile.usecase

import com.backbase.android.identity.journey.userprofile.UserProfileUseCase
import com.backbase.android.identity.journey.userprofile.models.UserContactDetails
import com.backbase.android.identity.journey.userprofile.models.UserContactDetailsWithAddress
import com.backbase.android.identity.journey.userprofile.usecase.UserManagerUserProfileUseCase
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.LAST_GET_ADDRESS_KEY
import com.westerra.release.custom.DataTransferCacheKeys.LAST_GET_MY_INFO_KEY
import com.westerra.release.custom.DataTransferCacheKeys.LAST_GET_PHONE_NUMBER_KEY

class CustomUserProfileUseCase(
    private val defaultUseCase: UserManagerUserProfileUseCase,
) : UserProfileUseCase {
    override suspend fun getUserPersonalInformation(): UserProfileUseCase.UserProfileResult {
        val result = defaultUseCase.getUserPersonalInformation()
        if (result is UserProfileUseCase.UserProfileResult.Success) {
            DataTransferCache().save(LAST_GET_MY_INFO_KEY, result.personalInformation)
        }
        return result
    }

    // Email

    override suspend fun getUserEmail(emailKey: String): UserProfileUseCase.EmailResult {
        return defaultUseCase.getUserEmail(emailKey = emailKey)
    }

    override suspend fun addUserEmailAddress(
        emailAddress: UserContactDetails,
    ): UserProfileUseCase.PersonalInformationUpdateResult {
        return defaultUseCase.addUserEmailAddress(emailAddress = emailAddress)
    }

    override suspend fun updateUserEmail(
        emailKey: String,
        email: UserContactDetails,
    ): UserProfileUseCase.PersonalInformationUpdateResult {
        val customEmail = UserContactDetailsDto(
            information = email.information,
            isPrimary = email.isPrimary,
            key = email.key,
            type = email.key,
        )
        return defaultUseCase.updateUserEmail(emailKey = emailKey, email = customEmail)
    }

    override suspend fun deleteUserEmail(
        emailKey: String,
    ): UserProfileUseCase.PersonalInformationUpdateResult {
        return defaultUseCase.deleteUserEmail(emailKey = emailKey)
    }

    // Phone Number

    override suspend fun getUserPhoneNumber(
        phoneNumberKey: String,
    ): UserProfileUseCase.PhoneNumberResult {
        val result = defaultUseCase.getUserPhoneNumber(phoneNumberKey = phoneNumberKey)
        if (result is UserProfileUseCase.PhoneNumberResult.Success) {
            DataTransferCache().save(LAST_GET_PHONE_NUMBER_KEY, result.phoneNumber)
        }
        return result
    }

    override suspend fun addUserPhoneNumber(
        phoneNumber: UserContactDetails,
    ): UserProfileUseCase.PersonalInformationUpdateResult {
        return defaultUseCase.addUserPhoneNumber(phoneNumber = phoneNumber)
    }

    override suspend fun updateUserPhoneNumber(
        phoneNumberKey: String,
        phoneNumber: UserContactDetails,
    ): UserProfileUseCase.PersonalInformationUpdateResult {
        val phoneNumberDto = UserContactDetailsDto(
            information = phoneNumber.information,
            isPrimary = phoneNumber.isPrimary,
            key = phoneNumber.key,
            // Note: type is missing/wrong here, use key instead
            type = phoneNumber.key,
        )
        return defaultUseCase.updateUserPhoneNumber(
            phoneNumberKey = phoneNumberKey,
            phoneNumber = phoneNumberDto,
        )
    }

    override suspend fun deleteUserPhoneNumber(
        phoneNumberKey: String,
    ): UserProfileUseCase.PersonalInformationUpdateResult {
        return defaultUseCase.deleteUserPhoneNumber(phoneNumberKey = phoneNumberKey)
    }

    // Postal Address

    override suspend fun getUserPostalAddress(
        postalAddressKey: String,
    ): UserProfileUseCase.PostalAddressResult {
        val defaultResult = defaultUseCase.getUserPostalAddress(postalAddressKey)
        if (defaultResult is UserProfileUseCase.PostalAddressResult.Success) {
            val dto = UserContactDetailsWithAddressDto.convertBackToFrontEnd(
                address = defaultResult.address,
            )
            DataTransferCache().save(LAST_GET_ADDRESS_KEY, dto)
            return UserProfileUseCase.PostalAddressResult.Success(address = dto)
        }
        return defaultResult
    }

    override suspend fun addUserPostalAddress(
        address: UserContactDetailsWithAddress,
    ): UserProfileUseCase.PersonalInformationUpdateResult {
        val dto = UserContactDetailsWithAddressDto.convertFrontToBackEnd(address = address)
        return defaultUseCase.addUserPostalAddress(dto)
    }

    override suspend fun updateUserPostalAddress(
        addressKey: String,
        address: UserContactDetailsWithAddress,
    ): UserProfileUseCase.PersonalInformationUpdateResult {
        val dto = UserContactDetailsWithAddressDto.convertFrontToBackEnd(address = address)
        return defaultUseCase.updateUserPostalAddress(addressKey, dto)
    }

    override suspend fun deleteUserPostalAddress(
        addressKey: String,
    ): UserProfileUseCase.PersonalInformationUpdateResult {
        return defaultUseCase.deleteUserPostalAddress(addressKey)
    }
}
