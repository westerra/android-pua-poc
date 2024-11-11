package com.westerra.release.sso.payveris

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.westerra.release.sso.model.SSOApplicationResponse
import com.westerra.release.sso.network.SSOClient
import kotlinx.coroutines.launch

class PayverisSSOViewModel : ViewModel() {
    companion object {
        private const val SSO_CLIENT_API_URL =
            "/api/sso/client-api/v1/applications?name=payveris&autoEnroll=true"
    }

    private val payverisSSOClient = SSOClient(clientApiUrl = SSO_CLIENT_API_URL)

    private val _payverisSSOToken: MutableLiveData<SSOApplicationResponse> =
        MutableLiveData<SSOApplicationResponse>()

    init {
        viewModelScope.launch {
            _payverisSSOToken.postValue(payverisSSOClient.fetchSSOToken())
        }
    }

    val payverisSSOToken: LiveData<SSOApplicationResponse>
        get() = _payverisSSOToken
}
