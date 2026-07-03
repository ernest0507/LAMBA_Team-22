package com.lamba.app.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamba.app.data.records.TimelineItemResponse
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme

private val HistoryScreenBackground = Color(0xFFEEF4F2)
private val HistoryCardBackground = Color(0xFFFBFCFB)
private val HistoryPrimaryText = Color(0xFF182124)
private val HistorySecondaryText = Color(0xFF6D7C80)
private val HistoryAccent = Color(0xFF17A1B8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    records: List<TimelineItemResponse> = emptyList(),
    onBackClick: () -> Unit = {}
) {
    val sections = rememberHistorySections(records)
    var expandedItemId by remember { mutableStateOf<Int?>(null) }

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
            if (isLoading) {
                item {
                    HistoryStatusCard(text = "Loading history...")
                }
            }

            if (!errorMessage.isNullOrBlank()) {
                item {
                    HistoryStatusCard(text = errorMessage, color = Color(0xFFB3261E))
                }
            }

            if (!isLoading && errorMessage.isNullOrBlank() && sections.isEmpty()) {
                item {
                    HistoryStatusCard(text = "No expense records yet.")
                }
            }

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
                    HistoryEventCard(
                        item = item,
                        isExpanded = expandedItemId == item.id,
                        onToggle = {
                            expandedItemId = if (expandedItemId == item.id) null else item.id
                        }
                    )
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
    item: HistoryItem,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = HistoryCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(top = 12.dp, start = 64.dp)
                ) {
                    HistoryDetailRow(label = "Тип", value = item.category.toRecordTypeName())
                    HistoryDetailRow(label = "Категория", value = item.title)
                    item.mileageKm?.let {
                        HistoryDetailRow(label = "Пробег", value = "$it км")
                    }
                    item.occurredAt?.let {
                        HistoryDetailRow(label = "Дата", value = it)
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = HistorySecondaryText
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = HistoryPrimaryText
        )
    }
}

@Composable
private fun HistoryStatusCard(
    text: String,
    color: Color = HistorySecondaryText
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = HistoryCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)
        )
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
    val id: Int,
    val title: String,
    val subtitle: String,
    val amount: String,
    val category: String? = null,
    val mileageKm: Int? = null,
    val occurredAt: String? = null
)

private fun rememberHistorySections(records: List<TimelineItemResponse>): List<HistorySection> {
    return records
        .groupBy { it.occurredAt ?: "No date" }
        .map { (date, items) ->
            HistorySection(
                title = date,
                items = items.map { record ->
                    HistoryItem(
                        id = record.id,
                        title = record.title ?: "Expense",
                        subtitle = record.subtitleText(),
                        amount = record.costAmount.formatAmount(),
                        category = record.category,
                        mileageKm = record.mileageKm,
                        occurredAt = record.occurredAt
                    )
                }
            )
        }
}

private fun TimelineItemResponse.subtitleText(): String {
    return listOfNotNull(
        category.toRecordTypeName().takeIf { it != "—" },
        mileageKm?.let { "$it км" }
    ).joinToString(" | ")
}

private fun String.formatAmount(): String {
    return "${substringBefore('.')} RUB"
}

private fun String?.toRecordTypeName(): String {
    return when (this) {
        "expense" -> "Трата"
        "maintenance" -> "Обслуживание"
        "repair" -> "Поломка"
        else -> this ?: "—"
    }
}

private fun rememberHistorySections(): List<HistorySection> {
    return listOf(
        HistorySection(
            title = "СЕГОДНЯ",
            items = listOf(
                HistoryItem(
                    id = 1,
                    title = "Замена масла",
                    subtitle = "Сервис · 12:40",
                    amount = "7 000 ₽",
                    category = "maintenance",
                    mileageKm = 45200,
                    occurredAt = "12.06.2026"
                ),
                HistoryItem(
                    id = 2,
                    title = "Заправка",
                    subtitle = "52 л · распознано из чека",
                    amount = "3 500 ₽",
                    category = "expense",
                    mileageKm = 45150,
                    occurredAt = "12.06.2026"
                )
            )
        ),
        HistorySection(
            title = "2 НЕДЕЛИ НАЗАД",
            items = listOf(
                HistoryItem(
                    id = 3,
                    title = "Диагностика",
                    subtitle = "Фильтры, диагностика",
                    amount = "12 000 ₽",
                    category = "maintenance",
                    mileageKm = 44000,
                    occurredAt = "28.05.2026"
                )
            )
        ),
        HistorySection(
            title = "МАРТ",
            items = listOf(
                HistoryItem(
                    id = 4,
                    title = "Заправка",
                    subtitle = "46 л · город",
                    amount = "4 200 ₽",
                    category = "expense",
                    mileageKm = 43800,
                    occurredAt = "15.03.2026"
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
