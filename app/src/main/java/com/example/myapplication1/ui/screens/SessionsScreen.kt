package com.example.myapplication1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication1.domain.model.AuthorRole
import com.example.myapplication1.domain.model.ChatMessage
import com.example.myapplication1.domain.model.ConversationSummary
import com.example.myapplication1.domain.model.DeliveryCheckpoint
import com.example.myapplication1.domain.model.MessageDeliveryStatus
import com.example.myapplication1.domain.model.MessageKind
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.BackendEndpointCard
import com.example.myapplication1.ui.components.DetailLine
import com.example.myapplication1.ui.components.EmptyStateCard
import com.example.myapplication1.ui.components.FillPanel
import com.example.myapplication1.ui.components.InitialBadge
import com.example.myapplication1.ui.components.ReadinessChip
import com.example.myapplication1.ui.components.StatusChip
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.components.TimelineCard
import com.example.myapplication1.ui.components.accent
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue
import com.example.myapplication1.ui.theme.SlateBlue
import com.example.myapplication1.ui.theme.Steel

@Composable
fun SessionsScreen(
    state: AppUiState,
    onSelectConversation: (String) -> Unit,
    onDraftChanged: (String) -> Unit,
    onSendTextMessage: () -> Unit,
    onSendMediaMessage: (MessageKind) -> Unit
) {
    val selectedConversation = state.selectedConversation

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "消息会话",
                subtitle = "前端已补成后端可联调的工作台：包含媒体卡片、群聊信息、状态回执和接口接入提示。"
            )
        }
        item {
            WorkspaceHero(state = state)
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    SummaryStatCard(
                        title = "当前会话",
                        value = selectedConversation?.title ?: "未选择",
                        caption = "当前环境：${state.profile?.environmentLabel ?: "未连接"}",
                        accent = SkyBlue
                    )
                }
                item {
                    SummaryStatCard(
                        title = "消息总数",
                        value = state.selectedMessages.size.toString(),
                        caption = "支持文本、图片、语音、视频四类卡片",
                        accent = Aqua
                    )
                }
                item {
                    SummaryStatCard(
                        title = "交付检查项",
                        value = state.checkpoints.count { it.status.name == "READY" }.toString(),
                        caption = "等待后端按接口清单逐项替换",
                        accent = Coral
                    )
                }
            }
        }
        item {
            DeliveryStrip(checkpoints = state.checkpoints.take(4))
        }
        item {
            if (selectedConversation == null) {
                EmptyStateCard(
                    title = "尚未选择会话",
                    detail = "选中一个会话后，这里会显示群聊路由、成员规模、媒体能力和后端接入重点。"
                )
            } else {
                SelectedConversationPanel(conversation = selectedConversation)
            }
        }
        item {
            if (selectedConversation != null) {
                ConversationHandoffPanel(state = state, conversation = selectedConversation)
            }
        }
        item {
            if (selectedConversation != null) {
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
                    text = "最近联调动态",
                    style = MaterialTheme.typography.titleLarge
                )
                StatusChip(text = "前端壳已就绪", accent = Aqua)
            }
        }
        items(state.workspaceUpdates.take(2), key = { it.title }) { update ->
            TimelineCard(update = update)
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
private fun WorkspaceHero(state: AppUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(30.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            SlateBlue.copy(alpha = 0.98f),
                            SkyBlue.copy(alpha = 0.94f),
                            Aqua.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusChip(text = state.profile?.releaseStage ?: "工作台未初始化", accent = Coral)
                StatusChip(text = state.profile?.peerClient ?: "PC 对端", accent = Color.White)
            }
            Text(
                text = "联调消息工作台",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "网关 ${state.profile?.gateway ?: "未连接"} · API ${state.profile?.backendBaseUrl ?: "未配置"}",
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

@Composable
private fun DeliveryStrip(checkpoints: List<DeliveryCheckpoint>) {
    if (checkpoints.isEmpty()) {
        EmptyStateCard(
            title = "暂无交付检查项",
            detail = "建议按登录、会话、消息、通话、管理五个域整理对接清单。"
        )
        return
    }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(checkpoints, key = { it.title }) { checkpoint ->
            Card(
                modifier = Modifier.width(250.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = checkpoint.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        ReadinessChip(status = checkpoint.status)
                    }
                    Text(
                        text = checkpoint.owner,
                        style = MaterialTheme.typography.labelLarge,
                        color = checkpoint.status.accent()
                    )
                    Text(
                        text = checkpoint.detail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedConversationPanel(conversation: ConversationSummary) {
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
                accent = conversation.integrationStatus.accent()
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    ReadinessChip(status = conversation.integrationStatus)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusChip(
                        text = if (conversation.isGroup) "群聊" else "单聊",
                        accent = Aqua
                    )
                    StatusChip(
                        text = "${conversation.memberCount} 人",
                        accent = Coral
                    )
                    StatusChip(
                        text = conversation.kind.toChineseLabel(),
                        accent = SkyBlue
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
private fun ConversationHandoffPanel(
    state: AppUiState,
    conversation: ConversationSummary
) {
    FillPanel(accent = conversation.integrationStatus.accent()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "后端接入信息",
                style = MaterialTheme.typography.titleLarge
            )
            DetailLine(label = "会话路由键", value = conversation.routingKey)
            DetailLine(label = "会话模式", value = if (conversation.isGroup) "群聊 / 支持成员管理" else "单聊 / 点对点")
            DetailLine(label = "消息种类", value = "TEXT / IMAGE / AUDIO / VIDEO")
            DetailLine(label = "建议接口", value = "/conversations, /messages/send, /messages/history")
            if (conversation.isGroup) {
                DetailLine(label = "群聊补充", value = "需要 groupId、role、muteState、memberOrder")
            }
            state.backendEndpoints.take(2).forEach { endpoint ->
                BackendEndpointCard(endpoint = endpoint)
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
                        text = "协议字段：conversationId / kind / content / timestamp / senderId / messageId",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusChip(text = "媒体卡片已补齐", accent = Coral)
            }
            if (messages.isEmpty()) {
                EmptyStateCard(
                    title = "当前没有消息",
                    detail = "先发送一条文本或媒体消息，验证气泡样式、状态变更和会话摘要更新。"
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
                label = { Text("输入文本消息") },
                placeholder = { Text("例如：消息页已完成媒体卡片，等待后端返回 attachmentUrl") }
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
    val containerColor = if (isSelf) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isSelf) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 320.dp),
            colors = CardDefaults.cardColors(containerColor = containerColor),
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

        MessageKind.IMAGE -> MediaPreviewCard(
            title = "图片消息",
            subtitle = message.body,
            accent = SkyBlue,
            lines = listOf("等待真实 attachmentUrl", "支持缩略图 / 原图 / 下载进度")
        )

        MessageKind.AUDIO -> MediaPreviewCard(
            title = "语音消息",
            subtitle = message.body,
            accent = Aqua,
            lines = listOf("时长、波形和播放状态待接后端", "后续补已听回执")
        )

        MessageKind.VIDEO -> MediaPreviewCard(
            title = "视频消息",
            subtitle = message.body,
            accent = Coral,
            lines = listOf("视频封面、时长、上传状态已预留", "支持点击进入全屏播放器")
        )
    }
}

@Composable
private fun MediaPreviewCard(
    title: String,
    subtitle: String,
    accent: Color,
    lines: List<String>
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
                    .height(88.dp)
                    .background(accent.copy(alpha = 0.16f), RoundedCornerShape(14.dp)),
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
                style = MaterialTheme.typography.bodyLarge
            )
            lines.forEach { line ->
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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
                    ReadinessChip(status = conversation.integrationStatus)
                }
                Text(
                    text = conversation.preview,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "路由 ${conversation.routingKey}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
