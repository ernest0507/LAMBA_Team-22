package com.lamba.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
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
    onMenuClick: () -> Unit,
    onSendClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }

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
                .padding(horizontal = LambaSpacing.ScreenHorizontal)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                    Text(
                        text = "✦",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFB7FF5A)
                    )
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
                        .padding(bottom = 40.dp)
                        .width(54.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(LambaRadius.Pill))
                        .background(Color.Gray.copy(alpha = 0.45f))
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { _, dragAmount ->
                                if (dragAmount < -16f) {
                                    onSwipeUp()
                                }
                            }
                        }
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

            Spacer(modifier = Modifier.weight(1f))

            ChatInput(
                value = messageText,
                onValueChange = { messageText = it },
                onSendClick = {
                    val textToSend = messageText.trim()
                    if (textToSend.isNotEmpty()) {
                        onSendClick(textToSend)
                        messageText = ""
                    }
                },
                modifier = Modifier.padding(bottom = 22.dp)
            )
        }
    }
}
