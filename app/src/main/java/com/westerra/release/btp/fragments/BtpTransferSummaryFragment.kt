package com.westerra.release.btp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.westerra.release.R
import com.westerra.release.analytics.AnalyticsScreenNames.BTP_INTERNAL_TRANSFER_SUMMARY
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.btp.fragments.BtpResultFragment.Companion.BTP_RESULT_FAILURE_MESSAGE
import com.westerra.release.btp.model.BtpProductItem
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.btp.viewmodels.BtpTransferSummaryViewModel
import com.westerra.release.btp.viewmodels.BtpTransferSummaryViewModelFactory
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_FROM_ACCOUNT_KEY
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_PRODUCT_KEY
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_TRANSFER_AMOUNT_KEY
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_TRANSFER_NOTES_KEY
import com.westerra.release.databinding.FragmentBtpTransferSummaryBinding
import com.westerra.release.sso.model.getErrorMessage
import com.westerra.release.sso.model.isError
import java.text.NumberFormat
import java.util.Locale

class BtpTransferSummaryFragment : Fragment() {
    companion object {
        private val TAG = BtpTransferSummaryFragment::class.java.simpleName
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentBtpTransferSummaryBinding? = null
    val binding get() = _binding!!
    private var product: WesterraProduct? = null
    private var fromAccount: BtpProductItem? = null
    private var transferAmount: Float? = null
    private var transferNotes: String? = null

    private val transferSummaryViewModel: BtpTransferSummaryViewModel by viewModels {
        BtpTransferSummaryViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        product = DataTransferCache().retrieve(id = BTP_FRAGMENTS_PRODUCT_KEY) as? WesterraProduct
        fromAccount = DataTransferCache().retrieve(
            id = BTP_FRAGMENTS_FROM_ACCOUNT_KEY,
        ) as? BtpProductItem
        transferAmount = DataTransferCache().retrieve(
            id = BTP_FRAGMENTS_TRANSFER_AMOUNT_KEY,
        ) as? Float
        transferNotes = DataTransferCache().retrieve(
            id = BTP_FRAGMENTS_TRANSFER_NOTES_KEY,
        ) as? String
    }

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordBtpScreenView(screenName = BTP_INTERNAL_TRANSFER_SUMMARY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBtpTransferSummaryBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            goBack()
        }
        binding.ctaButton.setOnClickListener {
            onCtaClicked()
        }
        transferSummaryViewModel.addProductResponse.observe(viewLifecycleOwner) {
            binding.ctaButton.loading = false
            if (it.isError()) {
                goToFailureScreen(errorMessage = it.getErrorMessage())
            } else {
                goToSuccessScreen()
            }
        }
        fromAccount ?. let { fromAccount ->
            renderTransferAccount(fromAccount = fromAccount)
        }
        product ?. let { product ->
            renderProduct(product = product)
        }
        transferAmount ?. let { transferAmount ->
            renderTransferAmount(amount = transferAmount)
        }
        renderTransferNotes()
        return binding.root
    }

    private fun renderTransferNotes() {
        val notes = transferNotes
        if (notes.isNullOrEmpty()) {
            binding.notesTitleText.visibility = View.GONE
            binding.notesCard.visibility = View.GONE
        } else {
            binding.notesTitleText.visibility = View.VISIBLE
            binding.notesCard.visibility = View.VISIBLE
            binding.notesText.text = notes
        }
    }

    private fun renderTransferAccount(fromAccount: BtpProductItem) {
        context ?. let { context ->
            val fromText = String.format(
                Locale.getDefault(),
                context.getString(R.string.from_account_formatted),
                fromAccount.displayName,
            )
            binding.fromAccountNameText.text = fromText
            binding.fromAccountNumberText.text = fromAccount.BBAN
        }
    }

    private fun renderProduct(product: WesterraProduct) {
        context ?. let { context ->
            val toText = String.format(
                Locale.getDefault(),
                context.getString(R.string.to_account_formatted),
                product.getDisplayName(),
            )
            binding.toAccountNameText.text = toText
        }
    }

    private fun renderTransferAmount(amount: Float) {
        binding.amountText.text = NumberFormat.getCurrencyInstance().format(amount)
    }

    private fun onCtaClicked() {
        product ?. let { product ->
            fromAccount ?. let { fromAccount ->
                transferAmount ?. let { transferAmount ->
                    binding.ctaButton.loading = true
                    binding.ctaButton.isEnabled = false
                    transferSummaryViewModel.addProduct(
                        product = product,
                        fromAccount = fromAccount,
                        transferAmount = transferAmount,
                        transferNotes = transferNotes,
                    )
                }
            }
        }
    }

    private fun goToSuccessScreen() {
        val bundle = bundleOf(BtpResultFragment.BTP_RESULT_TYPE to "success")
        findNavController().navigate(
            R.id.action_transfer_summary_to_btp_result,
            bundle,
        )
    }

    private fun goToFailureScreen(errorMessage: String?) {
        val bundle = bundleOf(
            BtpResultFragment.BTP_RESULT_TYPE to "failure",
        )
        errorMessage ?. let {
            bundle.putString(BTP_RESULT_FAILURE_MESSAGE, it)
        }
        findNavController().navigate(
            R.id.action_transfer_summary_to_btp_result,
            bundle,
        )
    }

    private fun goBack() {
        binding.ctaButton.isEnabled = false
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
