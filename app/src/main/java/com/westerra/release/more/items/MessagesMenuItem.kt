package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R

class MessagesMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.messages_title,
        icon = R.drawable.ic_messages,
        actionBlock = {
            OnActionComplete.NavigateTo(R.id.action_mainScreen_to_messagesJourney)
        },
    )
}
