package com.lamba.app.screens.garage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamba.app.components.CarImage

private val Background = Color(0xFFF5EFE6)
private val DarkBlue = Color(0xFF233B78)
private val Beige = Color(0xFFEFE7D8)
private val Brown = Color(0xFFA78B78)
private val TextDark = Color(0xFF2A2522)

@Composable
fun CarDetailsScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(start = 20.dp, end = 20.dp, top = 44.dp)
    ) {
        Header(
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        CarImageCard()

        Spacer(modifier = Modifier.height(24.dp))

        CarInfoRow(
            icon = "↗",
            title = "Пробег",
            value = "24 560 км"
        )

        CarInfoRow(
            icon = "⛽",
            title = "Тип топлива",
            value = "Бензин"
        )

        CarInfoRow(
            icon = "$",
            title = "Все расходы",
            value = "70 000 ₽"
        )

        CarInfoRow(
            icon = "⚒",
            title = "Следующее ТО",
            value = "через 3 240 км"
        )

        CarInfoRow(
            icon = "#",
            title = "VIN",
            value = "JTDBR32E720009876"
        )
    }
}

@Composable
private fun Header(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        CircleButton(
            text = "‹",
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = onBackClick
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Toyota Corolla",
                color = TextDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "2020 · 1,6 л",
                color = TextDark.copy(alpha = 0.6f),
                fontSize = 13.sp
            )
        }

        CircleButton(
            text = "✎",
            modifier = Modifier.align(Alignment.CenterEnd),
            backgroundColor = Brown,
            textColor = Color.White
        )
    }
}

@Composable
private fun CircleButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Beige,
    textColor: Color = DarkBlue,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(46.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CarImageCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkBlue
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CarImage()
        }
    }
}

@Composable
private fun CarInfoRow(
    icon: String,
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Beige
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(DarkBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(14.dp))

                Text(
                    text = title,
                    color = TextDark.copy(alpha = 0.55f),
                    fontSize = 15.sp
                )
            }

            Text(
                text = value,
                color = TextDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}