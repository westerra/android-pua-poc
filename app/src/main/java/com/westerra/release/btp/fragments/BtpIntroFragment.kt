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
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_PRODUCT_FILTER_KEY
import com.westerra.release.databinding.FragmentBtpIntroBinding

class BtpIntroFragment : Fragment() {
    companion object {
        private val TAG = BtpIntroFragment::class.java.simpleName
        const val SPENDING_FILTER = "Spending"
        const val SAVINGS_FILTER = "Savings"
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentBtpIntroBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBtpIntroBinding.inflate(
            inflater,
            container,
            false,
        )
        binding.backButton.setOnClickListener {
            goBack()
        }
        binding.spendingClicker.setOnClickListener {
            goToProductPicker(SPENDING_FILTER)
        }
        binding.savingsClicker.setOnClickListener {
            goToProductPicker(SAVINGS_FILTER)
        }
        return binding.root
    }

    private fun goToProductPicker(filter: String) {
        WesterraAnalytics.recordSelectProductCategoryEvent(category = filter)
        DataTransferCache().save(BTP_FRAGMENTS_PRODUCT_FILTER_KEY, filter)
        findNavController().navigate(
            R.id.action_intro_to_product_picker,
            bundleOf(),
        )
    }

    private fun goBack() {
        binding.spendingClicker.isEnabled = false
        binding.savingsClicker.isEnabled = false
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
