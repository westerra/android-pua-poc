package com.westerra.release.contacts

import com.backbase.android.retail.journey.contacts.ContactsConfiguration
import com.backbase.android.retail.journey.contacts.details.ContactDetailsScreenConfiguration
import com.backbase.android.retail.journey.contacts.form.AccountNumberFormField
import com.backbase.android.retail.journey.contacts.form.ContactFormScreenConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredText

object ContactsConfig {
    operator fun invoke(): ContactsConfiguration {
        return ContactsConfiguration {
            contactDetailsScreen = ContactDetailsScreenConfiguration {
                nameTitle = R.string.name_label_title.toDeferredText()
            }
            addContactScreen = contactFormScreenConfiguration()
            editContactScreen = contactFormScreenConfiguration(isAddContactScreen = false)
        }
    }

    private fun contactFormScreenConfiguration(
        isAddContactScreen: Boolean = true,
    ): ContactFormScreenConfiguration {
        return ContactFormScreenConfiguration {
            title = if (isAddContactScreen) {
                R.string.add_contact_title.toDeferredText()
            } else {
                R.string.edit_contact_title.toDeferredText()
            }
            nameLabel = R.string.name_label_title.toDeferredText()
            namePlaceholder = R.string.name_placeholder_text.toDeferredText()
            submitButtonText = R.string.save_button_text.toDeferredText()

            accountIdentifierFields = listOf(
                AccountNumberFormField {},
            )
        }
    }
}
