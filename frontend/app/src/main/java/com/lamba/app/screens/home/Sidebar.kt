package com.lamba.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface

@Composable
fun Sidebar(
    onClose: () -> Unit,
    onAddExpensesClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onDocumentsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.32f))
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .fillMaxWidth(0.74f)
                .background(LambaSurface)
                .padding(horizontal = 24.dp, vertical = 52.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "МОЙ АВТОМОБИЛЬ",
                        style = MaterialTheme.typography.labelSmall,
                        color = LambaInkMuted
                    )

                    Text(
                        text = "Toyota Corolla",
                        style = MaterialTheme.typography.titleLarge,
                        color = LambaInk
                    )

                    Text(
                        text = "48 230 км · 2021",
                        style = MaterialTheme.typography.bodySmall,
                        color = LambaInkMuted
                    )
                }

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = LambaInk
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Гараж",
                style = MaterialTheme.typography.titleLarge,
                color = LambaInk
            )

            Spacer(modifier = Modifier.height(24.dp))

            GarageMenuItem(
                title = "Добавить расход",
                subtitle = "Запишите новую трату на автомобиль",
                onClick = {
                    onClose()
                    onAddExpensesClick()
                }
            )

            GarageMenuItem(
                title = "История",
                subtitle = "События, обслуживание, расходы",
                onClick = onHistoryClick
            )

            GarageMenuItem(
                title = "Статистика",
                subtitle = "Расходы, пробег, категории",
                onClick = onStatisticsClick
            )

            GarageMenuItem(
                title = "Документы",
                subtitle = "СТС, страховка, чеки",
                onClick = onDocumentsClick
            )

            GarageMenuItem(
                title = "Профиль",
                subtitle = "Автомобиль, уведомления, настройки",
                onClick = onProfileClick
            )
        }
    }
}
