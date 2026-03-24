package com.example.myapplication1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication1.domain.model.Contact
import com.example.myapplication1.domain.model.ContactStatus
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.DetailLine
import com.example.myapplication1.ui.components.EmptyStateCard
import com.example.myapplication1.ui.components.InitialBadge
import com.example.myapplication1.ui.components.ReadinessChip
import com.example.myapplication1.ui.components.StatusChip
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue
import com.example.myapplication1.ui.theme.Steel

@Composable
fun ContactsScreen(state: AppUiState) {
    val onlineCount = state.contacts.count { it.status == ContactStatus.ONLINE }
    val busyCount = state.contacts.count { it.status == ContactStatus.BUSY }
    val offlineCount = state.contacts.count { it.status == ContactStatus.OFFLINE }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "联系人",
                subtitle = "联系人页已经按后端联调场景拆成目录、能力标签、响应窗口和快速动作，不再只是静态通讯录。"
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    SummaryStatCard(
                        title = "在线",
                        value = onlineCount.toString(),
                        caption = "可直接发消息或发起通话",
                        accent = Aqua
                    )
                }
                item {
                    SummaryStatCard(
                        title = "忙碌",
                        value = busyCount.toString(),
                        caption = "适合显示免打扰和会议状态",
                        accent = Coral
                    )
                }
                item {
                    SummaryStatCard(
                        title = "离线",
                        value = offlineCount.toString(),
                        caption = "后续可接离线消息和推送",
                        accent = Steel
                    )
                }
            }
        }
        item {
            HighlightPanel(
                title = "后端需要补的联系人能力",
                lines = listOf(
                    "支持按部门、标签、最近互动和在线状态联合筛选",
                    "返回联系人支持的能力集，例如 SIP、媒体、桥接、审计",
                    "补充头像、备注名、最近活跃时间和权限范围"
                )
            )
        }
        item {
            if (state.contacts.isEmpty()) {
                EmptyStateCard(
                    title = "暂无联系人",
                    detail = "后端返回联系人列表后，这里会展示分组、标签和快速操作入口。"
                )
            }
        }
        items(state.contacts, key = Contact::id) { contact ->
            ContactCard(contact = contact)
        }
    }
}

@Composable
private fun ContactCard(contact: Contact) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InitialBadge(
                    text = contact.name.take(1),
                    accent = contact.status.color()
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = contact.department,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusChip(
                    text = contact.status.toChineseLabel(),
                    accent = contact.status.color()
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusChip(text = contact.capability, accent = SkyBlue)
                ReadinessChip(
                    status = when (contact.status) {
                        ContactStatus.ONLINE -> com.example.myapplication1.domain.model.ReadinessStatus.READY
                        ContactStatus.BUSY -> com.example.myapplication1.domain.model.ReadinessStatus.IN_PROGRESS
                        ContactStatus.OFFLINE -> com.example.myapplication1.domain.model.ReadinessStatus.BLOCKED
                    }
                )
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(contact.roleTags) { tag ->
                    StatusChip(
                        text = tag,
                        accent = if (contact.status == ContactStatus.OFFLINE) Steel else Aqua
                    )
                }
            }
            DetailLine(label = "建议响应窗口", value = contact.responseWindow)
            DetailLine(
                label = "建议动作",
                value = if (contact.status == ContactStatus.OFFLINE) {
                    "留言、查看最近记录、等待上线"
                } else {
                    "发消息、单呼、拉群、确认接口字段"
                }
            )
        }
    }
}

private fun ContactStatus.color(): Color {
    return when (this) {
        ContactStatus.ONLINE -> Aqua
        ContactStatus.BUSY -> Coral
        ContactStatus.OFFLINE -> Steel
    }
}

private fun ContactStatus.toChineseLabel(): String {
    return when (this) {
        ContactStatus.ONLINE -> "在线"
        ContactStatus.BUSY -> "忙碌"
        ContactStatus.OFFLINE -> "离线"
    }
}
