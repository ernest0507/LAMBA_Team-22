package com.lamba.app.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaAccent
import com.lamba.app.ui.theme.LambaChat


@Composable
fun AiBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = LambaChat,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .clip(RoundedCornerShape(18.dp))
                .background(LambaAccent)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}







