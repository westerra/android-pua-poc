package com.westerra.release.more.items

import android.app.Activity
import android.os.Looper
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.alerts.DeleteAccountAlert

class DeleteAccountMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.delete_account_title,
        icon = R.drawable.backbase_ic_delete,
        actionBlock = {
            if (Looper.getMainLooper().isCurrentThread) {
                val activity = WesterraApplication.getInstance().getActivity()
                if (activity is Activity) {
                    DeleteAccountAlert(activity = activity).show()
                }
            }
            OnActionComplete.DoNothing
        },
    )
}
