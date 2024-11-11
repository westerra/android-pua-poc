package com.westerra.release.contacts

import com.backbase.android.client.contactmanagerclient2.api.ContactsApi
import com.backbase.android.retail.journey.contacts.AddedContact
import com.backbase.android.retail.journey.contacts.Contact
import com.backbase.android.retail.journey.contacts.ContactsPage
import com.backbase.android.retail.journey.contacts.ContactsPageRequestParameters
import com.backbase.android.retail.journey.contacts.ContactsUseCase
import com.backbase.android.retail.journey.contacts.DeletedContact
import com.backbase.android.retail.journey.contacts.EditedContact
import com.backbase.android.retail.journey.contacts.UnsavedContact
import com.backbase.android.retail.journey.contacts.contactmanager_client_2.GenContactManagerClient2ContactsUseCase

class CustomContactsUseCase(
    contactsApi: ContactsApi,
) : ContactsUseCase {
    // default implementation of use case as of now, may need to do custom changes in this use case
    private val defaultContactsUseCase = GenContactManagerClient2ContactsUseCase(contactsApi)

    override suspend fun addContact(
        contact: UnsavedContact,
    ): ContactsUseCase.Result<AddedContact> {
        return defaultContactsUseCase.addContact(contact)
    }

    override suspend fun deleteContact(id: String): ContactsUseCase.Result<DeletedContact> {
        return defaultContactsUseCase.deleteContact(id)
    }

    override suspend fun editContact(
        id: String,
        contact: UnsavedContact,
    ): ContactsUseCase.Result<EditedContact> {
        return defaultContactsUseCase.editContact(id, contact)
    }

    override suspend fun getContact(id: String): ContactsUseCase.Result<Contact> {
        return defaultContactsUseCase.getContact(id)
    }

    override suspend fun getContactsPage(
        requestParameters: ContactsPageRequestParameters,
    ): ContactsUseCase.Result<ContactsPage> {
        return defaultContactsUseCase.getContactsPage(requestParameters)
    }
}
