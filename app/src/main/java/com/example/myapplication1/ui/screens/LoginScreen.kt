package com.example.myapplication1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication1.ui.components.DetailLine
import com.example.myapplication1.ui.components.StatusChip
import com.example.myapplication1.ui.components.SummaryStatCard
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue
import com.example.myapplication1.ui.theme.SlateBlue
import com.example.myapplication1.ui.theme.Steel

@Composable
fun LoginScreen(
    onEnterWorkspace: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SlateBlue, SkyBlue.copy(alpha = 0.92f), Aqua.copy(alpha = 0.78f))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Coral.copy(alpha = 0.28f), androidx.compose.ui.graphics.Color.Transparent)
                    )
                )
                .align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusChip(
                        text = "Android 联调入口",
                        accent = Coral
                    )
                    StatusChip(
                        text = "前端可交付后端",
                        accent = androidx.compose.ui.graphics.Color.White
                    )
                }
                Text(
                    text = "联聊",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "面向 SIP 音视频、即时通信和后台管理的 Android 工作台。当前版本已经从静态展示推进到联调壳，适合直接拿给后端对接口。",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(
                        listOf(
                            Triple("消息", "4 类", "文本、图片、语音、视频"),
                            Triple("通话", "4 态", "拨号、来电、通话中、会议"),
                            Triple("后台", "6 接口", "登录、消息、群聊、SIP、统计")
                        )
                    ) { item ->
                        SummaryStatCard(
                            title = item.first,
                            value = item.second,
                            caption = item.third,
                            accent = when (item.first) {
                                "消息" -> SkyBlue
                                "通话" -> Coral
                                else -> Aqua
                            }
                        )
                    }
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.14f)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "联调环境",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        DetailLine(label = "网关", value = "ws://10.0.2.2:8080")
                        DetailLine(label = "后端 API", value = "http://10.0.2.2:8080/api")
                        DetailLine(label = "当前阶段", value = "前端壳已完成，等待真实认证与消息接口")
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(32.dp),
                tonalElevation = 10.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "入口检查",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        EntryStep(index = "01", title = "入口与主题", detail = "登录页、品牌标题、环境信息和启动动线已收口。")
                        EntryStep(index = "02", title = "主工作区", detail = "消息、联系人、通话、管理四个主标签页已成型。")
                        EntryStep(index = "03", title = "后端交付", detail = "接口清单、消息字段、群聊和通话状态位已准备完毕。")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusChip(text = "适合演示", accent = Aqua)
                        StatusChip(text = "可进入联调", accent = Steel)
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onEnterWorkspace,
                        colors = ButtonDefaults.buttonColors(containerColor = SlateBlue),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("进入联调工作台")
                    }
                }
            }
        }
    }
}

@Composable
private fun EntryStep(
    index: String,
    title: String,
    detail: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = SkyBlue.copy(alpha = 0.14f),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = index,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                color = SkyBlue
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
