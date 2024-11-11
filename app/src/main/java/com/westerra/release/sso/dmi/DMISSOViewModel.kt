package com.westerra.release.sso.dmi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.westerra.release.sso.model.DMIResponse
import com.westerra.release.sso.model.isError
import com.westerra.release.sso.network.MTGSVCClient
import com.westerra.release.sso.network.SSOClient
import kotlinx.coroutines.launch

class DMISSOViewModel(internalArrangementsId: String, isPayments: Boolean) : ViewModel() {
    companion object {
        private const val SSO_CLIENT_API_URL =
            "/api/sso/client-api/v1/applications?name=dmi&internalArrangementId="
    }

    private val dmiSSOClient = SSOClient(clientApiUrl = SSO_CLIENT_API_URL + internalArrangementsId)
    private val mtgsvcClient = MTGSVCClient()

    private val _dmiResponse: MutableLiveData<DMIResponse> =
        MutableLiveData<DMIResponse>()

    init {
        viewModelScope.launch {
            val ssoTokenResponse = dmiSSOClient.fetchSSOToken()
            if (ssoTokenResponse.isError() || ssoTokenResponse.ssoUrl.isEmpty()) {
                val errorMessage = ssoTokenResponse.errorMessage ?: "Unexpected response"
                _dmiResponse.postValue(DMIResponse(errorMessage = errorMessage))
            } else {
                val dmiResponse = mtgsvcClient.postLogin(
                    ssoToken = ssoTokenResponse.ssoUrl,
                    isPayments = isPayments,
                )
                val location = dmiResponse.location
                val result = if (dmiResponse.isError() || location == null) {
                    val errorMessage = dmiResponse.errorMessage ?: "Unexpected response"
                    DMIResponse(errorMessage = errorMessage)
                } else {
                    dmiResponse
                }
                _dmiResponse.postValue(result)
            }
        }
    }

    val dmiResponse: LiveData<DMIResponse>
        get() = _dmiResponse
}
