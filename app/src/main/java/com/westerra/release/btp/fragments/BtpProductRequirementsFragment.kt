package com.westerra.release.btp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.westerra.release.R
import com.westerra.release.analytics.AnalyticsScreenNames.BTP_REQUIREMENTS
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_PRODUCT_KEY
import com.westerra.release.custom.MySharedPreferences
import com.westerra.release.databinding.FragmentBtpProductRequirementsBinding
import java.util.Locale

class BtpProductRequirementsFragment : Fragment() {
    companion object {
        private val TAG = BtpProductRequirementsFragment::class.java.simpleName
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentBtpProductRequirementsBinding? = null
    val binding get() = _binding!!
    private var product: WesterraProduct? = null
    private var memberNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        product = DataTransferCache().retrieve(id = BTP_FRAGMENTS_PRODUCT_KEY) as? WesterraProduct
        memberNumber = MySharedPreferences.getMemberNumber()
    }

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordBtpScreenView(screenName = BTP_REQUIREMENTS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBtpProductRequirementsBinding.inflate(
            inflater,
            container,
            false,
        )
        binding.backButton.setOnClickListener {
            goBack()
        }
        binding.ctaButton.setOnClickListener {
            goToAccountPicker()
        }
        product ?. let {
            renderProduct(product = it)
        }
        val context = context
        if (memberNumber.isNullOrBlank() || context == null) {
            binding.memberNumberText.visibility = View.GONE
        } else {
            binding.memberNumberText.visibility = View.VISIBLE
            val message = String.format(
                Locale.getDefault(),
                context.getString(R.string.new_account_member_number_formatted),
                memberNumber,
            )
            binding.memberNumberText.text = message
        }
        return binding.root
    }

    private fun renderProduct(product: WesterraProduct) {
        if (product.hasMinimumAmountRequirement()) {
            context ?. let {
                val message = String.format(
                    Locale.getDefault(),
                    it.getString(R.string.minimum_balance_requirement_formatted),
                    product.getMinimumRequirementDisplayAmount(),
                )
                binding.minimumBalanceRequirementText.text = message
            }
        }
    }

    private fun goToAccountPicker() {
        product ?. let { product ->
            WesterraAnalytics.recordFundingInfoReadEvent()
            DataTransferCache().save(BTP_FRAGMENTS_PRODUCT_KEY, product)
            findNavController().navigate(
                R.id.action_requirements_to_account_picker,
                bundleOf(),
            )
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
}
