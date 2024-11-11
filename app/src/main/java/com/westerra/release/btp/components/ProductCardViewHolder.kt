package com.westerra.release.btp.components

import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.btp.model.ProductCardType
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.btp.network.ProductsWebViewClient
import com.westerra.release.databinding.CardWesterraProductBinding
import com.westerra.release.extensions.applyDefaultSettings
import com.westerra.release.extensions.fadeIn

class ProductCardViewHolder(
    private val binding: CardWesterraProductBinding,
    private val cardInterface: ProductCardInterface,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private val TAG = ProductCardViewHolder::class.java.simpleName
    }

    private val webViewClient = ProductsWebViewClient(productCard = binding)
    private val webChromeClient = WebChromeClient()

    fun bind(product: WesterraProduct, isAccountEligible: Boolean) {
        with(binding) {
            executePendingBindings()
            Log.d(TAG, "binding to: $product")
            render(product = product, isAccountEligible = isAccountEligible)
        }
    }

    private fun render(product: WesterraProduct, isAccountEligible: Boolean) {
        when (product.category) {
            ProductCardType.HEADER -> {
                renderHeader(product = product)
            }
            ProductCardType.FOOTER -> {
                renderFooter()
            }
            else -> {
                renderCard(product = product, isAccountEligible)
            }
        }
    }

    private fun renderHeader(product: WesterraProduct) {
        binding.cardContainer.visibility = View.GONE
        binding.headerContainer.fadeIn()
        binding.footerContainer.visibility = View.GONE
        binding.headerText.text = product.getHeaderText()
    }

    private fun renderFooter() {
        binding.cardContainer.visibility = View.GONE
        binding.headerContainer.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.footerContainer.fadeIn()
            },
            2000,
        )
    }

    private fun renderCard(product: WesterraProduct, isAccountEligible: Boolean) {
        binding.headerContainer.visibility = View.GONE
        binding.footerContainer.visibility = View.GONE
        binding.moreInfoButton.setOnClickListener {
            onInfoClick(product = product)
        }
        binding.ctaButton.setOnClickListener {
            onCtaClick(product = product, isAccountEligible = isAccountEligible)
        }
        binding.webView.applyDefaultSettings()
        binding.webView.isVerticalScrollBarEnabled = false
        binding.webView.webViewClient = webViewClient
        binding.webView.webChromeClient = webChromeClient
        binding.webView.loadUrl(product.getProductMarketingMarketingUrl())
        WesterraApplication.getInstance().getActivity() ?. let {
            val res = if (isAccountEligible && product.isEligible()) {
                R.color.submitButtonBackgroundColor
            } else {
                R.color.disabledSubmitButton
            }
            binding.ctaButton.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(it, res),
            )
        }
    }

    private fun onCtaClick(product: WesterraProduct, isAccountEligible: Boolean) {
        WesterraApplication.getInstance().getActivity() ?. let {
            binding.ctaButton.isEnabled = false
            cardInterface.onProductCardClicked(
                product = product,
                isAccountEligible = isAccountEligible,
            )
            binding.ctaButton.isEnabled = true
        }
    }

    private fun onInfoClick(product: WesterraProduct) {
        binding.moreInfoButton.isEnabled = false
        WesterraApplication.getInstance().launchExternalBrowser(url = product.getMoreInfoUrl())
        binding.moreInfoButton.isEnabled = true
    }
}
