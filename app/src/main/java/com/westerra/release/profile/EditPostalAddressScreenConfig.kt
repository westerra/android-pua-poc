package com.westerra.release.profile

import com.backbase.android.identity.journey.userprofile.address.PostalAddressScreenConfiguration
import com.backbase.deferredresources.DeferredTextArray
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.extensions.toDeferredBoolean
import com.westerra.release.extensions.toDeferredText

object EditPostalAddressScreenConfig {
    operator fun invoke(): PostalAddressScreenConfiguration {
        return PostalAddressScreenConfiguration {
            isAutocompleteEnabled = false.toDeferredBoolean()
            visibleFields =
                listOf(
                    PostalAddressScreenConfiguration.Field.STREET,
                    PostalAddressScreenConfiguration.Field.ADDRESS_LINE,
                    PostalAddressScreenConfiguration.Field.CITY,
                    PostalAddressScreenConfiguration.Field.SUBDIVISION,
                    PostalAddressScreenConfiguration.Field.POSTCODE,
                )
            requiredFields =
                setOf(
                    PostalAddressScreenConfiguration.Field.STREET,
                    PostalAddressScreenConfiguration.Field.CITY,
                    PostalAddressScreenConfiguration.Field.SUBDIVISION,
                    PostalAddressScreenConfiguration.Field.POSTCODE,
                )
            streetNameLabelText = R.string.address_line_1.toDeferredText()
            addressLineLabelText = R.string.address_line_2.toDeferredText()
            cityLabelText = R.string.city_or_town.toDeferredText()
            subdivisionLabelText = R.string.state_title.toDeferredText()
            postcodeLabelText = R.string.postal_zip_code.toDeferredText()
            availableSubdivisions = getStates()
            isPrimaryEditable = false.toDeferredBoolean()
            isTypeEditable = false.toDeferredBoolean()
            confirmText = R.string.save_changes.toDeferredText()
            addressTitleText = R.string.change_address.toDeferredText()
        }
    }

    private fun getStates(): DeferredTextArray {
        return DeferredTextArray.Constant(
            WesterraApplication.getInstance().applicationContext.resources
                .getStringArray(R.array.states).toList(),
        )
    }
}
