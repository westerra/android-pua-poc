package com.westerra.release.btp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.westerra.release.R
import com.westerra.release.analytics.AnalyticsScreenNames.BTP_INTERNAL_TRANSFER_ACCOUNT_PICKER
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.btp.components.BtpAccountCardInterface
import com.westerra.release.btp.components.BtpAccountCardListAdapter
import com.westerra.release.btp.model.BtpAccountsResponse
import com.westerra.release.btp.model.BtpProductItem
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.btp.viewmodels.BtpAccountPickerViewModel
import com.westerra.release.btp.viewmodels.BtpAccountPickerViewModelFactory
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_FROM_ACCOUNT_KEY
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_PRODUCT_KEY
import com.westerra.release.databinding.FragmentBtpAccountPickerBinding
import com.westerra.release.extensions.fadeGone
import com.westerra.release.extensions.fadeIn
import com.westerra.release.extensions.fadeInvisible
import com.westerra.release.sso.model.isError

class BtpAccountPickerFragment : BtpAccountCardInterface, Fragment() {
    companion object {
        private val TAG = BtpAccountPickerFragment::class.java.simpleName
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentBtpAccountPickerBinding? = null
    val binding get() = _binding!!
    private var product: WesterraProduct? = null

    private var adapter: BtpAccountCardListAdapter = BtpAccountCardListAdapter(cardInterface = this)
    private val accountsPickerViewModel: BtpAccountPickerViewModel by viewModels {
        BtpAccountPickerViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        product = DataTransferCache().retrieve(id = BTP_FRAGMENTS_PRODUCT_KEY) as? WesterraProduct
    }

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordBtpScreenView(screenName = BTP_INTERNAL_TRANSFER_ACCOUNT_PICKER)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBtpAccountPickerBinding.inflate(inflater, container, false)
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
            accountsPickerViewModel.accountsResponse.observe(viewLifecycleOwner) {
                handleAccountsResponse(accountsResponse = it)
            }
        }
        onSwipeToRefresh()
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
        accountsPickerViewModel.fetchAccounts()
    }

    private fun handleAccountsResponse(accountsResponse: BtpAccountsResponse) {
        binding.swipeRefresh.isRefreshing = false
        binding.errorButton.isEnabled = true
        val accounts = accountsResponse.getInternalTransferAccounts()
        if (accountsResponse.isError()) {
            renderErrorMessage()
        } else if (accounts.isEmpty()) {
            renderEmptyMessage()
        } else {
            renderAccountsList(accounts = accounts)
        }
    }

    private fun renderErrorMessage() {
        binding.resultsContainer.fadeGone()
        binding.errorContainer.fadeIn()
        binding.emptyContainer.fadeGone()
        binding.errorButton.fadeIn()
        binding.errorProgress.fadeGone()
    }

    private fun renderEmptyMessage() {
        binding.resultsContainer.fadeGone()
        binding.errorContainer.fadeGone()
        binding.emptyContainer.fadeIn()
        adapter.submitList(listOf())
        adapter.notifyDataSetChanged()
    }

    private fun renderAccountsList(accounts: List<BtpProductItem>) {
        binding.resultsContainer.fadeIn()
        binding.errorContainer.fadeGone()
        binding.emptyContainer.fadeGone()
        adapter.submitList(accounts)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCardClicked(product: BtpProductItem) {
        goToTransferDetails(fromAccount = product)
    }

    private fun goToTransferDetails(fromAccount: BtpProductItem) {
        product ?. let {
            DataTransferCache().save(BTP_FRAGMENTS_PRODUCT_KEY, it)
            DataTransferCache().save(BTP_FRAGMENTS_FROM_ACCOUNT_KEY, fromAccount)
            findNavController().navigate(
                R.id.action_account_picker_to_transfer_details,
                bundleOf(),
            )
        }
    }
}
