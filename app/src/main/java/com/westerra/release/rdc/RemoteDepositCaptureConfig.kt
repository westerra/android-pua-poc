package com.westerra.release.rdc

import com.backbase.android.retail.journey.rdc.RdcConfiguration
import com.backbase.android.retail.journey.rdc.configuration.AccountSelectorConfiguration
import com.backbase.android.retail.journey.rdc.configuration.BalanceConfiguration
import com.backbase.android.retail.journey.rdc.configuration.CompleteScreenConfiguration
import com.backbase.android.retail.journey.rdc.configuration.CurrentAccountBalanceType
import com.backbase.android.retail.journey.rdc.configuration.EditDepositItemScreenConfiguration
import com.backbase.android.retail.journey.rdc.configuration.FormScreenConfiguration
import com.backbase.android.retail.journey.rdc.configuration.GeneralAccountBalanceType
import com.backbase.android.retail.journey.rdc.configuration.RejectedScreenConfiguration
import com.backbase.android.retail.journey.rdc.configuration.ReviewScreenConfiguration
import com.backbase.android.retail.journey.rdc.configuration.SavingsAccountBalanceType
import com.backbase.android.retail.journey.rdc.configuration.UnverifiedScreenConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredText

object RemoteDepositCaptureConfig {

    operator fun invoke(): RdcConfiguration {
        return RdcConfiguration {
            balanceConfiguration = getBalanceConfig()

            formScreenConfiguration = FormScreenConfiguration {
                title = R.string.deposit_check_title.toDeferredText()
                accountSelectorConfiguration = AccountSelectorConfiguration {
                    title = R.string.to_account_title.toDeferredText()
                }
                bottomButtonText = R.string.verify_check_button_text.toDeferredText()
            }

            reviewScreenConfiguration = ReviewScreenConfiguration {
                bottomButtonText = R.string.confirm_deposit_button_text.toDeferredText()
            }

            editDepositItemScreenConfiguration = EditDepositItemScreenConfiguration {
                title = R.string.edit_check_details_title.toDeferredText()
                bottomButtonText = R.string.done_button_text.toDeferredText()
            }

            unverifiedScreenConfiguration = UnverifiedScreenConfiguration {
                title = R.string.unverified_check_title.toDeferredText()
                editBottomButtonText = R.string.edit_check_details_button_text.toDeferredText()
                confirmBottomButtonText = R.string.confirm_as_is_button_text.toDeferredText()
            }

            completeScreenConfiguration = CompleteScreenConfiguration {
                bottomButtonText = R.string.done_button_text.toDeferredText()
                title = {
                    R.string.check_submitted_title.toDeferredText()
                }
            }

            rejectedScreenConfiguration = RejectedScreenConfiguration {
                bottomButtonText = R.string.got_it_button_text.toDeferredText()
            }
        }
    }

    // RDC settings for which balances to display in To Accounts list
    private fun getBalanceConfig(): BalanceConfiguration {
        return BalanceConfiguration {
            currentAccountBalanceType = CurrentAccountBalanceType.AvailableBalance()
            generalAccountBalanceType = GeneralAccountBalanceType.AvailableBalance()
            savingsAccountBalanceType = SavingsAccountBalanceType.AvailableBalance()
        }
    }
}
