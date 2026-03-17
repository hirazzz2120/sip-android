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
import androidx.compose.ui.unit.dp
import com.example.myapplication1.domain.model.CallDirection
import com.example.myapplication1.domain.model.CallSession
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.FlowStep
import com.example.myapplication1.ui.components.InitialBadge
import com.example.myapplication1.ui.components.SignalMeter
import com.example.myapplication1.ui.components.StatusChip
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue

@Composable
fun CallsScreen(state: AppUiState) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "音视频通话",
                subtitle = "这一页先把单人和多人通话的 UI 模型定下来，后续再接 SIP 和媒体流。"
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    SummaryStatCard(
                        title = "会议房间",
                        value = "402",
                        caption = "适合展示会议号、成员和入会状态",
                        accent = SkyBlue
                    )
                }
                item {
                    SummaryStatCard(
                        title = "活跃通话",
                        value = state.calls.count { it.state.name != "LAST_COMPLETED" }.toString(),
                        caption = "后续接入真实通话状态流",
                        accent = Coral
                    )
                }
                item {
                    SummaryStatCard(
                        title = "接通率目标",
                        value = "96%",
                        caption = "管理页和通话页可共享统计卡片组件",
                        accent = Aqua
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "单人到群组的通话流程",
                        style = MaterialTheme.typography.titleLarge
                    )
                    FlowStep(1, "注册 SIP 账号", "展示账号在线、鉴权结果和网络质量", accent = SkyBlue)
                    FlowStep(2, "发起或接听通话", "进入拨号中、来电和通话中页面", accent = Coral)
                    FlowStep(3, "建立媒体会话", "挂载摄像头、麦克风、静音和扬声器操作", accent = Aqua)
                    FlowStep(4, "扩展多人会议", "展示成员宫格、举手状态和主持人控制", accent = SkyBlue)
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "当前网络观测",
                        style = MaterialTheme.typography.titleLarge
                    )
                    SignalMeter(label = "信令连通性", progress = 0.88f, accent = SkyBlue)
                    SignalMeter(label = "语音链路预估", progress = 0.74f, accent = Coral)
                    SignalMeter(label = "多人会议准备度", progress = 0.42f, accent = Aqua)
                }
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
                    accent = if (call.direction == CallDirection.INCOMING) Coral else SkyBlue
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
                    accent = if (call.direction == CallDirection.INCOMING) Coral else Aqua
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusChip(
                    text = "参与人数 ${call.participants}",
                    accent = SkyBlue
                )
                StatusChip(
                    text = if (call.direction == CallDirection.INCOMING) "呼入" else "呼出",
                    accent = Coral
                )
            }
            Text(
                text = "建议页面：拨号中页、来电页、通话控制栏、多人宫格布局、结束通话总结卡片。",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun com.example.myapplication1.domain.model.CallState.toChineseLabel(): String {
    return when (this) {
        com.example.myapplication1.domain.model.CallState.READY -> "待发起"
        com.example.myapplication1.domain.model.CallState.SCHEDULED -> "待加入"
        com.example.myapplication1.domain.model.CallState.LAST_COMPLETED -> "最近完成"
    }
}
