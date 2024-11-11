package com.westerra.release.constants

object Constants {
    const val WESTERRA_ROUTING_NUMBER = "302075319"
    const val WESTERRA_HOME_URL = "https://www.westerracu.com"
    const val WESTERRA_SPENDING_URL = "https://www.westerracu.com/spending"
    const val WESTERRA_SAVINGS_URL = "https://www.westerracu.com/moneymarket"
    const val SCHEDULE_APPOINTMENT_URL =
        "https://www.timetrade.com/app/westerracu/workflows/westerra002/schedule?resourceId=any"
    const val DISPUTE_TRANSACTION_URL =
        "https://www.westerracu.com/resources/forms?q=card-dispute-forms"
    const val SPLASH_DURATION_SECONDS = 3L

    const val STATE_ACTIVE = "Active"
    const val TRANSACTION_OUTGOING = "Outgoing"
    const val TRANSACTION_INCOMING = "Incoming"
    const val DEFAULT_ZERO_STRING = "0.0"

    const val MENU_INSTANCE_ID_MOVE_MONEY = "move-money"
    const val MENU_INSTANCE_ID_DEPOSIT_A_CHECK = "deposit-a-check"
    const val DEFAULT_EMPTY_STRING = ""
    const val KEY_TOOLBAR_TITLE = "key_toolbar_title"
    const val KEY_SSO_LINK = "ssolink"
    const val KEY_INTERNAL_ARRANGEMENT_ID = "internalArrangementId"
    const val KEY_WEB_VIEW_FRAGMENT_URL = "webViewFragmentUrl"
    const val KEY_IS_PAYMENTS = "isPayments"
    const val KEY_CONTENT_TYPE = "Content-Type"
    const val VALUE_APPLICATION_JSON = "application/json"
    const val SIMPLE_MDY_PATTERN = "MMM dd, yyyy"
    const val SIMPLE_YMD_PATTERN = "yyyy-MM-dd"
    const val PREFERRED_DATE_PATTERN = "EEEE, MMM dd, yyyy"
    const val SHORT_DATE_PATTERN = "MM/dd/YYYY"
    const val ACCOUNT_NUMBER_MASK = "•"

    const val BUILD_FLAVOR_DEV = "dev"
    const val BUILD_FLAVOR_UAT = "uat"
    const val BUILD_FLAVOR_PROD = "prod"
    const val PLAY_STORE_ID = "com.westerra.release.prod.release"

    const val AWS_WAF_DEV_DOMAIN_NAME = "westerracu.com"
    const val AWS_WAF_SIT_DOMAIN_NAME = "westerracu.com"
    const val AWS_WAF_UAT_DOMAIN_NAME = "westerracu.com"
    const val AWS_WAF_PROD_DOMAIN_NAME = "westerracu.com"
    const val AWS_WAF_DEV_URI = "https://westerracu.com"
    const val AWS_WAF_SIT_URI = "https://westerracu.com"
    const val AWS_WAF_UAT_URI = "https://westerracu.com"
    const val AWS_WAF_PROD_URI = "https://westerracu.com"
    const val AWS_WAF_PROD_INTEGRATION_URL =
        "https://444f4282d156.us-east-2.sdk.awswaf.com/444f4282d156/c29ecc84f660/"
    const val AWS_WAF_UAT_INTEGRATION_URL =
        "https://444f4282d156.us-east-2.sdk.awswaf.com/444f4282d156/7cec583c621c/"
    const val AWS_WAF_SIT_INTEGRATION_URL =
        "https://444f4282d156.us-east-2.sdk.awswaf.com/444f4282d156/7cec583c621c/"
    const val AWS_WAF_DEV_INTEGRATION_URL =
        "https://444f4282d156.us-east-2.sdk.awswaf.com/444f4282d156/7cec583c621c/"

    const val PAYVERIS_PROD_DOMAIN = "https://billpay.payverisbp.com/"
    const val PAYVERIS_TEST_DOMAIN = "https://test.regrpayverisbp.com"
    const val DMI_PROD_DOMAIN = "https://mtgsvc.com/"
    const val DMI_TEST_DOMAIN = "https://mtgsvc.tv/"

    const val DENVER_LATITUDE = 39.742043
    const val DENVER_LONGITUDE = -104.991531

    const val TEXT_HTML_UTF8_MIME_TYPE = "text/html; charset=UTF-8"
    const val UTF8_ENCODING = "UTF-8"

    const val TYPE_NAME_LOC = "loc"
    const val TYPE_NAME_LINE_OF_CREDIT = "line of credit"
    const val TYPE_NAME_OVERDRAFT = "overdraft"
    const val TYPE_NAME_AUTO = "auto"

    const val WESTERRA_CALL_NUMBER = "3033214209"
    const val TEXT_PLAIN = "text/plain"
}
