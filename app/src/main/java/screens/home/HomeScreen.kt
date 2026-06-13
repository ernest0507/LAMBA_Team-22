package com.lamba.app.screens.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun HomeScreen(
    onCarClick: () -> Unit = {},
    onAiClick: () -> Unit = {},
    onQuestionSend: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(start = 20.dp, end = 20.dp, top = 44.dp)
    ) {
        Text(
            text = "Привет, Никита!",
            color = TextDark,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        CarCard(onClick = onCarClick)

        Spacer(modifier = Modifier.height(20.dp))

        AssistantCard(
            onAiClick = onAiClick,
            onQuestionSend = onQuestionSend
        )

        Spacer(modifier = Modifier.height(20.dp))

        StatsGrid()

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationMock(
            onHomeClick = {},
            onAiClick = onAiClick
        )
    }
}

@Composable
private fun CarCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = DarkBlue)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "МОЙ АВТОМОБИЛЬ",
                    color = Color.White.copy(alpha = 0.55f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Toyota Corolla",
                    color = Color.White,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "2020 · 1,6 л · Бензин",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Brown),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "›",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Light
                )
            }

            CarImage(
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun AssistantCard(
    onAiClick: () -> Unit,
    onQuestionSend: (String) -> Unit
) {
    var question by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onAiClick() },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = DarkBlue)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Brown),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▣",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Спросите AI-ассистента",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "О расходах, обслуживании или состоянии авто",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Background)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⌕",
                    color = DarkBlue,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(12.dp))

                BasicTextField(
                    value = question,
                    onValueChange = { question = it },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (question.isEmpty()) {
                            Text(
                                text = "Напишите вопрос...",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                )

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Brown)
                        .clickable {
                            if (question.isNotBlank()) {
                                onQuestionSend(question)
                            } else {
                                onAiClick()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "➤",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsGrid() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                title = "Пробег",
                value = "24,560",
                subtitle = "км",
                iconType = "↗",
                backgroundColor = Beige,
                contentColor = TextDark,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Все расходы",
                value = "70 000 ₽",
                subtitle = "за этот год",
                iconType = "$",
                backgroundColor = DarkBlue,
                contentColor = Color.White,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                title = "Следующее ТО",
                value = "3 240",
                subtitle = "км",
                iconType = "⚒",
                backgroundColor = Brown,
                contentColor = Color.White,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Внимание",
                value = "Проверьте",
                subtitle = "тормозные колодки",
                iconType = "!",
                backgroundColor = Color(0xFFF5DAD5),
                contentColor = Color(0xFF8A3930),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    iconType: String,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = iconType,
                color = contentColor.copy(alpha = 0.8f),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                color = contentColor.copy(alpha = 0.75f),
                fontSize = 13.sp
            )

            Text(
                text = value,
                color = contentColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                color = contentColor.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun BottomNavigationMock(
    onHomeClick: () -> Unit,
    onAiClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavButton(
            icon = "⌂",
            label = "Главная",
            selected = true,
            onClick = onHomeClick
        )

        BottomNavButton(
            icon = "▣",
            label = "AI-ассистент",
            selected = false,
            onClick = onAiClick
        )

        BottomNavButton(
            icon = "☺",
            label = "Профиль",
            selected = false,
            onClick = {}
        )
    }
}

@Composable
private fun BottomNavButton(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(if (selected) DarkBlue else Beige),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                color = if (selected) Color.White else TextDark,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            color = if (selected) DarkBlue else TextDark.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}