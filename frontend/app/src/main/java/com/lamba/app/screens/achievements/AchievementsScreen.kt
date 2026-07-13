package com.lamba.app.screens.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lamba.app.data.achievements.AchievementResponse
import com.lamba.app.data.achievements.CategoryTitles
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import components.BackButton

private val CategoryOrder = listOf("statistics", "road", "repair")

private val ManuallyUnlockableCategories = setOf("road", "repair")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    achievements: List<AchievementResponse> = emptyList(),
    onBackClick: () -> Unit = {},
    onUnlockClick: (Int) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val sections = remember(achievements) {
        achievements
            .groupBy { it.category }
            .let { grouped ->
                CategoryOrder.map { key ->
                    CategoryTitles.getValue(key) to grouped[key].orEmpty()
                }
            }
    }

    val hasAnyItems = remember(sections) { sections.any { it.second.isNotEmpty() } }

    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.onBackground,
                    navigationIconContentColor = colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = "Достижения",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorScheme.onBackground,
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

            if (!isLoading && errorMessage.isNullOrBlank() && !hasAnyItems) {
                item {
                    StatusCard(text = "Пока нет достижений.")
                }
            }

            sections.forEach { (title, items) ->
                item {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                items.chunked(2).forEach { pair ->
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            pair.forEach { achievement ->
                                AchievementCard(
                                    achievement = achievement,
                                    canUnlock = achievement.category in ManuallyUnlockableCategories && !achievement.unlocked,
                                    onUnlockClick = { onUnlockClick(achievement.id) },
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
}

@Composable
private fun AchievementCard(
    achievement: AchievementResponse,
    canUnlock: Boolean = false,
    onUnlockClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val unlocked = achievement.unlocked
    var showUnlock by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) colorScheme.surface else colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (unlocked) 2.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (canUnlock) Modifier.clickable { showUnlock = !showUnlock }
                    else Modifier
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(LambaRadius.Medium))
                    .background(
                        if (unlocked) colorScheme.primaryContainer.copy(alpha = 0.52f)
                        else colorScheme.outlineVariant.copy(alpha = 0.7f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (unlocked) {
                    Text(
                        text = achievement.name.take(2),
                        style = MaterialTheme.typography.titleLarge,
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "?",
                        style = MaterialTheme.typography.headlineLarge,
                        color = colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = achievement.name,
                style = MaterialTheme.typography.titleSmall,
                color = if (unlocked) colorScheme.onSurface else colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            if (achievement.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

            if (canUnlock && showUnlock) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onUnlockClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(LambaRadius.Small),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Выполнить", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun StatusCard(
    text: String,
    color: Color = Color.Unspecified
) {
    val colorScheme = MaterialTheme.colorScheme
    val resolvedColor = if (color == Color.Unspecified) {
        colorScheme.onSurfaceVariant
    } else {
        color
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = resolvedColor,
            modifier = Modifier.padding(LambaSpacing.CardPadding)
        )
    }
}
