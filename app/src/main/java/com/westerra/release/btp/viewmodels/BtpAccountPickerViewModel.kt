package com.westerra.release.btp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.westerra.release.btp.model.BtpAccountsResponse
import com.westerra.release.btp.network.BtpAccountsClient
import com.westerra.release.sso.model.isError
import kotlinx.coroutines.launch

class BtpAccountPickerViewModel : ViewModel() {

    private val accountsClient = BtpAccountsClient()

    private val _accountsResponse: MutableLiveData<BtpAccountsResponse> =
        MutableLiveData<BtpAccountsResponse>()

    val accountsResponse: LiveData<BtpAccountsResponse>
        get() = _accountsResponse

    fun fetchAccounts() {
        viewModelScope.launch {
            val response = accountsClient.fetchBtpAccounts()
            if (response.isError()) {
                val errorMessage = response.errorMessage ?: "Unexpected response"
                _accountsResponse.postValue(BtpAccountsResponse(errorMessage = errorMessage))
            } else {
                _accountsResponse.postValue(response)
            }
        }
    }
}
