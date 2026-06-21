package com.lamba.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaCanvas


@Composable
fun HomeScreen(
    onOpenAiChat: () -> Unit = {},
    onAddExpensesClick: () -> Unit = {}
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LambaCanvas)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LambaCanvas)
        ) {
            CarSummaryCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(333.dp)
            )


            AiChatPanel(
                onSwipeUp = onOpenAiChat,
                onMenuClick = {
                    isMenuOpen = true
                },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }

        if (isMenuOpen) {
            Sidebar(
                onClose = {
                    isMenuOpen = false
                },
                onAddExpensesClick = onAddExpensesClick
            )
        }
    }


}




















