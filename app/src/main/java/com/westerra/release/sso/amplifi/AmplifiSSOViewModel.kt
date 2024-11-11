package com.westerra.release.sso.amplifi

import androidx.lifecycle.ViewModel
import com.westerra.release.sso.model.SSOApplicationResponse
import com.westerra.release.sso.network.SSOClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AmplifiSSOViewModel : ViewModel() {

    companion object {
        private const val SSO_CLIENT_API_URL =
            "/api/sso/client-api/v1/applications?name=amplify&program=cashback"
    }

    private val amplifiSSOClient = SSOClient(clientApiUrl = SSO_CLIENT_API_URL)

    suspend fun amplifiSSOToken(): Flow<SSOApplicationResponse> = flow {
        emit(amplifiSSOClient.fetchSSOToken())
    }
}
