package com.lamba.app.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lamba.app.data.trips.TripResponse
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import components.BackButton
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TripHistoryScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    trips: List<TripResponse> = emptyList(),
    onBackClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(
                    PaddingValues(
                        start = LambaSpacing.ScreenHorizontal,
                        top = LambaSpacing.ScreenTop,
                        end = LambaSpacing.ScreenHorizontal,
                        bottom = LambaSpacing.ScreenBottom
                    )
                )
        ) {
            BackButton(onClick = onBackClick)

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "История поездок",
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Дата, расстояние, время в пути и средняя скорость",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when {
                    isLoading -> {
                        item {
                            TripHistoryStatusCard("Загрузка поездок...")
                        }
                    }

                    !errorMessage.isNullOrBlank() -> {
                        item {
                            TripHistoryStatusCard(
                                text = errorMessage,
                                color = colorScheme.error
                            )
                        }
                    }

                    trips.isEmpty() -> {
                        item {
                            TripHistoryStatusCard("Поездок пока нет.")
                        }
                    }

                    else -> {
                        items(trips, key = { it.id }) { trip ->
                            TripHistoryCard(trip = trip)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TripHistoryCard(
    trip: TripResponse
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = trip.startedAt.formatTripDate(),
                        style = MaterialTheme.typography.titleMedium,
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = trip.status.toTripStatusLabel(),
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                TripStatusDot(isFinished = trip.endedAt != null)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TripMetric(
                    label = "Расстояние",
                    value = "${trip.distanceM.toDoubleOrNull().orZero().metersToKm()} км",
                    modifier = Modifier.weight(1f)
                )
                TripMetric(
                    label = "Время",
                    value = trip.durationSeconds.formatDuration(),
                    modifier = Modifier.weight(1f)
                )
            }

            TripMetric(
                label = "Средняя скорость",
                value = "${trip.averageSpeedKmh.toDoubleOrNull().orZero().formatSpeed()} км/ч",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TripMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .background(
                colorScheme.primaryContainer.copy(alpha = 0.42f),
                RoundedCornerShape(LambaRadius.Medium)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TripStatusDot(
    isFinished: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme
    val color = if (isFinished) colorScheme.primary else colorScheme.error
    Surface(
        modifier = Modifier.size(34.dp),
        color = color.copy(alpha = 0.12f),
        shape = CircleShape
    ) {
        Surface(
            modifier = Modifier
                .padding(11.dp)
                .size(12.dp),
            color = color,
            shape = CircleShape
        ) {}
    }
}

@Composable
private fun TripHistoryStatusCard(
    text: String,
    color: Color = Color.Unspecified
) {
    val colorScheme = MaterialTheme.colorScheme
    val resolvedColor = if (color == Color.Unspecified) {
        colorScheme.onSurfaceVariant
    } else {
        color
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = resolvedColor,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)
        )
    }
}

private fun String.formatTripDate(): String {
    return runCatching {
        Instant.parse(this).atZone(ZoneId.systemDefault()).format(TripDateFormatter)
    }.getOrElse {
        runCatching {
            OffsetDateTime.parse(this).atZoneSameInstant(ZoneId.systemDefault()).format(TripDateFormatter)
        }.getOrDefault(this)
    }
}

private fun String.toTripStatusLabel(): String {
    return when (this.lowercase()) {
        "active" -> "Активная поездка"
        "finished" -> "Завершена"
        else -> this
    }
}

private fun Int.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

private fun Double?.orZero(): Double = this ?: 0.0

private fun Double.metersToKm(): String {
    return "%.3f".format(this / 1000.0)
}

private fun Double.formatSpeed(): String {
    return "%.1f".format(this)
}

private val TripDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
