package com.westerra.release.authentication

import com.backbase.android.identity.journey.authentication.AuthenticationConfiguration
import com.backbase.android.identity.journey.authentication.biometric.BiometricScreensConfiguration
import com.backbase.android.identity.journey.authentication.biometric.enroll.EnrollBiometricScreenConfiguration
import com.backbase.android.identity.journey.authentication.biometric.late_enroll.LateEnrollBiometricSuccessScreenConfiguration
import com.backbase.android.identity.journey.authentication.enroll.EnrollmentCompleteScreenConfiguration
import com.backbase.android.identity.journey.authentication.forgot_credentials.ForgotCredentialsConfiguration
import com.backbase.android.identity.journey.authentication.forgot_credentials.ForgotCredentialsOptionsScreenConfiguration
import com.backbase.android.identity.journey.authentication.forgot_credentials.forgot_password.ForgotPasswordOtpEntryScreenConfiguration
import com.backbase.android.identity.journey.authentication.forgot_credentials.forgot_password.ForgotPasswordUsernameInputScreenConfiguration
import com.backbase.android.identity.journey.authentication.forgot_credentials.forgot_password.ForgotPasswordWithOtpAuthenticationScreensConfiguration
import com.backbase.android.identity.journey.authentication.forgot_credentials.forgot_username.ForgotUsernameEmailInputScreenConfiguration
import com.backbase.android.identity.journey.authentication.forgot_credentials.forgot_username.ForgotUsernameSuccessScreenConfiguration
import com.backbase.android.identity.journey.authentication.login.LoginScreenConfiguration
import com.backbase.android.identity.journey.authentication.passcode.create.CreatePasscodeScreenConfiguration
import com.backbase.android.identity.journey.authentication.transaction.TransactionSigningScreenConfiguration
import com.backbase.android.identity.journey.authentication.update_password.UpdatePasswordScreenConfiguration
import com.backbase.android.identity.journey.authentication.update_password.UpdatePasswordSuccessScreenConfiguration
import com.backbase.android.identity.journey.authentication.username.UsernameScreenConfiguration
import com.backbase.deferredresources.DeferredText
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredBoolean
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object AuthenticationConfig {

    operator fun invoke(): AuthenticationConfiguration {
        return AuthenticationConfiguration {
            background = R.color.authenticationBackgroundColor.toDeferredDrawable()
            showEnrollmentCompletionScreen = true.toDeferredBoolean()
            forgotCredentialsScreensConfiguration = ForgotCredentialsConfiguration {
                showOptions = setOf(
                    ForgotCredentialsConfiguration.Option.FORGOT_USERNAME,
                    ForgotCredentialsConfiguration.Option.FORGOT_PASSWORD,
                )

                forgotCredentialsOptionsScreen = ForgotCredentialsOptionsScreenConfiguration {
                    forgotUsernameOptionText = R.string.forgot_username_option_text.toDeferredText()
                    forgotPasswordOptionText = R.string.forgot_password_option_text.toDeferredText()
                }

                forgotPasswordUsernameInputScreen = ForgotPasswordUsernameInputScreenConfiguration {
                    background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                    confirmButtonText = R.string.continue_button_text.toDeferredText()
                }

                forgotPasswordWithOtpAuthenticationScreens = ForgotPasswordWithOtpAuthenticationScreensConfiguration.Builder().apply{
                    otpInputScreen = ForgotPasswordOtpEntryScreenConfiguration.Builder().apply {
                        background = R.color.authenticationBackgroundColor.toDeferredDrawable()

                        //TODO confirmButtonText is replaced by string resource, R.string.identity_authentication_forgotPassword_otp_input_buttons_submit
                       // confirmButtonText = R.string.submit_button_title.toDeferredText()

                    }.build()
                }.build()

                //TODO replaced with forgotPasswordWithOtpAuthenticationScreens -> ForgotPasswordOtpEntryScreenConfiguration
//                forgotPasswordOtpInputScreen = ForgotPasswordOtpInputScreenConfiguration {
//                    background = R.color.authenticationBackgroundColor.toDeferredDrawable()
//                    confirmButtonText = R.string.submit_button_title.toDeferredText()
//                }

                updatePasswordScreen = UpdatePasswordScreenConfiguration {
                    background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                    confirmButtonText = R.string.confirm_button_title.toDeferredText()
                }

                updatePasswordSuccessScreen = UpdatePasswordSuccessScreenConfiguration {
                    background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                    doneButtonText = R.string.done_button_title.toDeferredText()
                }

                forgotUsernameEmailInputScreen = ForgotUsernameEmailInputScreenConfiguration {
                    background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                    titleText = R.string.forgot_username_title.toDeferredText()
                    descriptionText = R.string.forgot_username_subtitle.toDeferredText()
                    inputFieldTitle = R.string.email_placeholder.toDeferredText()
                    confirmButtonText = R.string.confirm_email_address.toDeferredText()
                }

                forgotUsernameSuccessScreen = ForgotUsernameSuccessScreenConfiguration {
                    background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                    titleText = R.string.instruction_sent_email_title.toDeferredText()
                    descriptionText = R.string.check_email_steps_description.toDeferredText()
                    confirmButtonText = R.string.done_button_title.toDeferredText()
                }
            }

            loginScreenConfiguration = LoginScreenConfiguration {
                background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                // westerra_logo_stacked image present in both drawable and drawable-night
                // changes based on the UI theme(light or dark)
                titleIcon = R.drawable.westerra_logo_stacked.toDeferredDrawable()
                titleText = DeferredText.Resource(
                    R.string.welcome_back,
                    DeferredText.Resource.Type.TEXT,
                )
                forgotPasscodeButtonText = R.string.forgot_passcode_button_title.toDeferredText()
            }

            passcodeScreenConfiguration = CreatePasscodeScreenConfiguration {
                background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                showDismissButton = false.toDeferredBoolean()
                showResetButton = true.toDeferredBoolean()
                resetButtonTitle = R.string.reset_button_title.toDeferredText()
                tipText = R.string.empty_string.toDeferredText()
            }

            usernameScreenConfiguration = UsernameScreenConfiguration {
                background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                forgotCredentialsText = R.string.forgot_credentials_text.toDeferredText()
                logInText = R.string.log_in_button_title.toDeferredText()
                showForgotCredentials = true.toDeferredBoolean()
            }
            otpScreensConfiguration = OtpConfig()

            biometricScreensConfiguration = BiometricScreensConfiguration {
                background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                enrollBiometricScreenConfiguration = EnrollBiometricScreenConfiguration {
                    enableBiometricText = R.string.enable_biometric_button_title.toDeferredText()
                }
            }

            enrollmentCompleteScreenConfiguration = EnrollmentCompleteScreenConfiguration {
                background = R.color.authenticationBackgroundColor.toDeferredDrawable()
            }

            inBandTransactionSigningScreenConfiguration = TransactionSigningScreenConfiguration {
                background = R.color.authenticationBackgroundColor.toDeferredDrawable()
            }

            lateEnrollBiometricSuccessScreenConfiguration =
                LateEnrollBiometricSuccessScreenConfiguration {
                    background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                }
        }
    }
}
