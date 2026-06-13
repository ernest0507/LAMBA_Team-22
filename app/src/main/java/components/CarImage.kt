package com.lamba.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkBlue = Color(0xFF233B78)
private val Brown = Color(0xFFA78B78)

@Composable
fun CarImage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(190.dp)
            .height(78.dp)
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(170.dp)
                .height(38.dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 80.dp))
                .background(Brown)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .width(74.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 80.dp))
                .background(Color(0xFFE7E2DA))
        )

        Wheel(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 35.dp)
        )

        Wheel(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 26.dp)

        )
    }
}

@Composable
private fun Wheel(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(DarkBlue)
        )
    }
}

