package com.example.myapplication1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication1.domain.model.AdminAlert
import com.example.myapplication1.domain.model.AdminMetric
import com.example.myapplication1.domain.model.PcInteropContract
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.components.AccentPanel
import com.example.myapplication1.ui.components.SignalMeter
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue

@Composable
fun AdminScreen(state: AppUiState) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionHeader(
                title = "后台管理",
                subtitle = "把统计、协议、告警和审计入口做成同一套展示语言，便于最终答辩演示。"
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.metrics, key = AdminMetric::title) { metric ->
                    SummaryStatCard(
                        title = metric.title,
                        value = metric.value,
                        caption = metric.insight,
                        accent = when (metric.title) {
                            "在线用户" -> Aqua
                            "今日消息量" -> SkyBlue
                            else -> Coral
                        }
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "运行观测",
                        style = MaterialTheme.typography.titleLarge
                    )
                    SignalMeter(label = "消息投递稳定度", progress = 0.81f, accent = SkyBlue)
                    SignalMeter(label = "SIP 注册准备度", progress = 0.56f, accent = Coral)
                    SignalMeter(label = "PC 互通协议完成度", progress = 0.67f, accent = Aqua)
                }
            }
        }
        item {
            AccentPanel(
                title = "服务端建设范围",
                subtitle = "这部分用于课程设计答辩时解释系统后台要承担的职责。",
                lines = listOf(
                    "用户与群组生命周期管理",
                    "消息审计与投递统计",
                    "通话详单和丢包趋势",
                    "按用户、会话、时间和媒体类型检索"
                ),
                accent = Coral
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "PC 互通协议字段",
                        style = MaterialTheme.typography.titleLarge
                    )
                    PcInteropContract.messageFields.forEach { field ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = field.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = field.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "示例：${field.example}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
        items(state.alerts, key = AdminAlert::title) { alert ->
            AlertCard(alert = alert)
        }
    }
}

@Composable
private fun AlertCard(alert: AdminAlert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = alert.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = alert.detail,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
