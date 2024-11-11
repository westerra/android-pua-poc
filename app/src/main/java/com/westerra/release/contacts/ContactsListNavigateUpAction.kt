package com.westerra.release.contacts

import androidx.navigation.NavController
import com.backbase.android.retail.journey.contacts.list.ContactListNavigateUpAction

class ContactsListNavigateUpAction(private val navController: NavController) :
    ContactListNavigateUpAction {
    override fun onNavigateUp() {
        navController.navigateUp()
    }
}
