package com.example.myapplication1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                destinations.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = destination.marker,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
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
