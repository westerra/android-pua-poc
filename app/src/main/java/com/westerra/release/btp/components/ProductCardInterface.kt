package com.westerra.release.btp.components

import com.westerra.release.btp.model.WesterraProduct

interface ProductCardInterface {
    fun onProductCardClicked(product: WesterraProduct, isAccountEligible: Boolean)
}
