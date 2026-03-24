package com.example.myapplication1.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.example.myapplication1.domain.model.AuthorRole
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.MessageDeliveryStatus
import com.example.myapplication1.domain.model.MessageKind
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.EmptyStateCard
import com.example.myapplication1.ui.components.InitialBadge
import com.example.myapplication1.ui.components.StatusChip
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue

@Composable
fun SessionsScreen(
    state: AppUiState,
    onSelectConversation: (String) -> Unit,
    onDraftChanged: (String) -> Unit,
    onSendTextMessage: () -> Unit,
    onSendMediaMessage: (MessageKind) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "消息",
                subtitle = "会话、聊天记录和输入区集中在一个清晰页面里。"
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    SummaryStatCard(
                        title = "当前会话",
                        value = state.selectedConversation?.title ?: "未选择",
                        caption = state.transportStatus,
                        accent = SkyBlue
                    )
                }
                item {
                    SummaryStatCard(
                        title = "消息数量",
                        value = state.selectedMessages.size.toString(),
                        caption = "支持文本、图片、语音、视频",
                        accent = Aqua
                    )
                }
                item {
                    SummaryStatCard(
                        title = "未读",
                        value = state.conversations.sumOf { it.unreadCount }.toString(),
                        caption = "最近会话提醒",
                        accent = Coral
                    )
                }
            }
        }
        item {
            if (state.selectedConversation == null) {
                EmptyStateCard(
                    title = "请选择一个会话",
                    detail = "从下方列表中选择联系人或群聊后开始聊天。"
                )
            } else {
                SelectedConversationPanel(conversation = state.selectedConversation)
            }
        }
        item {
            if (state.selectedConversation != null) {
                MessageBoard(
                    draft = state.messageDraft,
                    messages = state.selectedMessages,
                    onDraftChanged = onDraftChanged,
                    onSendTextMessage = onSendTextMessage,
                    onSendMediaMessage = onSendMediaMessage
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "最近会话",
                    style = MaterialTheme.typography.titleLarge
                )
                StatusChip(
                    text = "${state.conversations.size} 个会话",
                    accent = SkyBlue
                )
            }
        }
        items(state.conversations, key = ConversationSummary::id) { conversation ->
            ConversationCard(
                conversation = conversation,
                selected = conversation.id == state.selectedConversationId,
                onClick = { onSelectConversation(conversation.id) }
            )
        }
    }
}

@Composable
private fun SelectedConversationPanel(conversation: ConversationSummary?) {
    if (conversation == null) return
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InitialBadge(
                text = conversation.title.take(1),
                accent = if (conversation.isGroup) Coral else SkyBlue
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = conversation.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusChip(
                        text = if (conversation.isGroup) "群聊" else "单聊",
                        accent = Aqua
                    )
                    if (conversation.memberCount > 1) {
                        StatusChip(
                            text = "${conversation.memberCount} 人",
                            accent = Coral
                        )
                    }
                }
                Text(
                    text = conversation.preview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MessageBoard(
    draft: String,
    messages: List<ChatMessage>,
    onDraftChanged: (String) -> Unit,
    onSendTextMessage: () -> Unit,
    onSendMediaMessage: (MessageKind) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "聊天记录",
                    style = MaterialTheme.typography.titleLarge
                )
                StatusChip(text = "在线", accent = SkyBlue)
            }
            if (messages.isEmpty()) {
                EmptyStateCard(
                    title = "还没有消息",
                    detail = "发送一条消息后，这里会显示聊天内容。"
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    messages.forEach { message ->
                        MessageBubble(message = message)
                    }
                }
            }
            OutlinedTextField(
                value = draft,
                onValueChange = onDraftChanged,
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(22.dp),
                label = { Text("输入消息") },
                placeholder = { Text("说点什么") }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(MessageKind.entries.filter { it != MessageKind.TEXT }) { kind ->
                        Button(onClick = { onSendMediaMessage(kind) }) {
                            Text(kind.toActionLabel())
                        }
                    }
                }
                Button(onClick = onSendTextMessage) {
                    Text("发送")
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isSelf = message.authorRole == AuthorRole.SELF
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isSelf) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 320.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelf) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = if (isSelf) 24.dp else 8.dp,
                bottomEnd = if (isSelf) 8.dp else 24.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusChip(
                        text = message.kind.toChineseLabel(),
                        accent = if (isSelf) SkyBlue else Aqua
                    )
                    Text(
                        text = message.author,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                MessageBody(message = message)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = message.timestamp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = message.deliveryStatus.toChineseLabel(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (message.deliveryStatus == MessageDeliveryStatus.FAILED) Coral else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBody(message: ChatMessage) {
    when (message.kind) {
        MessageKind.TEXT -> {
            Text(
                text = message.body,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        MessageKind.IMAGE -> MediaPreviewCard("图片", message.body, SkyBlue)
        MessageKind.AUDIO -> MediaPreviewCard("语音", message.body, Aqua)
        MessageKind.VIDEO -> MediaPreviewCard("视频", message.body, Coral)
    }
}

@Composable
private fun MediaPreviewCard(
    title: String,
    subtitle: String,
    accent: Color
) {
    Surface(
        color = accent.copy(alpha = 0.12f),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = accent
                )
            }
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ConversationCard(
    conversation: ConversationSummary,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InitialBadge(
                text = conversation.title.take(1),
                accent = if (conversation.isGroup) Coral else SkyBlue
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = conversation.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = conversation.timestamp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = conversation.preview,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            if (conversation.unreadCount > 0) {
                Surface(
                    color = Coral,
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text(
                        text = conversation.unreadCount.toString(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
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
            .padding(top = 4.dp, bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
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

internal fun MessageKind.toChineseLabel(): String {
    return when (this) {
        MessageKind.TEXT -> "文本"
        MessageKind.IMAGE -> "图片"
        MessageKind.AUDIO -> "语音"
        MessageKind.VIDEO -> "视频"
    }
}

private fun MessageDeliveryStatus.toChineseLabel(): String {
    return when (this) {
        MessageDeliveryStatus.LOCAL -> "本地"
        MessageDeliveryStatus.SENDING -> "发送中"
        MessageDeliveryStatus.SENT -> "已发送"
        MessageDeliveryStatus.DELIVERED -> "已送达"
        MessageDeliveryStatus.FAILED -> "失败"
    }
}

private fun MessageKind.toActionLabel(): String {
    return when (this) {
        MessageKind.TEXT -> "文本"
        MessageKind.IMAGE -> "图片"
        MessageKind.AUDIO -> "语音"
        MessageKind.VIDEO -> "视频"
    }
}
