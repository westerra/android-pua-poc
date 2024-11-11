package com.westerra.release.constants

import com.westerra.release.BuildConfig

enum class BuildFlavors(val stringValue: String) {
    DEV("dev"),
    UAT("uat"),
    PROD("prod"),
    ;

    companion object {
        fun fromString(value: String?) = BuildFlavors.values().firstOrNull {
            it.stringValue.equals(value, true)
        }

        fun fromBuildConfig() = this.fromString(BuildConfig.FLAVOR)
    }

    fun integrationUrl(): String {
        return when (this) {
            PROD -> {
                Constants.AWS_WAF_PROD_INTEGRATION_URL
            }
            UAT -> {
                Constants.AWS_WAF_UAT_INTEGRATION_URL
            }
            DEV -> {
                Constants.AWS_WAF_DEV_INTEGRATION_URL
            }
        }
    }

    fun domainName(): String {
        return when (this) {
            PROD -> {
                Constants.AWS_WAF_PROD_DOMAIN_NAME
            }
            UAT -> {
                Constants.AWS_WAF_UAT_DOMAIN_NAME
            }
            DEV -> {
                Constants.AWS_WAF_DEV_DOMAIN_NAME
            }
        }
    }

    fun wafUri(): String {
        return when (this) {
            PROD -> {
                Constants.AWS_WAF_PROD_URI
            }
            UAT -> {
                Constants.AWS_WAF_UAT_URI
            }
            DEV -> {
                Constants.AWS_WAF_DEV_URI
            }
        }
    }
}
