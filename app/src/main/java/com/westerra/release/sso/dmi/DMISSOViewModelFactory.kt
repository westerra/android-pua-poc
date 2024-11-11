package com.westerra.release.sso.dmi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DMISSOViewModelFactory(
    private val internalArrangementsId: String,
    private val isPayments: Boolean,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = DMISSOViewModel(
        internalArrangementsId = internalArrangementsId,
        isPayments = isPayments,
    ) as T
}
