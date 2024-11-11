package com.westerra.release.rdc

import androidx.navigation.NavController
import com.backbase.android.retail.journey.rdc.ExitNavigateAction

class RDCExitNavigationAction(private val navController: NavController) : ExitNavigateAction {
    override fun navigate() {
        navController.navigateUp()
    }
}
