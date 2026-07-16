package com.lamba.app.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.lamba.app.ui.theme.LambaAccentStrong
import com.lamba.app.ui.theme.LambaChatInk
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing

private val AiPanelTop = Color(0xFF8FB9B1)
private val AiPanelMintGlow = Color(0xFFD7F8EA)
private val AiPanelMid = Color(0xFF7DB7B7)
private val AiPanelTeal = Color(0xFF2D7F8C)
private val AiPanelDeep = Color(0xFF123A49)
private val AiPanelDark = Color(0xFF0D2630)

@Composable
fun AiChatPanel(
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit = {},
    onMenuClick: () -> Unit,
    onSendClick: (String) -> Unit,
    messages: List<ChatMessage>,
    isSending: Boolean = false,
    modifier: Modifier = Modifier,
    onDrag: (Float) -> Unit,
    onDragEnd: () -> Unit,
    expandProgress: Float = 0f
) {
    var messageText by remember { mutableStateOf("") }
    var localMessages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            localMessages = emptyList()
        }
    }
    val displayedMessages = messages + localMessages
    val listState = rememberLazyListState()
    val bottomAnchorIndex = displayedMessages.size + if (isSending) 1 else 0
    LaunchedEffect(bottomAnchorIndex) {
        listState.animateScrollToItem(bottomAnchorIndex)
    }
    LaunchedEffect(bottomAnchorIndex, expandProgress) {
        snapshotFlow { listState.layoutInfo.viewportEndOffset }
            .collect {
                listState.scrollToItem(bottomAnchorIndex)
            }
    }

    val statusBarPadding = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()

    val topSafePadding = lerp(
        start = 0.dp,
        stop = statusBarPadding,
        fraction = expandProgress
    )

    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.00f to AiPanelTop,
                        0.24f to AiPanelMid,
                        0.52f to AiPanelTeal,
                        0.78f to AiPanelDeep,
                        1.00f to AiPanelDark
                    )
                )
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        AiPanelMintGlow.copy(alpha = 0.75f),
                        AiPanelMintGlow.copy(alpha = 0.22f),
                        Color.Transparent
                    ),
                    center = Offset(780f, 190f),
                    radius = 520f
                )
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE8FFF4).copy(alpha = 0.62f),
                        Color(0xFFBFEFE1).copy(alpha = 0.24f),
                        Color.Transparent
                    ),
                    center = Offset(90f, 650f),
                    radius = 520f
                )
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0A2230).copy(alpha = 0.42f),
                        Color.Transparent
                    ),
                    center = Offset(760f, 1050f),
                    radius = 620f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topSafePadding)
                .padding(horizontal = LambaSpacing.ScreenHorizontal)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(onSwipeUp, onSwipeDown, onDrag, onDragEnd) {
                        var totalDragY = 0f
                        detectVerticalDragGestures(
                            onDragStart = {
                                totalDragY = 0f
                            },
                            onVerticalDrag = { _, dragAmount ->
                                totalDragY += dragAmount
                                onDrag(dragAmount)
                            },
                            onDragEnd = {
                                onDragEnd()
                                when {
                                    totalDragY < -64f -> onSwipeUp()
                                    totalDragY > 64f -> onSwipeDown()
                                }
                            },
                            onDragCancel = {
                                onDragEnd()
                            }
                        )
                    }
                    .padding(top = 12.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(LambaAccentStrong.copy(alpha = 0.75f)),
                    contentAlignment = Alignment.Center
                ) {
                    AiSparkleIcon()
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "LAMBA AI",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LambaChatInk,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(54.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(LambaRadius.Pill))
                        .background(Color.Gray.copy(alpha = 0.45f))
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = LambaChatInk
                    )
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(displayedMessages) { message ->
                    if (message.isUser) {
                        UserBubble(message.text)
                    } else AiBubble(message.text)
                    Spacer(modifier = Modifier.height(8.dp))

                }
                if (isSending) {
                    item {
                        AiBubble("Thinking...")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(1.dp))
                }
            }

            ChatInput(
                value = messageText,
                onValueChange = { messageText = it },
                onSendClick = {
                    val textToSend = messageText.trim()
                    if (textToSend.isNotEmpty()) {
                        localMessages = localMessages + ChatMessage(
                            text = textToSend,
                            isUser = true
                        )
                        onSendClick(textToSend)
                        messageText = ""
                    }
                },
                modifier = Modifier.padding(bottom = 22.dp),
            )
        }
    }
}

@Composable
private fun AiSparkleIcon(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(16.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val longRadius = size.minDimension * 0.36f
        val shortRadius = size.minDimension * 0.18f
        val color = Color(0xFFB7FF5A)
        val strokeWidth = 2.dp.toPx()

        drawLine(
            color = color,
            start = Offset(center.x, center.y - longRadius),
            end = Offset(center.x, center.y + longRadius),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(center.x - longRadius, center.y),
            end = Offset(center.x + longRadius, center.y),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(center.x - shortRadius, center.y - shortRadius),
            end = Offset(center.x + shortRadius, center.y + shortRadius),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(center.x - shortRadius, center.y + shortRadius),
            end = Offset(center.x + shortRadius, center.y - shortRadius),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
