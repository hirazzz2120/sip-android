package com.example.myapplication1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication1.domain.model.AuthorRole
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.MessageDeliveryStatus
import com.example.myapplication1.domain.model.MessageKind
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.InitialBadge
import com.example.myapplication1.ui.components.StatusChip
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue
import com.example.myapplication1.ui.theme.SlateBlue

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
                title = "消息会话",
                subtitle = "模拟 Android、PC 与服务端之间的消息交互，用于先完成界面与交互层。"
            )
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(30.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    SlateBlue.copy(alpha = 0.96f),
                                    SkyBlue.copy(alpha = 0.92f),
                                    Aqua.copy(alpha = 0.9f)
                                )
                            )
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatusChip(text = "当前链路在线", accent = Coral)
                    Text(
                        text = "联调工作台",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "网关 ${state.profile?.gateway ?: "未连接"} · SIP ${state.profile?.sipAccount ?: "未配置"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "传输状态：${state.transportStatus}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    SummaryStatCard(
                        title = "当前会话",
                        value = state.selectedConversation?.title ?: "未选择",
                        caption = "已连接到 ${state.profile?.peerClient ?: "PC 对端"}",
                        accent = SkyBlue
                    )
                }
                item {
                    SummaryStatCard(
                        title = "消息总数",
                        value = state.selectedMessages.size.toString(),
                        caption = "包含文本、图片、语音和视频占位消息",
                        accent = Aqua
                    )
                }
                item {
                    SummaryStatCard(
                        title = "未读提醒",
                        value = state.conversations.sumOf { it.unreadCount }.toString(),
                        caption = "后续可接真实已读回执与离线消息",
                        accent = Coral
                    )
                }
            }
        }
        item {
            state.selectedConversation?.let { conversation ->
                SelectedConversationPanel(conversation = conversation)
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
                    text = "会话列表",
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
private fun SelectedConversationPanel(
    conversation: ConversationSummary
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(26.dp)
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
                accent = SkyBlue
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    StatusChip(
                        text = conversation.kind.toChineseLabel(),
                        accent = Coral
                    )
                }
                Text(
                    text = "最近一条：${conversation.preview}",
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
        shape = RoundedCornerShape(30.dp)
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
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "聊天窗口",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "协议字段：conversationId / kind / content / timestamp",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusChip(
                    text = "本地演示",
                    accent = Coral
                )
            }
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(24.dp)
                        )
                        .padding(18.dp)
                ) {
                    Text(
                        text = "当前没有消息，先发送一条文本或媒体消息，验证会话列表、气泡状态和回执展示。",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
                label = { Text("输入文本消息") },
                placeholder = { Text("例如：安卓端已完成中文 UI 设计，等待接入服务端") }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "媒体快捷发送",
                    style = MaterialTheme.typography.labelLarge
                )
                Button(onClick = onSendTextMessage) {
                    Text("发送文本")
                }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(MessageKind.entries.filter { it != MessageKind.TEXT }) { kind ->
                    Button(onClick = { onSendMediaMessage(kind) }) {
                        Text(kind.toActionLabel())
                    }
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
            modifier = Modifier.widthIn(max = 300.dp),
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                Text(
                    text = message.body,
                    style = MaterialTheme.typography.bodyLarge
                )
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
private fun ConversationCard(
    conversation: ConversationSummary,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = containerColor),
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusChip(
                        text = if (conversation.isGroup) "群聊" else "单聊",
                        accent = if (conversation.isGroup) Coral else Aqua
                    )
                    StatusChip(
                        text = conversation.kind.toChineseLabel(),
                        accent = SkyBlue
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
            .padding(top = 12.dp, bottom = 4.dp),
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(26.dp)
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
        MessageKind.TEXT -> "发送文本"
        MessageKind.IMAGE -> "发送图片"
        MessageKind.AUDIO -> "发送语音"
        MessageKind.VIDEO -> "发送视频"
    }
}
