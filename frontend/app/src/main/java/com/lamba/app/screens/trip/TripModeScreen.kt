package com.lamba.app.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import components.BackButton
import kotlinx.coroutines.delay

@Composable
fun TripModeScreen(
    startedAtMillis: Long?,
    onCollapseClick: () -> Unit,
    onFinishTripClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(startedAtMillis) {
        while (startedAtMillis != null) {
            delay(1000)
            nowMillis = System.currentTimeMillis()
        }
    }

    val elapsedMillis = if (startedAtMillis != null) {
        nowMillis - startedAtMillis
    } else { 0L }

    val timerText = elapsedMillis.formatTripDuration()

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
                ),
            verticalArrangement = Arrangement.spacedBy(LambaSpacing.CardPadding)
        ) {
            BackButton(onClick = onCollapseClick)

            Text(
                text = "Режим поездки",
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.onBackground
            )

            Text(
                text = timerText,
                style = MaterialTheme.typography.headlineLarge,
                color = colorScheme.onBackground
            )


            Button(
                onClick = onFinishTripClick,
                shape = RoundedCornerShape(LambaRadius.Medium),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.error,
                    contentColor = colorScheme.onError
                )
            ) {
                Text(
                    text = "Завершить поездку",
                    style = MaterialTheme.typography.bodyLarge
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
