package com.westerra.release.btp.components

import com.westerra.release.btp.model.BtpProductItem

interface BtpAccountCardInterface {
    fun onCardClicked(product: BtpProductItem)
}
