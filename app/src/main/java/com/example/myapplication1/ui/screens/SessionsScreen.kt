package com.example.myapplication1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication1.domain.model.AuthorRole
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.MessageDeliveryStatus
import com.example.myapplication1.domain.model.MessageKind
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.EmptyStateCard
import com.example.myapplication1.ui.components.StatusChip

private val WeChatGreen = Color(0xFF95EC69)
private val WeChatBackground = Color(0xFFF3F4F6)
private val WeChatToolbar = Color(0xFFFFFFFF)
private val WeChatMeta = Color(0xFF8C8C8C)

@Composable
fun SessionsScreen(
    state: AppUiState,
    onSelectConversation: (String) -> Unit,
    onDraftChanged: (String) -> Unit,
    onSendTextMessage: () -> Unit,
    onSendMediaMessage: (MessageKind) -> Unit
) {
    val conversation = state.selectedConversation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WeChatBackground)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "聊天",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )

        ConversationSwitcher(
            conversations = state.conversations,
            selectedConversationId = state.selectedConversationId,
            onSelectConversation = onSelectConversation
        )

        if (conversation == null) {
            EmptyStateCard(
                title = "没有打开的聊天",
                detail = "先从上方选择一个联系人或群聊。"
            )
        } else {
            ChatThread(
                conversation = conversation,
                messages = state.selectedMessages,
                draft = state.messageDraft,
                onDraftChanged = onDraftChanged,
                onSendTextMessage = onSendTextMessage,
                onSendMediaMessage = onSendMediaMessage
            )
        }
    }
}

@Composable
private fun ConversationSwitcher(
    conversations: List<ConversationSummary>,
    selectedConversationId: String?,
    onSelectConversation: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(conversations, key = ConversationSummary::id) { conversation ->
            val selected = conversation.id == selectedConversationId
            Card(
                modifier = Modifier.clickable { onSelectConversation(conversation.id) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selected) MaterialTheme.colorScheme.surface else WeChatToolbar
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                color = if (conversation.isGroup) Color(0xFFFFD8CC) else Color(0xFFDDEBFF),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversation.title.take(1),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = conversation.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = conversation.preview,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (conversation.unreadCount > 0) {
                        Surface(
                            color = Color(0xFFFF5A5F),
                            shape = CircleShape
                        ) {
                            Text(
                                text = conversation.unreadCount.toString(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatThread(
    conversation: ConversationSummary,
    messages: List<ChatMessage>,
    draft: String,
    onDraftChanged: (String) -> Unit,
    onSendTextMessage: () -> Unit,
    onSendMediaMessage: (MessageKind) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = WeChatBackground),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatHeader(conversation = conversation)

            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateCard(
                        title = "还没有消息",
                        detail = "发一条消息开始聊天。"
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        TimePill(text = conversation.timestamp)
                    }
                    items(messages, key = ChatMessage::id) { message ->
                        WeChatBubble(message = message)
                    }
                }
            }

            ChatComposer(
                draft = draft,
                onDraftChanged = onDraftChanged,
                onSendTextMessage = onSendTextMessage,
                onSendMediaMessage = onSendMediaMessage
            )
        }
    }
}

@Composable
private fun ChatHeader(conversation: ConversationSummary) {
    Surface(
        color = WeChatToolbar,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = conversation.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (conversation.isGroup) "${conversation.memberCount} 人群聊" else "在线聊天",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WeChatMeta
                )
            }
            StatusChip(
                text = if (conversation.isGroup) "群聊" else "消息",
                accent = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TimePill(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color(0xFFE6E8EC),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = WeChatMeta
            )
        }
    }
}

@Composable
private fun WeChatBubble(message: ChatMessage) {
    val isSelf = message.authorRole == AuthorRole.SELF

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isSelf) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isSelf) {
            ChatAvatar(label = message.author.take(1))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .widthIn(max = 280.dp),
            horizontalAlignment = if (isSelf) Alignment.End else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!isSelf) {
                Text(
                    text = message.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = WeChatMeta
                )
            }
            Surface(
                color = if (isSelf) WeChatGreen else Color.White,
                shape = RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 6.dp,
                    bottomStart = if (isSelf) 12.dp else 4.dp,
                    bottomEnd = if (isSelf) 4.dp else 12.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WeChatMessageBody(message = message)
                }
            }
            Text(
                text = if (isSelf) {
                    "${message.timestamp} ${message.deliveryStatus.toSimpleLabel()}"
                } else {
                    message.timestamp
                },
                style = MaterialTheme.typography.bodyMedium,
                color = WeChatMeta
            )
        }

        if (isSelf) {
            ChatAvatar(label = "我")
        }
    }
}

@Composable
private fun ChatAvatar(label: String) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(Color(0xFFD9DDE4), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun WeChatMessageBody(message: ChatMessage) {
    when (message.kind) {
        MessageKind.TEXT -> Text(
            text = message.body,
            style = MaterialTheme.typography.bodyLarge
        )

        MessageKind.IMAGE -> MediaBubble(label = "[图片]", content = message.body)
        MessageKind.AUDIO -> MediaBubble(label = "[语音]", content = message.body)
        MessageKind.VIDEO -> MediaBubble(label = "[视频]", content = message.body)
    }
}

@Composable
private fun MediaBubble(
    label: String,
    content: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Surface(
            color = Color(0x14000000),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ChatComposer(
    draft: String,
    onDraftChanged: (String) -> Unit,
    onSendTextMessage: () -> Unit,
    onSendMediaMessage: (MessageKind) -> Unit
) {
    Surface(
        color = WeChatToolbar,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = draft,
                onValueChange = onDraftChanged,
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                shape = RoundedCornerShape(18.dp),
                label = { Text("输入消息") },
                placeholder = { Text("发送消息") }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ComposerAction("图") { onSendMediaMessage(MessageKind.IMAGE) }
                    ComposerAction("语") { onSendMediaMessage(MessageKind.AUDIO) }
                    ComposerAction("视") { onSendMediaMessage(MessageKind.VIDEO) }
                }
                Button(
                    onClick = onSendTextMessage,
                    colors = ButtonDefaults.buttonColors(containerColor = WeChatGreen, contentColor = Color.Black),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("发送")
                }
            }
        }
    }
}

@Composable
private fun ComposerAction(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        color = Color(0xFFF0F1F3),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
internal fun SectionHeader(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
internal fun HighlightPanel(
    title: String,
    lines: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            lines.forEach { line ->
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun MessageDeliveryStatus.toSimpleLabel(): String {
    return when (this) {
        MessageDeliveryStatus.LOCAL -> ""
        MessageDeliveryStatus.SENDING -> "发送中"
        MessageDeliveryStatus.SENT -> "已发送"
        MessageDeliveryStatus.DELIVERED -> "已送达"
        MessageDeliveryStatus.FAILED -> "失败"
    }.trim()
}
