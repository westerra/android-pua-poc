package com.westerra.release.messages

import com.backbase.engagementchannels.messages.MessagesConfiguration
import com.backbase.engagementchannels.messages.compose.ComposeMessageConfiguration
import com.backbase.engagementchannels.messages.conversation.ConversationConfiguration
import com.backbase.engagementchannels.messages.messagelist.MessageListConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredColor
import com.westerra.release.extensions.toDeferredText

object MessagesConfig {

    operator fun invoke(): MessagesConfiguration {
        return MessagesConfiguration {
            listItemAvatarBackground = R.color.messageListIconBackground.toDeferredColor()
            listItemAvatarTextColor = R.color.messageListIconText.toDeferredColor()

            messageListConfiguration = MessageListConfiguration {
                inboxTitle = R.string.inbox_tab_title.toDeferredText()
                sentTitle = R.string.sent_tab_title.toDeferredText()
                draftsTitle = R.string.drafts_tab_title.toDeferredText()
            }

            conversationConfiguration = ConversationConfiguration {
                useMessageIcon = false
            }

            composeMessageConfiguration = getComposeMessageConfiguration()
        }
    }

    private fun getComposeMessageConfiguration(): ComposeMessageConfiguration =
        ComposeMessageConfiguration {
            title = R.string.new_message_title.toDeferredText()
            bodyMaxCount = 300
            subjectMaxCount = 100
        }
}
