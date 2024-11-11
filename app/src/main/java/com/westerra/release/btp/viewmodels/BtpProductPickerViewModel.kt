package com.westerra.release.btp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.westerra.release.btp.model.BtpProductsResponse
import com.westerra.release.btp.network.BtpProductsClient
import com.westerra.release.sso.model.isError
import kotlinx.coroutines.launch

class BtpProductPickerViewModel : ViewModel() {

    private val productsClient = BtpProductsClient()

    private val _productsResponse: MutableLiveData<BtpProductsResponse> =
        MutableLiveData<BtpProductsResponse>()

    val productsResponse: LiveData<BtpProductsResponse>
        get() = _productsResponse

    fun fetchProducts() {
        viewModelScope.launch {
            val response = productsClient.fetchProducts()
            if (response.isError() || response.products == null) {
                val errorMessage = response.errorMessage ?: "Unexpected response"
                _productsResponse.postValue(BtpProductsResponse(errorMessage = errorMessage))
            } else {
                _productsResponse.postValue(
                    BtpProductsResponse(
                        products = response.products,
                        errorMessage = null,
                    ),
                )
            }
        }
    }
}
