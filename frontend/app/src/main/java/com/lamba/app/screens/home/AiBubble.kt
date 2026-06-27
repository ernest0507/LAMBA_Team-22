package com.lamba.app.screens.home


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaAccent
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaChat


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AiBubble(text: String) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val bubbleMaxWidth = maxWidth * 0.72f
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .widthIn(max = bubbleMaxWidth)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }

}







