package com.westerra.release.remoteconfig

import com.backbase.android.retail.journey.app.common.remoteconfig.RemoteConfigConfiguration

/**
 * Object containing every key used for [com.backbase.android.remoteconfig.RemoteConfig] in this app.
 */
object RemoteConfigKey {
    const val SHOW_CONTACTS = "show_contacts"
}

/**
 * Map of default values for each [RemoteConfigKey].
 */
val RemoteConfigDefaults: Map<String, Any> = mapOf(
    RemoteConfigKey.SHOW_CONTACTS to true,
)

const val REMOTE_CONFIG_PROJECT_NAME = "backbase-retail-prototypes"

object RemoteConfigConfig {
    operator fun invoke(appName: String): RemoteConfigConfiguration {
        return RemoteConfigConfiguration {
            projectName = REMOTE_CONFIG_PROJECT_NAME
            applicationName = appName
            defaultParameters = RemoteConfigDefaults
        }
    }
}
