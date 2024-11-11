package com.westerra.release.btp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.westerra.release.R
import com.westerra.release.analytics.AnalyticsScreenNames.BTP_PRODUCT_PICKER
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.btp.components.ProductCardInterface
import com.westerra.release.btp.components.ProductCardListAdapter
import com.westerra.release.btp.model.BtpProductsResponse
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.btp.model.WesterraProduct.Companion.FOOTER
import com.westerra.release.btp.model.WesterraProduct.Companion.SAVINGS_HEADER
import com.westerra.release.btp.model.WesterraProduct.Companion.SPENDING_HEADER
import com.westerra.release.btp.viewmodels.BtpProductPickerViewModel
import com.westerra.release.btp.viewmodels.BtpProductPickerViewModelFactory
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_PRODUCT_FILTER_KEY
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_PRODUCT_KEY
import com.westerra.release.databinding.FragmentBtpProductPickerBinding
import com.westerra.release.extensions.fadeGone
import com.westerra.release.extensions.fadeIn
import com.westerra.release.extensions.fadeInvisible
import com.westerra.release.sso.model.isError

class BtpProductPickerFragment :
    Fragment(),
    ProductCardInterface {
    companion object {
        private val TAG = BtpProductPickerFragment::class.java.simpleName
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentBtpProductPickerBinding? = null
    val binding get() = _binding!!

    private var filter: String? = null
    private var adapter: ProductCardListAdapter = ProductCardListAdapter(cardInterface = this)
    private val productPickerViewModel: BtpProductPickerViewModel by viewModels {
        BtpProductPickerViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        filter = DataTransferCache().retrieve(id = BTP_FRAGMENTS_PRODUCT_FILTER_KEY) as? String
    }

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordBtpScreenView(screenName = BTP_PRODUCT_PICKER)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBtpProductPickerBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            goBack()
        }
        binding.recycler.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false,
        )
        binding.recycler.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            onSwipeToRefresh()
        }
        binding.errorButton.setOnClickListener {
            binding.errorButton.isEnabled = false
            binding.errorButton.fadeInvisible()
            binding.errorProgress.fadeIn()
            reloadData()
        }
        binding.emptyButton.setOnClickListener {
            goBack()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            productPickerViewModel.productsResponse.observe(viewLifecycleOwner) {
                handleProductsResponse(productsResponse = it)
            }
        }
        onSwipeToRefresh()
        DataTransferCache().resetBtpDisclosuresState()
        return binding.root
    }

    private fun goBack() {
        binding.emptyButton.isEnabled = false
        binding.backButton.isEnabled = false
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    private fun onSwipeToRefresh() {
        binding.swipeRefresh.isRefreshing = true
        reloadData()
    }

    private fun reloadData() {
        productPickerViewModel.fetchProducts()
    }

    private fun handleProductsResponse(productsResponse: BtpProductsResponse) {
        binding.swipeRefresh.isRefreshing = false
        binding.errorButton.isEnabled = true
        productsResponse.profile ?. let {
            adapter.isAccountEligible = it.isEligible()
        }
        val products = productsResponse.products
        if (productsResponse.isError() || products == null) {
            renderErrorMessage()
        } else if (products.isEmpty()) {
            renderEmptyMessage()
        } else {
            renderProductsList(products = products)
        }
    }

    private fun renderProductsList(products: List<WesterraProduct>) {
        binding.swipeRefresh.fadeIn()
        binding.errorContainer.fadeGone()
        binding.emptyContainer.fadeGone()
        var hasSpendingHeader = false
        var hasSavingsHeader = false
        val finalProductsList = mutableListOf<WesterraProduct>()
        products.filter { it.isFilterType(filter) }.sorted().forEach {
            if (!hasSpendingHeader && it.isSpendingType()) {
                finalProductsList.add(SPENDING_HEADER)
                hasSpendingHeader = true
            } else if (!hasSavingsHeader && it.isSavingsType()) {
                finalProductsList.add(SAVINGS_HEADER)
                hasSavingsHeader = true
            }
            finalProductsList.add(it)
        }
        finalProductsList.add(FOOTER)
        adapter.submitList(finalProductsList)
        adapter.notifyDataSetChanged()
    }

    private fun renderErrorMessage() {
        binding.swipeRefresh.fadeGone()
        binding.errorContainer.fadeIn()
        binding.emptyContainer.fadeGone()
        binding.errorButton.fadeIn()
        binding.errorProgress.fadeGone()
    }

    private fun renderEmptyMessage() {
        binding.swipeRefresh.fadeGone()
        binding.errorContainer.fadeGone()
        binding.emptyContainer.fadeIn()
        adapter.submitList(listOf())
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onProductCardClicked(product: WesterraProduct, isAccountEligible: Boolean) {
        val productId = product.typeId
        if (productId.isNullOrEmpty()) {
            Log.w(TAG, "Unexpected missing product id.")
            return
        }
        if (isAccountEligible && product.isEligible()) {
            goToDisclosures(product = product)
        } else {
            context ?. let {
                val toastMessage = if (!product.isEligible()) {
                    product.getNotEligibleReason(context = it)
                } else {
                    it.getString(R.string.you_are_not_eligible)
                }
                Toast.makeText(it, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToDisclosures(product: WesterraProduct) {
        WesterraAnalytics.recordSelectProductEvent()
        DataTransferCache().save(BTP_FRAGMENTS_PRODUCT_KEY, product)
        findNavController().navigate(
            R.id.action_product_picker_to_disclosures,
            bundleOf(),
        )
    }
}
