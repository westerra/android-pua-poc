package com.westerra.release.profile

import com.backbase.android.identity.journey.userprofile.email.edit.EmailScreenConfiguration
import com.westerra.release.extensions.toDeferredBoolean

object EmailScreenConfig {
    operator fun invoke(): EmailScreenConfiguration {
        return EmailScreenConfiguration {
            isPrimaryEditable = false.toDeferredBoolean()
            isTypeEditable = false.toDeferredBoolean()
        }
    }
}
