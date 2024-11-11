package com.westerra.release.btp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.westerra.release.R
import com.westerra.release.analytics.AnalyticsScreenNames.BTP_RESULT_FAILURE
import com.westerra.release.analytics.AnalyticsScreenNames.BTP_RESULT_SUCCESS
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_PRODUCT_KEY
import com.westerra.release.databinding.FragmentBtpResultBinding

class BtpResultFragment : Fragment() {
    companion object {
        private val TAG = BtpResultFragment::class.java.simpleName
        const val BTP_RESULT_TYPE = "btp_result_type"
        const val BTP_RESULT_FAILURE_MESSAGE = "btp_result_failure_message"
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentBtpResultBinding? = null
    val binding get() = _binding!!
    private var product: WesterraProduct? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        product = DataTransferCache().retrieve(id = BTP_FRAGMENTS_PRODUCT_KEY) as? WesterraProduct
    }

    override fun onResume() {
        super.onResume()
        if (isFailureResult()) {
            WesterraAnalytics.recordBtpScreenView(screenName = BTP_RESULT_FAILURE)
        } else {
            WesterraAnalytics.recordBtpScreenView(screenName = BTP_RESULT_SUCCESS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBtpResultBinding.inflate(inflater, container, false)
        binding.addMoreButton.setOnClickListener {
            goBackToProducts()
        }
        binding.ctaButton.setOnClickListener {
            goBack()
        }
        if (isFailureResult()) {
            renderFailure(message = arguments?.getString(BTP_RESULT_FAILURE_MESSAGE))
        } else {
            renderSuccess()
        }
        return binding.root
    }

    private fun isFailureResult(): Boolean {
        val resultType = arguments?.getString(BTP_RESULT_TYPE, "success")
        return resultType == "failure"
    }

    private fun renderSuccess() {
        context ?. let {
            binding.image.setImageDrawable(
                AppCompatResources.getDrawable(it, R.drawable.ic_success_check),
            )
        }
        binding.addMoreButton.visibility = View.VISIBLE
        binding.titleText.text = getString(R.string.success)
        binding.messageText.text = getString(R.string.account_created_message)
    }

    private fun renderFailure(message: String?) {
        context ?. let {
            binding.image.setImageDrawable(
                AppCompatResources.getDrawable(it, R.drawable.ic_payment_failed),
            )
        }
        binding.addMoreButton.visibility = View.GONE
        binding.titleText.text = getString(R.string.whoops)
        product ?. let {
            binding.messageText.text = message ?: getString(
                R.string.account_failure_message_formatted,
                it.getMinimumRequirementDisplayAmount(),
            )
        }
    }

    private fun goBack() {
        binding.ctaButton.isEnabled = false
        goBackHelper(7)
    }

    private fun goBackToProducts() {
        binding.ctaButton.isEnabled = false
        goBackHelper(6)
    }

    private fun goBackHelper(counter: Int) {
        binding.ctaButton.isEnabled = false
        for (i in 0..counter) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
