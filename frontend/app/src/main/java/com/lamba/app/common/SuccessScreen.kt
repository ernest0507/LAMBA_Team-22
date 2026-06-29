package com.lamba.app.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaSpacing
import components.ContinueButton

@Composable
fun SuccessScreen(
    title: String,
    message: String,
    buttonText: String,
    onContinue: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = LambaCanvas
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LambaSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = LambaInk,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message)

            Spacer(modifier = Modifier.height(24.dp))
            ContinueButton(
                text = buttonText,
                onClick = onContinue
            )
        }
    }
}



