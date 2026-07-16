package com.lamba.app.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaRadius
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TripStartButton(
    isTripActive: Boolean,
    tripStartedAtMillis: Long? = null,
    distanceKm: Double = 0.0,
    modifier: Modifier = Modifier,
    onTripHoldComplete: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val holdProgress = remember { Animatable(0f) }
    val holdDurationMillis = 3000
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(isTripActive, tripStartedAtMillis) {
        while (isTripActive && tripStartedAtMillis != null) {
            nowMillis = System.currentTimeMillis()
            delay(1000)
        }
    }

    LaunchedEffect(isTripActive) {
        holdProgress.snapTo(0f)
    }

    val elapsedMillis = if (isTripActive && tripStartedAtMillis != null) {
        nowMillis - tripStartedAtMillis
    } else {
        0L
    }
    val buttonText = if (isTripActive) {
        "Поездка активна"
    } else {
        "Начать поездку"
    }
    val subtitleText = if (isTripActive) {
        "${elapsedMillis.formatTripDuration()} | ${"%.3f".format(distanceKm)} км"
    } else {
        "Удерживайте 3 сек"
    }
    val circleColor = if (isTripActive) {
        colorScheme.primaryContainer
    } else {
        colorScheme.onPrimaryContainer
    }
    val iconColor = if (isTripActive) {
        colorScheme.onPrimaryContainer
    } else {
        colorScheme.onPrimary
    }
    val icon = if (isTripActive) {
        Icons.Filled.FiberManualRecord
    } else {
        Icons.Filled.PlayArrow
    }
    val iconSize = if (isTripActive) {
        14.dp
    } else {
        24.dp
    }

    Card(
        modifier = modifier
            .height(76.dp)
            .clip(RoundedCornerShape(LambaRadius.Large))
            .pointerInput(isTripActive) {
                coroutineScope {
                    awaitEachGesture {
                        awaitFirstDown()
                        var completed = false

                        val holdJob = launch {
                            holdProgress.snapTo(0f)
                            holdProgress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = holdDurationMillis,
                                    easing = LinearEasing
                                )
                            )
                            completed = true
                            onTripHoldComplete()
                            holdProgress.snapTo(0f)
                        }

                        waitForUpOrCancellation()
                        holdJob.cancel()

                        if (!completed) {
                            launch {
                                holdProgress.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(durationMillis = 180)
                                )
                            }
                        }
                    }
                }
            },
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(holdProgress.value)
                    .background(
                        color = if (isTripActive) {
                            colorScheme.error.copy(alpha = 0.16f)
                        } else {
                            colorScheme.primaryContainer
                        }
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = circleColor,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(iconSize)
                    )
                }

                Column {
                    Text(
                        text = buttonText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Text(
                        text = subtitleText,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colorScheme.onPrimaryContainer
                )
            }
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
