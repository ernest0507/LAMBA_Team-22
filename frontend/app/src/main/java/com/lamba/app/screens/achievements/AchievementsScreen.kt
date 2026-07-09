package com.lamba.app.screens.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lamba.app.data.achievements.AchievementResponse
import com.lamba.app.ui.theme.LambaAccent
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaOutlineSoft
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface
import components.BackButton

private val LockedCardBg = Color(0xFFE1E8E6)
private val LockedCardText = Color(0xFF9AABAB)
private val UnlockedAccent = LambaAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    achievements: List<AchievementResponse> = emptyList(),
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = LambaCanvas,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LambaCanvas,
                    titleContentColor = LambaInk,
                    navigationIconContentColor = LambaInk
                ),
                title = {
                    Text(
                        text = "Достижения",
                        style = MaterialTheme.typography.titleLarge,
                        color = LambaInk,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    BackButton(onClick = onBackClick)
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = LambaSpacing.ScreenHorizontal,
                top = 12.dp,
                end = LambaSpacing.ScreenHorizontal,
                bottom = LambaSpacing.ScreenBottom + 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isLoading) {
                item {
                    StatusCard(text = "Загрузка достижений...")
                }
            }

            if (!errorMessage.isNullOrBlank()) {
                item {
                    StatusCard(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (!isLoading && errorMessage.isNullOrBlank() && achievements.isEmpty()) {
                item {
                    StatusCard(text = "Пока нет достижений.")
                }
            }

            if (achievements.isNotEmpty()) {
                item {
                    Text(
                        text = "Ваши достижения",
                        style = MaterialTheme.typography.labelLarge,
                        color = LambaInkMuted,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(achievements.chunked(2)) { _, pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { achievement ->
                            AchievementCard(
                                achievement = achievement,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementCard(
    achievement: AchievementResponse,
    modifier: Modifier = Modifier
) {
    val unlocked = achievement.unlocked

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) LambaSurface else LockedCardBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (unlocked) 2.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(LambaRadius.Medium))
                    .background(
                        if (unlocked) UnlockedAccent.copy(alpha = 0.12f)
                        else LockedCardText.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (unlocked) {
                    Text(
                        text = achievement.name.take(2),
                        style = MaterialTheme.typography.titleLarge,
                        color = UnlockedAccent,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "?",
                        style = MaterialTheme.typography.headlineLarge,
                        color = LockedCardText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (unlocked) achievement.name else "???",
                style = MaterialTheme.typography.titleSmall,
                color = if (unlocked) LambaInk else LockedCardText,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            if (unlocked && achievement.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = LambaInkMuted,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun StatusCard(
    text: String,
    color: Color = LambaInkMuted
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = LambaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            modifier = Modifier.padding(LambaSpacing.CardPadding)
        )
    }
}
