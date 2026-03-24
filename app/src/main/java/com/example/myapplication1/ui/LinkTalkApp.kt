package com.example.myapplication1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication1.domain.model.MessageKind
import com.example.myapplication1.ui.app.AppUiState
import com.example.myapplication1.ui.navigation.AppDestination
import com.example.myapplication1.ui.screens.AdminScreen
import com.example.myapplication1.ui.screens.CallsScreen
import com.example.myapplication1.ui.screens.ContactsScreen
import com.example.myapplication1.ui.screens.LoginScreen
import com.example.myapplication1.ui.screens.SessionsScreen
import com.example.myapplication1.ui.theme.Aqua
import com.example.myapplication1.ui.theme.Coral
import com.example.myapplication1.ui.theme.SkyBlue
import com.example.myapplication1.ui.theme.SlateBlue

@Composable
fun LinkTalkApp(
    state: AppUiState,
    onEnterWorkspace: () -> Unit,
    onSelectConversation: (String) -> Unit,
    onDraftChanged: (String) -> Unit,
    onSendTextMessage: () -> Unit,
    onSendMediaMessage: (MessageKind) -> Unit
) {
    if (!state.isAuthenticated) {
        LoginScreen(onEnterWorkspace = onEnterWorkspace)
        return
    }

    val navController = rememberNavController()
    val destinations = AppDestination.entries
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentAppDestination = destinations.firstOrNull { destination ->
        currentDestination?.hierarchy?.any { it.route == destination.route } == true
    } ?: AppDestination.SESSIONS

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            WorkspaceTopBar(
                state = state,
                destination = currentAppDestination
            )
        },
        bottomBar = {
            WorkspaceBottomBar(
                destinations = destinations.toList(),
                currentDestination = currentDestination,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .systemBarsPadding()
        ) {
            NavHost(
                navController = navController,
                startDestination = AppDestination.SESSIONS.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(AppDestination.SESSIONS.route) {
                    SessionsScreen(
                        state = state,
                        onSelectConversation = onSelectConversation,
                        onDraftChanged = onDraftChanged,
                        onSendTextMessage = onSendTextMessage,
                        onSendMediaMessage = onSendMediaMessage
                    )
                }
                composable(AppDestination.CONTACTS.route) {
                    ContactsScreen(state = state)
                }
                composable(AppDestination.CALLS.route) {
                    CallsScreen(state = state)
                }
                composable(AppDestination.ADMIN.route) {
                    AdminScreen(state = state)
                }
            }
        }
    }
}

@Composable
private fun WorkspaceTopBar(
    state: AppUiState,
    destination: AppDestination
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            SlateBlue.copy(alpha = 0.98f),
                            SkyBlue.copy(alpha = 0.92f),
                            Aqua.copy(alpha = 0.88f)
                        )
                    )
                )
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "联聊工作区",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = destination.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Surface(
                    color = Coral.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = state.profile?.releaseStage ?: "未初始化",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WorkspaceMeta(label = "当前用户", value = state.profile?.currentUserName ?: "安卓终端")
                WorkspaceMeta(label = "SIP", value = state.profile?.sipAccount ?: "未配置")
                WorkspaceMeta(label = "环境", value = state.profile?.environmentLabel ?: "本地环境")
            }
        }
    }
}

@Composable
private fun RowScope.WorkspaceMeta(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun WorkspaceBottomBar(
    destinations: List<AppDestination>,
    currentDestination: androidx.navigation.NavDestination?,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(26.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                destinations.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onNavigate(destination.route) },
                        alwaysShowLabel = true,
                        icon = {
                            Surface(
                                color = if (selected) SkyBlue.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = destination.marker,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selected) SkyBlue else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        label = {
                            Text(
                                text = destination.label,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }
    }
}
