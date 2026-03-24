package com.example.myapplication1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.myapplication1.ui.components.DetailLine
import com.example.myapplication1.ui.components.EmptyStateCard
import com.example.myapplication1.ui.components.FlowStep
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
                title = "音视频通话",
                subtitle = "通话页已拆成拨号、来电、通话中和会议准备四类展示，后端接入后只需替换实时状态流。"
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    SummaryStatCard(
                        title = "活跃会话",
                        value = state.calls.count { it.state == CallState.LIVE || it.state == CallState.RINGING }.toString(),
                        caption = "后续接入 SIP / RTC 状态流",
                        accent = Coral
                    )
                }
                item {
                    SummaryStatCard(
                        title = "会议场景",
                        value = state.calls.count { it.participants > 2 }.toString(),
                        caption = "含多人会议和群组拉会",
                        accent = SkyBlue
                    )
                }
                item {
                    SummaryStatCard(
                        title = "接通目标",
                        value = "96%",
                        caption = "可与后台统计共享一套指标口径",
                        accent = Aqua
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "通话联调流程",
                        style = MaterialTheme.typography.titleLarge
                    )
                    FlowStep(1, "领取 SIP 启动信息", "前端等待后端返回域名、账号、鉴权和 TURN/STUN。", accent = SkyBlue)
                    FlowStep(2, "进入拨号或来电页", "展示呼叫者、通话类型、倒计时和接听/拒绝操作。", accent = Coral)
                    FlowStep(3, "挂载实时媒体状态", "静音、摄像头、扬声器、网络评分和弱网提示。", accent = Aqua)
                    FlowStep(4, "扩展到多人会议", "主持人控制、成员宫格、举手状态和会议记录。", accent = SkyBlue)
                }
            }
        }
        item {
            CallPreviewPanel()
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(28.dp)
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
                    SignalMeter(label = "音频链路预估", progress = 0.74f, accent = Coral)
                    SignalMeter(label = "多人会议准备度", progress = 0.42f, accent = Aqua)
                }
            }
        }
        item {
            if (state.calls.isEmpty()) {
                EmptyStateCard(
                    title = "暂无通话任务",
                    detail = "后端返回通话计划或实时会话后，这里会显示来电、拨号中和会议中的状态卡。"
                )
            }
        }
        items(state.calls, key = CallSession::id) { call ->
            CallCard(call = call)
        }
    }
}

@Composable
private fun CallPreviewPanel() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "页面壳预览",
                style = MaterialTheme.typography.titleLarge
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(
                    listOf(
                        Triple("拨号中", "显示目标、倒计时、取消按钮", SkyBlue),
                        Triple("来电页", "显示接听、拒绝、静音预设", Coral),
                        Triple("通话中", "显示波形、码率、控制栏", Aqua),
                        Triple("多人会议", "显示成员宫格、举手、主持人控制", Steel)
                    )
                ) { item ->
                    Surface(
                        color = item.third.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(88.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.first,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = item.third
                                )
                            }
                            Text(
                                text = item.second,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CallCard(call: CallSession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp)
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
                        text = call.scenarioLabel,
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
                    text = "参与人数 ${call.participants}",
                    accent = SkyBlue
                )
                StatusChip(
                    text = if (call.direction == CallDirection.INCOMING) "呼入" else "呼出",
                    accent = Coral
                )
            }
            DetailLine(label = "当前指标", value = call.durationLabel)
            DetailLine(label = "下一步接入", value = call.nextAction)
            SignalMeter(
                label = "网络评分",
                progress = call.networkScore,
                accent = call.state.color()
            )
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
