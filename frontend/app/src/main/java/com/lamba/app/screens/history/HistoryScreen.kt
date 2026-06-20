package com.lamba.app.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme

private val HistoryScreenBackground = Color(0xFFEEF4F2)
private val HistoryCardBackground = Color(0xFFFBFCFB)
private val HistoryPrimaryText = Color(0xFF182124)
private val HistorySecondaryText = Color(0xFF6D7C80)
private val HistoryAccent = Color(0xFF17A1B8)

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit = {}
) {
    val sections = rememberHistorySections()

    Scaffold(
        containerColor = HistoryScreenBackground,
        topBar = {
            TopAppBar(
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = HistoryScreenBackground,
                    titleContentColor = HistoryPrimaryText,
                    navigationIconContentColor = HistoryPrimaryText
                ),
                title = {
                    Text(
                        text = "История",
                        style = MaterialTheme.typography.titleLarge,
                        color = HistoryPrimaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = "\u2039",
                            style = MaterialTheme.typography.headlineMedium,
                            color = HistoryPrimaryText
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            sections.forEach { section ->
                item {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.labelLarge,
                        color = HistorySecondaryText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                    )
                }

                items(section.items) { item ->
                    HistoryEventCard(item = item)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                InsightCard(
                    text = "LAMBA AI: расходы на обслуживание выше обычного из-за планового ТО. Следующая крупная трата ожидается только через 1 800 км."
                )
            }
        }
    }
}

@Composable
private fun HistoryEventCard(
    item: HistoryItem
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = HistoryCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HistoryBadge(title = item.title)

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = HistoryPrimaryText,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = HistorySecondaryText
                )
            }

            Text(
                text = item.amount,
                style = MaterialTheme.typography.titleMedium,
                color = HistoryPrimaryText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HistoryBadge(
    title: String
) {
    val badgeText = when {
        title.contains("Заправка") -> "АЗС"
        title.contains("масла") -> "ТО"
        else -> "СВ"
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(HistoryAccent.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = badgeText,
            style = MaterialTheme.typography.labelLarge,
            color = HistoryAccent,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun InsightCard(
    text: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = HistoryCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(HistoryAccent)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = HistoryPrimaryText
            )
        }
    }
}

private data class HistorySection(
    val title: String,
    val items: List<HistoryItem>
)

private data class HistoryItem(
    val title: String,
    val subtitle: String,
    val amount: String
)

private fun rememberHistorySections(): List<HistorySection> {
    return listOf(
        HistorySection(
            title = "СЕГОДНЯ",
            items = listOf(
                HistoryItem(
                    title = "Замена масла",
                    subtitle = "Сервис · 12:40",
                    amount = "7 000 ₽"
                ),
                HistoryItem(
                    title = "Заправка",
                    subtitle = "52 л · распознано из чека",
                    amount = "3 500 ₽"
                )
            )
        ),
        HistorySection(
            title = "2 НЕДЕЛИ НАЗАД",
            items = listOf(
                HistoryItem(
                    title = "Техническое обслуживание",
                    subtitle = "Фильтры, диагностика",
                    amount = "12 000 ₽"
                )
            )
        ),
        HistorySection(
            title = "МАРТ",
            items = listOf(
                HistoryItem(
                    title = "Заправка",
                    subtitle = "46 л · город",
                    amount = "4 200 ₽"
                )
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    LAMBA_MVPv0Theme {
        Surface {
            HistoryScreen()
        }
    }
}
