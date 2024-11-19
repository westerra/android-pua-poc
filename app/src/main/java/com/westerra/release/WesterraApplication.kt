package com.westerra.release

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.backbase.android.client.gen2.paymentorderv2client2.api.PaymentOrdersApi
import com.backbase.android.client.usermanagerclient2.api.UserProfileManagementApi
import com.backbase.android.identity.journey.authentication.AuthenticationJourney
import com.backbase.android.identity.journey.authentication.passcode.change_passcode.ChangePasscodeRouter
import com.backbase.android.identity.journey.userprofile.UserProfileJourneyScope
import com.backbase.android.identity.journey.userprofile.usecase.UserManagerUserProfileUseCase
import com.backbase.android.retail.journey.app.common.COMMON_API_ROOT_QUALIFIER
import com.backbase.android.retail.journey.app.us.UsAppActivity
import com.backbase.android.retail.journey.more.MoreJourneyScope
import com.backbase.android.retail.journey.more.MoreMenuInstanceId
import com.backbase.android.retail.journey.payments.PaymentJourneyScope
import com.backbase.android.retail.journey.payments.PaymentJourneyType
import com.backbase.android.retail.journey.payments.PaymentUseCase
import com.backbase.android.retail.journey.payments.filters.PaymentPartyListFilter
import com.backbase.android.retail.journey.payments.gen2_paymentorder_v2_client_2.PaymentOrderV2Client2PaymentServiceUseCase
import com.backbase.android.retail.journey.payments.model.PaymentParty
import com.backbase.android.retail.journey.payments.model.PaymentPartyType
import com.westerra.release.awswaf.WafApplication
import com.westerra.release.constants.Constants.MENU_INSTANCE_ID_DEPOSIT_A_CHECK
import com.westerra.release.constants.Constants.MENU_INSTANCE_ID_MOVE_MONEY
import com.westerra.release.extensions.launchExternalBrowser
import com.westerra.release.movemoney.MoveMoneyHubConfig
import com.westerra.release.payments.config.ContactsP2PConfig
import com.westerra.release.payments.config.InternalA2AConfig
import com.westerra.release.payments.usecase.CustomP2PPaymentUseCase
import com.westerra.release.rdc.RDCMoreConfiguration
import com.westerra.release.splash.SplashScreenConfig
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope

/**
 * The application class used by this app to define its various Journey configurations and
 * other setup items.
 */
@Suppress("unused") // Used in AndroidManifest
class WesterraApplication : WafApplication() {
    private val usAppNavHostFragmentString = "usApp_navHostFragment"
    private val navController get() = (
        _activity?.supportFragmentManager?.findFragmentByTag(
            usAppNavHostFragmentString,
        ) as? NavHostFragment
        )?.navController

    private var _activity: UsAppActivity? = null

    init {
        instance = this@WesterraApplication
    }

    companion object {
        private val TAG = WesterraApplication::class.java.simpleName
        private var instance: WesterraApplication? = null
        fun getInstance(): WesterraApplication {
            return instance!!
        }
    }

    fun setActivity(value: UsAppActivity?) {
        _activity = value
    }
    private fun Scope.apiRoot() = get<String>(COMMON_API_ROOT_QUALIFIER)

    fun getActivity(): UsAppActivity? {
        return _activity
    }

    fun getMyNavController(): NavController? {
        return navController
    }

    override fun onCreate() {
        super.onCreate()
        enableSecurityControls()
    }

    override fun createSplashScreenConfiguration() = SplashScreenConfig()

    override fun createApplicationConfiguration() = AppConfiguration(application = this)

    override fun createUseCaseDefinitions() =
        WesterraUseCaseDefinitions(overrides = { westerraCustomUseCases() })

    override val additionalApplicationDependencies =
        WesterraClientDefinitions(
            additionalDependencies = {
                scope<MoreJourneyScope> {
                    scoped(MoreMenuInstanceId.Custom(name = MENU_INSTANCE_ID_DEPOSIT_A_CHECK)) {
                        RDCMoreConfiguration()
                    }
                }
                scope<MoreJourneyScope> {
                    scoped(MoreMenuInstanceId.Custom(name = MENU_INSTANCE_ID_MOVE_MONEY)) {
                        MoveMoneyHubConfig()
                    }
                }
                scope<AuthenticationJourney> {
                    scoped {
                        ChangePasscodeRouter {
                            navController?.navigateUp()
                        }
                    }
                }
                scope<UserProfileJourneyScope> {
                    scoped {
                        UserManagerUserProfileUseCase(
                            userProfileManagementApi = get<UserProfileManagementApi>(),
                        )
                    }
                }

                scope<PaymentJourneyScope> {
                    scoped {
                        InternalA2AConfig()
                    }
                    scoped(PaymentJourneyType.P2P) {
                        ContactsP2PConfig()
                    }
                    scoped<PaymentUseCase>(PaymentJourneyType.P2P) {
                        CustomP2PPaymentUseCase(
                            PaymentOrderV2Client2PaymentServiceUseCase(
                            paymentOrdersApi = get<PaymentOrdersApi>()
                        )
                        )
                    }

                    // customizing 'from' and 'to' payment list filter in A2A transfer
                    scoped<PaymentPartyListFilter> {
                        object : PaymentPartyListFilter {
                            // default is checking, savings, loan and credit card accounts
                            override fun filterFromPaymentParty(
                                fromPaymentParty: PaymentParty,
                                toPaymentParty: PaymentParty?,
                            ): Boolean = when (fromPaymentParty.paymentPartyType) {
                                PaymentPartyType.ExternalCheckingAccount,
                                PaymentPartyType.SavingsAccount,
                                PaymentPartyType.Loan,
                                PaymentPartyType.CreditCard,
                                PaymentPartyType.CurrentAccount,
                                -> true
                                else -> false
                            }

                            // default is checking, savings and credit card
                            // as per requirement, adding loan account
                            override fun filterToPaymentParty(
                                fromPaymentParty: PaymentParty?,
                                toPaymentParty: PaymentParty,
                            ): Boolean = when (toPaymentParty.paymentPartyType) {
                                PaymentPartyType.ExternalCheckingAccount,
                                PaymentPartyType.SavingsAccount,
                                PaymentPartyType.Loan,
                                PaymentPartyType.CreditCard,
                                PaymentPartyType.CurrentAccount,
                                -> true
                                else -> false
                            }
                        }
                    }
                }
            },
        )

    override suspend fun onInitialized() {
        val mNavController = navController
        val mActivity = getActivity()
        if (mActivity != null && mNavController != null) {
            initializeKoinModulesWithDependencies(
                navController = mNavController,
                activity = mActivity,
            )
        } else {
            val s1 = if (mActivity == null) "Not Exists" else "Exists"
            val s2 = if (mNavController == null) "Not Exists" else "Exists"
            val msg = "onInitialized unexpected missing state. Activity $s1, NavController = $s2"
            Log.e(TAG, msg)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    mActivity?.lifecycleScope?.launch {
                        onInitialized()
                    }
                },
                250,
            )
        }
    }

    override fun onInitializeDependencies() {
        super.onInitializeDependencies()
        setSecurityViolationListener()
    }

    fun launchExternalBrowser(url: String) {
        val activity = getActivity()
        if (activity == null) {
            Log.w(TAG, "Unexpected activity not found")
            return
        }
        activity.launchExternalBrowser(url = url)
    }
}
