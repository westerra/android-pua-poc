# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# DO-NOT-TOUCH : These rules are added for `2022.04` release, will be removed in subsequent releases.
-keepclassmembers class * implements com.backbase.android.identity.BBAuthenticatorView { public <init>(...); }

-keep class com.backbase.android.identity.journey.authentication.passcode.CustomPasscodeAuthenticator { public <init>(...); }
-keep class com.backbase.android.identity.journey.authentication.update_password.CustomUpdatePasswordAuthenticator { public <init>(...); }
-keep class com.backbase.android.identity.journey.authentication.input_required.CustomInputRequiredAuthenticator { public <init>(...); }
-keep class com.backbase.android.identity.journey.authentication.otp.CustomOtpAuthenticator { public <init>(...); }
-keep class com.backbase.android.identity.journey.authentication.terms_and_conditions.CustomTermsAndConditionsAuthenticator { public <init>(...); }

-keep class com.backbase.android.identity.journey.authentication.navigation.ForgotCredentialsEvent { public <init>(...); }
-keep class com.backbase.android.identity.journey.authentication.navigation.ForgotUsernameConfirmationEvent { public <init>(...); }
-keep class com.backbase.android.identity.journey.oob_authentication.CustomOutOfBandAuthSessionAuthenticator { public <init>(...); }

-keep class com.backbase.android.identity.journey.authentication.AuthenticationUseCase$UserInfo { <fields>; }

-keep class com.google.gson.stream.** { *; }

-keep class com.backbase.mobilenotifications.core.model.** { *; }
-keep class com.backbase.modelBank.retail.app.us.cards.TravelDestinationJsonData { <fields>; }
-keep class com.backbase.modelBank.retail.app.us.cards.TravelDestinationRegionJsonData { <fields>; }

-keep class com.backbase.android.retail.journey.rdc.gen_remotedepositcapturer_client_2.model.BadErrorRequest { <fields>; }
-keep class com.backbase.android.retail.journey.billPaySso.genBillPayIntegrator.model.** { *; }

# These rules are needed for Entrust SDK
-keep class fr.antelop.exposed.** { *; }

-keep, includedescriptorclasses  class fr.antelop.antelopsecurecmodule.** {
   native <methods>;
}

-keep  class fr.antelop.antelopsecurecmodule.** {
   *;
}

#Prevent SDK Callback classes to be obfuscated
-keep public class * extends fr.antelop.sdk.WalletNotificationServiceCallback
-keep public class * extends fr.antelop.sdk.transaction.hce.HceTransactionListener
-keep public class * extends fr.antelop.sdk.sca.PushAuthenticationRequestListener

-keep class com.backbase.android.journey.dashboard.quickactions.view.QuickActionTile {
    <init>(...);
 }

-keep class com.backbase.android.retail.journey.accounts_and_transactions.accounts.total_balance.tile.TotalBalanceTile {
    <init>(...);
}

-keep class com.backbase.android.retail.journey.accounts_and_transactions.transactions.tile.TransactionTile {
    <init>(...);
}

-keep class com.backbase.android.retail.journey.accounts_and_transactions.accounts.tile.AccountsTile {
    <init>(...);
}

-keep class com.backbase.android.journey.dashboard.bannertile.view.BannerTile {
    <init>(...);
}

#keep required data classes in flow selector
-keep class com.backbase.android.flow.v2.models.** { *; }
-keep class com.backbase.android.flow.loading.models.** { *; }
-keep class com.backbase.android.flow.coapplicant.welcome.models.** { *; }
-keep class com.backbase.android.flow.productselector.models.** { *; }
-keep class com.backbase.android.flow.aboutyou.models.** { *; }
-keep class com.backbase.android.flow.walkthrough.models.** { *; }
-keep class com.backbase.android.flow.otp.models.** { *; }
-keep class com.backbase.android.flow.identityverification.models.** { *; }
-keep class com.backbase.android.flow.address.models.** { *; }
-keep class com.backbase.android.flow.ssn.models.** { *; }
-keep class com.backbase.android.flow.citizenship.models.** { *; }
-keep class com.backbase.android.flow.questionnaire.models.** { *; }
-keep class com.backbase.android.flow.credentialscreation.model.** { *; }
-keep class com.backbase.android.flow.customerandaccountcreation.model.** { *; }
-keep class com.backbase.android.flow.common.model.** { *; }

-keep class com.backbase.android.design.phone.PhoneInputView { public <init>(...); }