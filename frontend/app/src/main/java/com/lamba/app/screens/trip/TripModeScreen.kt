package com.lamba.app.screens.trip

import android.view.Surface
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaError
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import components.BackButton
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun TripModeScreen(
    startedAtMillis: Long?,
    onCollapseClick: () -> Unit,
    onFinishTripClick: () -> Unit
) {
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
        color = LambaCanvas
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LambaCanvas)
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
                color = LambaInk
            )

            Text(
                text = timerText,
                style = MaterialTheme.typography.headlineLarge,
                color = LambaInk
            )


            Button(
                onClick = onFinishTripClick,
                shape = RoundedCornerShape(LambaRadius.Medium),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LambaError,
                    contentColor = Color.White
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
