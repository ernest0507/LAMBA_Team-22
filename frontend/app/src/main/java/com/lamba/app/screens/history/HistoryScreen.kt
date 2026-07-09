package com.lamba.app.screens.history

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lamba.app.data.records.RecordPhotoImage
import com.lamba.app.data.records.RecordPhotosUiState
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
    recordPhotos: Map<Int, RecordPhotosUiState> = emptyMap(),
    onRecordExpanded: (Int) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val sections = rememberHistorySections(records)
    var expandedItemId by remember { mutableStateOf<Int?>(null) }
    var selectedPhoto by remember { mutableStateOf<RecordPhotoImage?>(null) }

    Scaffold(
        containerColor = HistoryScreenBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
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
                    HistoryStatusCard(text = "Загрузка истории...")
                }
            }

            if (!errorMessage.isNullOrBlank()) {
                item {
                    HistoryStatusCard(text = errorMessage, color = Color(0xFFB3261E))
                }
            }

            if (!isLoading && errorMessage.isNullOrBlank() && sections.isEmpty()) {
                item {
                    HistoryStatusCard(text = "Записей пока нет.")
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

                items(section.items, key = { it.id }) { item ->
                    HistoryEventCard(
                        item = item,
                        isExpanded = expandedItemId == item.id,
                        photoState = recordPhotos[item.id],
                        onPhotoClick = { selectedPhoto = it },
                        onToggle = {
                            val shouldExpand = expandedItemId != item.id
                            expandedItemId = if (shouldExpand) item.id else null
                            if (shouldExpand) {
                                onRecordExpanded(item.id)
                            }
                        }
                    )
                }
            }
        }
    }

    selectedPhoto?.let { photo ->
        PhotoPreviewDialog(
            photo = photo,
            onDismiss = { selectedPhoto = null }
        )
    }
}

@Composable
private fun HistoryEventCard(
    item: HistoryItem,
    isExpanded: Boolean,
    photoState: RecordPhotosUiState?,
    onPhotoClick: (RecordPhotoImage) -> Unit,
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
                    item.receiptTime?.let {
                        HistoryDetailRow(label = "Время", value = it)
                    }
                    item.fuelType?.let {
                        HistoryDetailRow(label = "Топливо", value = it)
                    }
                    item.vendor?.let {
                        HistoryDetailRow(label = "Продавец", value = it)
                    }

                    HistoryPhotosSection(
                        photoState = photoState,
                        onPhotoClick = onPhotoClick,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryPhotosSection(
    photoState: RecordPhotosUiState?,
    onPhotoClick: (RecordPhotoImage) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Фото",
            style = MaterialTheme.typography.bodySmall,
            color = HistorySecondaryText,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            photoState == null || photoState.isLoading -> {
                Text(
                    text = "Загрузка фото...",
                    style = MaterialTheme.typography.bodySmall,
                    color = HistorySecondaryText
                )
            }

            !photoState.errorMessage.isNullOrBlank() -> {
                Text(
                    text = photoState.errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB3261E)
                )
            }

            photoState.photos.isEmpty() -> {
                Text(
                    text = "Фото не добавлены",
                    style = MaterialTheme.typography.bodySmall,
                    color = HistorySecondaryText
                )
            }

            else -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(end = 18.dp)
                ) {
                    items(photoState.photos, key = { it.id }) { photo ->
                        HistoryPhotoThumbnail(
                            photo = photo,
                            onClick = { onPhotoClick(photo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryPhotoThumbnail(
    photo: RecordPhotoImage,
    onClick: () -> Unit
) {
    val image = remember(photo.id, photo.bytes) {
        BitmapFactory.decodeByteArray(photo.bytes, 0, photo.bytes.size)?.asImageBitmap()
    }

    Box(
        modifier = Modifier
            .size(86.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(HistoryAccent.copy(alpha = 0.08f))
            .border(1.dp, HistoryAccent.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = photo.filename,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = "Фото",
                style = MaterialTheme.typography.bodySmall,
                color = HistorySecondaryText
            )
        }
    }
}

@Composable
private fun PhotoPreviewDialog(
    photo: RecordPhotoImage,
    onDismiss: () -> Unit
) {
    val image = remember(photo.id, photo.bytes) {
        BitmapFactory.decodeByteArray(photo.bytes, 0, photo.bytes.size)?.asImageBitmap()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .clickable(onClick = onDismiss)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (image != null) {
                Image(
                    bitmap = image,
                    contentDescription = photo.filename,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = "x",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp)
            )
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
        title.contains("Заправка", ignoreCase = true) -> "АЗС"
        title.contains("масл", ignoreCase = true) -> "ТО"
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
    val occurredAt: String? = null,
    val receiptTime: String? = null,
    val fuelType: String? = null,
    val vendor: String? = null
)

private fun rememberHistorySections(records: List<TimelineItemResponse>): List<HistorySection> {
    return records
        .groupBy { it.occurredAt ?: "Без даты" }
        .map { (date, items) ->
            HistorySection(
                title = date,
                items = items.map { record ->
                    HistoryItem(
                        id = record.id,
                        title = record.title ?: "Событие",
                        subtitle = record.subtitleText(),
                        amount = record.costAmount.formatAmount(),
                        category = record.category,
                        mileageKm = record.mileageKm,
                        occurredAt = record.occurredAt,
                        receiptTime = record.description.extractReceiptField("Receipt time"),
                        fuelType = record.description.extractReceiptField("Fuel type"),
                        vendor = record.vendor
                    )
                }
            )
        }
}

private fun TimelineItemResponse.subtitleText(): String {
    return listOfNotNull(
        category.toRecordTypeName().takeIf { it != "-" },
        description.extractReceiptField("Receipt time"),
        description.extractReceiptField("Fuel type"),
        mileageKm?.let { "$it км" }
    ).joinToString(" | ")
}

private fun String?.extractReceiptField(label: String): String? {
    if (isNullOrBlank()) return null
    val prefix = "$label:"
    return lineSequence()
        .firstOrNull { it.startsWith(prefix) }
        ?.removePrefix(prefix)
        ?.trim()
        ?.takeIf { it.isNotBlank() }
}

private fun String.formatAmount(): String {
    return "${substringBefore('.')} ₽"
}

private fun String?.toRecordTypeName(): String {
    return when (this) {
        "expense" -> "Трата"
        "maintenance" -> "Обслуживание"
        "repair" -> "Поломка"
        else -> this ?: "-"
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    LAMBA_MVPv0Theme {
        Surface {
            HistoryScreen(
                records = listOf(
                    TimelineItemResponse(
                        id = 1,
                        category = "maintenance",
                        title = "Замена масла",
                        occurredAt = "2026-07-03",
                        mileageKm = 45200,
                        costAmount = "7000.00"
                    )
                )
            )
        }
    }
}
