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
import com.lamba.app.data.cars.CarResponse
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing

@Composable
fun Sidebar(
    car: CarResponse? = null,
    onClose: () -> Unit,
    onAddExpensesClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onTripHistoryClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onOpenQrClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val carTitle = car?.displayName().orEmpty().ifBlank { "Автомобиль" }
    val carDetails = car?.let {
        "${it.currentMileageKm.formatMileage()} км · ${it.year}"
    } ?: "Данные авто загружаются"

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
                .background(colorScheme.surface)
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
                        color = colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = carTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = colorScheme.onSurface
                    )

                    Text(
                        text = carDetails,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Гараж",
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.onSurface
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
                title = "Поездки",
                subtitle = "Дата, расстояние, время и скорость",
                onClick = onTripHistoryClick
            )

            GarageMenuItem(
                title = "Статистика",
                subtitle = "Расходы, пробег, категории",
                onClick = onStatisticsClick
            )

            GarageMenuItem(
                title = "Сканировать чек | QR-код",
                subtitle = "Добавьте информацию о заправке через QR-код",
                onClick = onOpenQrClick
            )

            GarageMenuItem(
                title = "Достижения",
                subtitle = "Награды и достижения",
                onClick = onAchievementsClick
            )

            GarageMenuItem(
                title = "Профиль",
                subtitle = "Автомобиль и настройки приложения",
                onClick = onProfileClick
            )
        }
    }
}
