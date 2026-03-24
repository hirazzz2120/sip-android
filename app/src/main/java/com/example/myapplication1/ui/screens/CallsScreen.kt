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
import com.example.myapplication1.domain.model.CallDirection
import com.example.myapplication1.domain.model.CallSession
import com.example.myapplication1.domain.model.CallState
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.EmptyStateCard
import com.example.myapplication1.ui.components.InitialBadge
import com.example.myapplication1.ui.components.SignalMeter
import com.example.myapplication1.ui.components.StatusChip
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue
import com.example.myapplication1.ui.theme.Steel

@Composable
fun CallsScreen(state: AppUiState) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "通话",
                subtitle = "当前通话状态和最近通话一目了然。"
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    SummaryStatCard(
                        title = "进行中",
                        value = state.calls.count { it.state == CallState.LIVE }.toString(),
                        caption = "当前活跃通话",
                        accent = Aqua
                    )
                }
                item {
                    SummaryStatCard(
                        title = "来电",
                        value = state.calls.count { it.state == CallState.RINGING }.toString(),
                        caption = "等待处理",
                        accent = Coral
                    )
                }
                item {
                    SummaryStatCard(
                        title = "会议",
                        value = state.calls.count { it.participants > 2 }.toString(),
                        caption = "多人通话",
                        accent = SkyBlue
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "网络状态",
                        style = MaterialTheme.typography.titleLarge
                    )
                    SignalMeter(label = "通话稳定度", progress = 0.82f, accent = SkyBlue)
                }
            }
        }
        item {
            if (state.calls.isEmpty()) {
                EmptyStateCard(
                    title = "暂无通话",
                    detail = "开始通话后，这里会显示状态和最近记录。"
                )
            }
        }
        items(state.calls, key = CallSession::id) { call ->
            CallCard(call = call)
        }
    }
}

@Composable
private fun CallCard(call: CallSession) {
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
                    text = if (call.participants > 2) "群" else "单",
                    accent = call.state.color()
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = call.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = call.durationLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusChip(
                    text = call.state.toChineseLabel(),
                    accent = call.state.color()
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusChip(
                    text = if (call.direction == CallDirection.INCOMING) "呼入" else "呼出",
                    accent = Coral
                )
                StatusChip(
                    text = "${call.participants} 人",
                    accent = SkyBlue
                )
            }
        }
    }
}

private fun CallState.toChineseLabel(): String {
    return when (this) {
        CallState.READY -> "待发起"
        CallState.RINGING -> "来电中"
        CallState.LIVE -> "通话中"
        CallState.SCHEDULED -> "待加入"
        CallState.LAST_COMPLETED -> "最近完成"
    }
}

private fun CallState.color(): Color {
    return when (this) {
        CallState.READY -> SkyBlue
        CallState.RINGING -> Coral
        CallState.LIVE -> Aqua
        CallState.SCHEDULED -> Steel
        CallState.LAST_COMPLETED -> SkyBlue
    }
}
