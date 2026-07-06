package com.lamba.app.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaAccent
import com.lamba.app.ui.theme.LambaAccentSoft
import com.lamba.app.ui.theme.LambaAccentStrong
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface
import components.ContinueButton

@Composable
fun TripFinishedScreen(
    durationMillis: Long,
    distanceKm: Double,
    averageSpeedKmH: Double,
    fuelConsumptionL: Double,
    onDoneClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LambaCanvas
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LambaCanvas)
                .padding(
                    PaddingValues(
                        start = LambaSpacing.ScreenHorizontal,
                        end = LambaSpacing.ScreenHorizontal,
                        top = LambaSpacing.ScreenTop,
                        bottom = LambaSpacing.ScreenBottom
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Поездка завершена",
                style = MaterialTheme.typography.titleLarge,
                color = LambaInk
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Отличная поездка",
                style = MaterialTheme.typography.titleMedium,
                color = LambaAccentStrong
            )

            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = CircleShape,
                        ambientColor = LambaAccent.copy(alpha = 0.10f),
                        spotColor = LambaAccentStrong.copy(alpha = 0.20f)
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(LambaAccent, LambaAccentStrong)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TripResultRow("Расстояние", "$distanceKm км" )
                TripResultRow("Время в пути", "${durationMillis.formatTripDuration()}")
                TripResultRow("Средняя скорость", "$averageSpeedKmH км/ч")
            }

            Spacer(modifier = Modifier.weight(1f))

            ContinueButton(
                text = "Готово",
                onClick = onDoneClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
private fun TripResultRow(
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = LambaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
               modifier = Modifier
                   .size(34.dp)
                   .background(LambaAccentSoft, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(LambaAccentStrong, CircleShape)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = LambaInkMuted,
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = LambaInk,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun Long.formatTripDuration(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}



