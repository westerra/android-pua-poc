package com.westerra.release.btp.fragments

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.analytics.AnalyticsScreenNames.BTP_DISCLOSURES
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.custom.DataTransferCache
import com.westerra.release.custom.DataTransferCacheKeys.BTP_DISCLOSURES_IRS_WITHOLDING_SELECTED
import com.westerra.release.custom.DataTransferCacheKeys.BTP_DISCLOSURES_TERMS_SELECTED
import com.westerra.release.custom.DataTransferCacheKeys.BTP_DISCLOSURES_WEGOTYA_SELECTED
import com.westerra.release.custom.DataTransferCacheKeys.BTP_FRAGMENTS_PRODUCT_KEY
import com.westerra.release.databinding.FragmentBtpDisclosuresBinding

class BtpDisclosuresFragment : Fragment() {
    companion object {
        private val TAG = BtpDisclosuresFragment::class.java.simpleName
        private const val ACCOUNT_AGREEMENT_URL =
            "https://www.westerracu.com/docs/Account_Agreement.pdf"
        private const val SAVINGS_RATE_URL =
            "https://www.westerracu.com/resources/rates?q=personal-savings-and-checking-rates"
        private const val FEE_SCHEDULE_URL = "https://www.westerracu.com/feeschedule"
        private const val ELECTRONIC_DISCLOSURES_URL =
            "https://www.westerracu.com/resources/disclosures?q=electronic-disclosure-and-consent"
        private const val WEGOTYA_DISCLOSURES_URL = "https://www.westerracu.com/wegotyadisclosure"
    }

    // This property is only valid between onCreateView and onDestroyView
    private var _binding: FragmentBtpDisclosuresBinding? = null
    val binding get() = _binding!!
    private var product: WesterraProduct? = null
    private var isTermsSelected = false
    private var isIrsWithholdingSelected = false
    private var isWeGotYaSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        product = DataTransferCache().retrieve(id = BTP_FRAGMENTS_PRODUCT_KEY) as? WesterraProduct
    }

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordBtpScreenView(screenName = BTP_DISCLOSURES)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBtpDisclosuresBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            goBack()
        }
        binding.ctaButton.setOnClickListener {
            goToRequirements()
        }
        binding.accountAgreementText.text = formatLinkText(
            input = binding.accountAgreementText.text.toString(),
        )
        binding.accountAgreementLayout.setOnClickListener {
            goToAccountAgreement()
        }
        binding.savingsRateText.text = formatLinkText(
            input = binding.savingsRateText.text.toString(),
        )
        binding.savingsRateLayout.setOnClickListener {
            goToSavingsRate()
        }
        binding.feeScheduleText.text = formatLinkText(
            input = binding.feeScheduleText.text.toString(),
        )
        binding.feeScheduleLayout.setOnClickListener {
            goToFeeSchedule()
        }
        binding.electronicDisclosuresText.text = formatLinkText(
            input = binding.electronicDisclosuresText.text.toString(),
        )
        binding.electronicDisclosuresLayout.setOnClickListener {
            goToElectronicDisclosures()
        }
        binding.wegotyaDisclosureText.text = formatLinkText(
            input = binding.wegotyaDisclosureText.text.toString(),
        )
        binding.wegotyaDisclosureLayout.setOnClickListener {
            goToWeGotYaDisclosures()
        }
        binding.termsConditionsCheckboxText.text = formatCheckboxText(
            input = binding.termsConditionsCheckboxText.text.toString(),
        )
        binding.termsConditionsCheckboxLayout.setOnClickListener {
            onTermsClicked()
        }
        binding.irsWithholdingCheckboxText.text = formatCheckboxText(
            input = binding.irsWithholdingCheckboxText.text.toString(),
        )
        binding.irsWithholdingCheckboxLayout.setOnClickListener {
            onIrsWithholdingClicked()
        }
        binding.wegotyaCheckboxText.text = formatCheckboxText(
            input = binding.wegotyaCheckboxText.text.toString(),
        )
        binding.wegotyaCheckboxLayout.setOnClickListener {
            onWeGotYaClicked()
        }
        product ?. let {
            renderProduct(product = it)
        }
        isTermsSelected = DataTransferCache().retrieve(
            id = BTP_DISCLOSURES_TERMS_SELECTED,
        ) as? Boolean ?: false
        renderTerms()
        isIrsWithholdingSelected = DataTransferCache().retrieve(
            id = BTP_DISCLOSURES_IRS_WITHOLDING_SELECTED,
        ) as? Boolean ?: false
        renderIrsWithholding()
        isWeGotYaSelected = DataTransferCache().retrieve(
            id = BTP_DISCLOSURES_WEGOTYA_SELECTED,
        ) as? Boolean ?: false
        renderWeGotYa()
        updateButtonState()
        return binding.root
    }

    private fun renderTerms() {
        context ?. let { context ->
            if (isTermsSelected) {
                binding.termsConditionsCheckoutImage.setImageDrawable(
                    AppCompatResources.getDrawable(context, R.drawable.ic_checkbox_filled),
                )
                binding.termsConditionsCheckoutImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.primary),
                )
            } else {
                binding.termsConditionsCheckoutImage.setImageDrawable(
                    AppCompatResources.getDrawable(context, R.drawable.ic_square_outline),
                )
                binding.termsConditionsCheckoutImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.textColorPrimary),
                )
            }
        }
    }

    private fun onTermsClicked() {
        isTermsSelected = !isTermsSelected
        renderTerms()
        updateButtonState()
    }

    private fun renderIrsWithholding() {
        context ?. let { context ->
            if (isIrsWithholdingSelected) {
                binding.irsWithholdingCheckoutImage.setImageDrawable(
                    AppCompatResources.getDrawable(context, R.drawable.ic_checkbox_filled),
                )
                binding.irsWithholdingCheckoutImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.primary),
                )
            } else {
                binding.irsWithholdingCheckoutImage.setImageDrawable(
                    AppCompatResources.getDrawable(context, R.drawable.ic_square_outline),
                )
                binding.irsWithholdingCheckoutImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.textColorPrimary),
                )
            }
        }
    }

    private fun onIrsWithholdingClicked() {
        isIrsWithholdingSelected = !isIrsWithholdingSelected
        renderIrsWithholding()
        updateButtonState()
    }

    private fun renderWeGotYa() {
        context ?. let { context ->
            if (isWeGotYaSelected) {
                binding.wegotyaCheckoutImage.setImageDrawable(
                    AppCompatResources.getDrawable(context, R.drawable.ic_checkbox_filled),
                )
                binding.wegotyaCheckoutImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.primary),
                )
            } else {
                binding.wegotyaCheckoutImage.setImageDrawable(
                    AppCompatResources.getDrawable(context, R.drawable.ic_square_outline),
                )
                binding.wegotyaCheckoutImage.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.textColorPrimary),
                )
            }
        }
    }

    private fun onWeGotYaClicked() {
        isWeGotYaSelected = !isWeGotYaSelected
        renderWeGotYa()
        updateButtonState()
    }

    private fun updateButtonState() {
        var isEnabled = isTermsSelected && isIrsWithholdingSelected
        if (product?.isSpendingType() == true) {
            isEnabled = isEnabled && isWeGotYaSelected
        }
        binding.ctaButton.isEnabled = isEnabled
        DataTransferCache().save(id = BTP_DISCLOSURES_TERMS_SELECTED, obj = isTermsSelected)
        DataTransferCache().save(
            id = BTP_DISCLOSURES_IRS_WITHOLDING_SELECTED,
            obj = isIrsWithholdingSelected,
        )
        DataTransferCache().save(id = BTP_DISCLOSURES_WEGOTYA_SELECTED, obj = isWeGotYaSelected)
    }

    private fun renderProduct(product: WesterraProduct) {
        if (product.isSpendingType()) {
            binding.wegotyaContainer.visibility = View.VISIBLE
        } else {
            binding.wegotyaContainer.visibility = View.GONE
        }
    }

    private fun formatLinkText(input: String): SpannableString {
        val result = SpannableString(input)
        result.setSpan(UnderlineSpan(), 0, input.length, 0)
        result.setSpan(StyleSpan(Typeface.ITALIC), 0, input.length, 0)
        return result
    }

    private fun formatCheckboxText(input: String): SpannableString {
        val result = SpannableString(input)
        result.setSpan(StyleSpan(Typeface.ITALIC), 0, input.length, 0)
        return result
    }

    private fun goToAccountAgreement() {
        binding.accountAgreementLayout.isEnabled = false
        goToPdf(url = ACCOUNT_AGREEMENT_URL)
        binding.accountAgreementLayout.isEnabled = true
    }

    private fun goToSavingsRate() {
        binding.savingsRateLayout.isEnabled = false
        WesterraApplication.getInstance().launchExternalBrowser(url = SAVINGS_RATE_URL)
        binding.savingsRateLayout.isEnabled = true
    }

    private fun goToFeeSchedule() {
        binding.feeScheduleLayout.isEnabled = false
        WesterraApplication.getInstance().launchExternalBrowser(url = FEE_SCHEDULE_URL)
        binding.feeScheduleLayout.isEnabled = true
    }

    private fun goToElectronicDisclosures() {
        binding.electronicDisclosuresLayout.isEnabled = false
        WesterraApplication.getInstance().launchExternalBrowser(url = ELECTRONIC_DISCLOSURES_URL)
        binding.electronicDisclosuresLayout.isEnabled = true
    }

    private fun goToWeGotYaDisclosures() {
        binding.wegotyaDisclosureLayout.isEnabled = false
        WesterraApplication.getInstance().launchExternalBrowser(url = WEGOTYA_DISCLOSURES_URL)
        binding.wegotyaDisclosureLayout.isEnabled = true
    }

    private fun goToPdf(url: String) {
        val bundle = bundleOf(BtpPdfViewerFragment.PDF_VIEWER_URL_KEY to url)
        findNavController().navigate(
            R.id.action_disclosures_to_pdf_viewer,
            bundle,
        )
    }

    private fun goToRequirements() {
        product ?. let {
            WesterraAnalytics.recordTermsAcceptedEvent()
            DataTransferCache().save(BTP_FRAGMENTS_PRODUCT_KEY, it)
            findNavController().navigate(
                R.id.action_disclosures_to_requirements,
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
