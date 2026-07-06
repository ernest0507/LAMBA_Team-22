package com.lamba.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.lamba.app.data.cars.CarResponse
import com.lamba.app.ui.theme.LambaCanvas


@Composable
fun HomeScreen(
    car: CarResponse? = null,
    messages: List<ChatMessage> = emptyList(),
    isAssistantSending: Boolean = false,
    onOpenAiChat: () -> Unit = {},
    onAddExpensesClick: () -> Unit = {},
    onOpenHistory: () -> Unit = {},
    onOpenStatistics: () -> Unit = {},
    onOpenDocuments: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    onSendMessage: (String) -> Unit = {},
    onStartTripClick: () -> Unit = {},
    isTripActive: Boolean = false,
    tripStartedAtMillis: Long? = null,
    onTripHoldComplete: () -> Unit = {}
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    var chatExpandProgress by remember { mutableFloatStateOf(0f) }
    val carHeight = lerp(
        start = 430.dp,
        stop = 0.dp,
        fraction = chatExpandProgress
    )
    val dragDistancePx = with(LocalDensity.current) { 285.dp.toPx() }


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
                car = car,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(carHeight),
                onStartTripClick = onStartTripClick,
                isTripActive = isTripActive,
                tripStartedAtMillis = tripStartedAtMillis,
                onTripHoldComplete = onTripHoldComplete
            )


            AiChatPanel(
                messages = messages,
                isSending = isAssistantSending,
                expandProgress = chatExpandProgress,
                onDrag = { dragAmount ->
                    chatExpandProgress = (chatExpandProgress - dragAmount / dragDistancePx)
                        .coerceIn(0f, 1f)
                },
                onDragEnd = {
                    if (chatExpandProgress > 0.35f)  chatExpandProgress = 1f else chatExpandProgress = 0f
                },
                onSwipeUp = {
                    chatExpandProgress = 1f
                },
                onSwipeDown = {
                    chatExpandProgress = 0f
                },
                onMenuClick = {
                    isMenuOpen = true
                },
                onSendClick = onSendMessage,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }

        if (isMenuOpen) {
            Sidebar(
                car = car,
                onClose = {
                    isMenuOpen = false
                },
                onAddExpensesClick = {
                    isMenuOpen = false
                    onAddExpensesClick()
                },
                onHistoryClick = {
                    isMenuOpen = false
                    onOpenHistory()
                },
                onStatisticsClick = {
                    isMenuOpen = false
                    onOpenStatistics()
                },
                onDocumentsClick = {
                    isMenuOpen = false
                    onOpenDocuments()
                },
                onProfileClick = {
                    isMenuOpen = false
                    onOpenProfile()
                }
            )
        }
    }


}




















