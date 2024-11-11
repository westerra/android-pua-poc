package com.westerra.release.btp.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.westerra.release.R
import com.westerra.release.analytics.AnalyticsScreenNames.BTP_INTERNAL_TRANSFER_DETAILS
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.btp.components.BtpAmountInputInterface
import com.westerra.release.btp.components.BtpAmountInputTextWatcher
import com.westerra.release.btp.model.BtpProductItem
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_FROM_ACCOUNT_KEY
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_PRODUCT_KEY
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_TRANSFER_AMOUNT_KEY
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_TRANSFER_NOTES_KEY
import com.westerra.release.databinding.FragmentBtpTransferDetailsBinding
import com.westerra.release.extensions.fadeGone
import com.westerra.release.extensions.fadeIn
import com.westerra.release.extensions.getAmount
import com.westerra.release.extensions.hideKeyboardFrom
import java.util.Locale

class BtpTransferDetailsFragment :
    Fragment(),
    BtpAmountInputInterface {
    companion object {
        private val TAG = BtpTransferDetailsFragment::class.java.simpleName
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentBtpTransferDetailsBinding? = null
    val binding get() = _binding!!
    private var product: WesterraProduct? = null
    private var fromAccount: BtpProductItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        product = DataTransferCache().retrieve(id = BTP_FRAGMENTS_PRODUCT_KEY) as? WesterraProduct
        fromAccount = DataTransferCache().retrieve(
            id = BTP_FRAGMENTS_FROM_ACCOUNT_KEY,
        ) as? BtpProductItem
    }

    private fun hideKeyboard() {
        hideKeyboardFrom(context = context, view = binding.backButton)
        binding.amountEditText.clearFocus()
        binding.notesEditText.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordBtpScreenView(screenName = BTP_INTERNAL_TRANSFER_DETAILS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBtpTransferDetailsBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            hideKeyboard()
            goBack()
        }
        binding.ctaButton.setOnClickListener {
            hideKeyboard()
            if (validateInput(hasFocus = false)) {
                goToTransferSummary()
            }
        }
        binding.mainContainer.setOnClickListener {
            hideKeyboard()
        }
        binding.appBar.setOnClickListener {
            hideKeyboard()
        }
        binding.toolbar.setOnClickListener {
            hideKeyboard()
        }
        binding.amountEditText.setOnFocusChangeListener { _, hasFocus ->
            validateInput(hasFocus = hasFocus)
        }
        binding.amountEditText.addTextChangedListener(
            BtpAmountInputTextWatcher(
                editText = binding.amountEditText,
                amountInputInterface = this,
            ),
        )
        binding.notesEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                context ?. let { context ->
                    v.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.primary),
                    )
                }
            } else {
                context ?. let { context ->
                    v.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.light_grey),
                    )
                }
            }
        }
        renderProduct()
        return binding.root
    }

    private fun renderProduct() {
        product ?. let { product ->
            if (product.hasMinimumAmountRequirement()) {
                binding.ctaButton.isEnabled = false
            }
        }
    }

    private fun getTransferAmount(): Float {
        return binding.amountEditText.getAmount().toFloat()
    }

    private fun validateInput(hasFocus: Boolean): Boolean {
        if (isValidInput()) {
            renderValidInput(hasFocus = hasFocus)
            return true
        } else {
            renderInvalidInput()
            return false
        }
    }

    private fun isValidInput(): Boolean {
        product ?. let { product ->
            return getTransferAmount() >= product.getMinimumRequirementAmount()
        }
        return false
    }

    private fun renderValidInput(hasFocus: Boolean) {
        binding.ctaButton.isEnabled = true
        binding.requirementsText.fadeGone()
        renderAmountEditTextValid(hasFocus = hasFocus)
    }

    private fun renderInvalidInput() {
        binding.ctaButton.isEnabled = false
        binding.requirementsText.fadeIn()
        val message = String.format(
            Locale.getDefault(),
            getString(R.string.minimum_balance_requirement_formatted),
            product?.getMinimumRequirementDisplayAmount(),
        )
        binding.requirementsText.text = message
        binding.amountEditText.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context ?: return, R.color.primary),
        )
    }

    private fun renderAmountEditTextValid(hasFocus: Boolean) {
        if (hasFocus) {
            binding.amountEditText.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context ?: return, R.color.editTextFocusUnderline),
            )
        } else {
            binding.amountEditText.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context ?: return, R.color.textColorPrimary),
            )
        }
    }

    private fun goToTransferSummary() {
        product ?. let { product ->
            fromAccount ?. let { fromAccount ->
                WesterraAnalytics.recordFundInitiatedEvent()
                DataTransferCache().save(BTP_FRAGMENTS_PRODUCT_KEY, product)
                DataTransferCache().save(BTP_FRAGMENTS_FROM_ACCOUNT_KEY, fromAccount)
                DataTransferCache().save(BTP_FRAGMENTS_TRANSFER_AMOUNT_KEY, getTransferAmount())
                binding.notesEditText.text?.toString() ?. let { notes ->
                    DataTransferCache().save(BTP_FRAGMENTS_TRANSFER_NOTES_KEY, notes)
                }
                findNavController().navigate(
                    R.id.action_transfer_details_to_transfer_summary,
                    bundleOf(),
                )
            }
        }
    }

    private fun goBack() {
        binding.ctaButton.isEnabled = false
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun doValidation() {
        validateInput(hasFocus = true)
    }
}
