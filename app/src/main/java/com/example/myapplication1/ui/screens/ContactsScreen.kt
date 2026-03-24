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
import com.example.myapplication1.ui.components.EmptyStateCard
import com.example.myapplication1.ui.components.InitialBadge
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
                subtitle = "快速找到人，并决定是发消息还是发起通话。"
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    SummaryStatCard(
                        title = "在线",
                        value = onlineCount.toString(),
                        caption = "可立即联系",
                        accent = Aqua
                    )
                }
                item {
                    SummaryStatCard(
                        title = "忙碌",
                        value = busyCount.toString(),
                        caption = "稍后再联系",
                        accent = Coral
                    )
                }
                item {
                    SummaryStatCard(
                        title = "离线",
                        value = offlineCount.toString(),
                        caption = "可留言",
                        accent = Steel
                    )
                }
            }
        }
        item {
            if (state.contacts.isEmpty()) {
                EmptyStateCard(
                    title = "暂无联系人",
                    detail = "联系人上线后会出现在这里。"
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
                StatusChip(
                    text = if (contact.status == ContactStatus.OFFLINE) "留言" else "联系",
                    accent = if (contact.status == ContactStatus.OFFLINE) Steel else Aqua
                )
            }
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
