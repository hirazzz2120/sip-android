package com.example.myapplication1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.myapplication1.ui.LinkTalkApp
import com.example.myapplication1.ui.app.AppViewModel
import com.example.myapplication1.ui.theme.MyApplication1Theme

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by viewModel.uiState.collectAsState()
            MyApplication1Theme {
                LinkTalkApp(
                    state = state,
                    onEnterWorkspace = viewModel::enterWorkspace,
                    onSelectConversation = viewModel::selectConversation,
                    onDraftChanged = viewModel::updateDraft,
                    onSendTextMessage = viewModel::sendTextMessage,
                    onSendMediaMessage = viewModel::sendMediaPlaceholder
                )
            }
        }
    }
}
