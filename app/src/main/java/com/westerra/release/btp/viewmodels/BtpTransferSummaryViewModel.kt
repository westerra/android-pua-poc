package com.westerra.release.btp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.btp.model.AddProductResponse
import com.westerra.release.btp.model.BtpProductItem
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.btp.network.BtpProductsClient
import com.westerra.release.sso.model.isError
import kotlinx.coroutines.launch

class BtpTransferSummaryViewModel : ViewModel() {

    private val productsClient = BtpProductsClient()

    private val _addProductResponse: MutableLiveData<AddProductResponse> =
        MutableLiveData<AddProductResponse>()

    val addProductResponse: LiveData<AddProductResponse>
        get() = _addProductResponse

    fun addProduct(
        product: WesterraProduct,
        fromAccount: BtpProductItem,
        transferAmount: Number,
        transferNotes: String?,
    ) {
        viewModelScope.launch {
            WesterraAnalytics.recordFundingInfoReadEvent()
            val response = productsClient.postAddProduct(
                product = product,
                fromAccount = fromAccount,
                transferAmount = transferAmount,
                transferNotes = transferNotes,
            )
            if (response.isError()) {
                WesterraAnalytics.recordAccountCreationFailedEvent()
            } else {
                WesterraAnalytics.recordBtpAccountCreatedEvent()
            }
            _addProductResponse.postValue(response)
        }
    }
}
