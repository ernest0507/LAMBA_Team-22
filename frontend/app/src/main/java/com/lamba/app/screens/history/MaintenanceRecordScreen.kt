package com.lamba.app.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamba.app.common.LoadingOverlay
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaOutlineSoft
import com.lamba.app.ui.theme.LambaError
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import components.BackButton
import components.ContinueButton
import components.LambaTextField
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class MaintenanceRecordFormData(
    val category: String,
    val title: String,
    val description: String,
    val serviceDate: String,
    val mileage: String,
    val cost: String,
    val organization: String,
    val imageUris: List<String> = emptyList()
)

private val MaintenanceCategories = listOf(
    "Замена масла",
    "Замена фильтров",
    "Замена тормозных колодок",
    "Замена свечей",
    "Диагностика",
    "Другое"
)

private val RecordDateFormatter = DateTimeFormatter.ofPattern(
    "dd.MM.yyyy",
    Locale.forLanguageTag("ru-RU")
)

@Composable
fun MaintenanceRecordScreen(
    onBack: () -> Unit,
    onSave: (MaintenanceRecordFormData) -> Unit,
    isSaving: Boolean = false,
    errorMessage: String? = null
) {
    var category by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var serviceDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    var mileage by rememberSaveable { mutableStateOf("") }
    var cost by rememberSaveable { mutableStateOf("") }
    var organization by rememberSaveable { mutableStateOf("") }
    var imageUris by rememberSaveable { mutableStateOf(listOf<String>()) }

    RecordFormScreen(
        subtitle = "Обслуживание",
        onBack = onBack,
        isSaving = isSaving,
        errorMessage = errorMessage,
        onSave = {
            onSave(
                MaintenanceRecordFormData(
                    category = category,
                    title = title,
                    description = description,
                    serviceDate = serviceDateMillis?.let(::formatRecordDate).orEmpty(),
                    mileage = mileage,
                    cost = cost,
                    organization = organization,
                    imageUris = imageUris
                )
            )
        },
        saveEnabled = category.isNotBlank() && title.isNotBlank()
    ) {
        RecordDropdownField(
            label = "Категория обслуживания",
            value = category,
            placeholder = "Выберите категорию",
            options = MaintenanceCategories,
            onValueChange = { category = it }
        )

        LambaTextField(
            label = "Название",
            value = title,
            onValueChange = { title = it },
            placeholder = "Например, Замена масла"
        )

        LambaTextField(
            label = "Описание",
            value = description,
            onValueChange = { description = it },
            placeholder = "Введите описание",
            singleLine = false,
            minHeight = RecordMultilineFieldMinHeight
        )

        RecordDateField(
            label = "Дата обслуживания",
            value = serviceDateMillis?.let(::formatRecordDate).orEmpty(),
            placeholder = "Выберите дату",
            selectedDateMillis = serviceDateMillis,
            onDateSelected = { serviceDateMillis = it }
        )

        LambaTextField(
            label = "Пробег",
            value = mileage,
            onValueChange = { mileage = it.filter(Char::isDigit) },
            placeholder = "0 км",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        LambaTextField(
            label = "Стоимость",
            value = cost,
            onValueChange = { cost = it.filter(Char::isDigit) },
            placeholder = "0 ₽",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        LambaTextField(
            label = "Организация",
            value = organization,
            onValueChange = { organization = it },
            placeholder = "Название сервиса"
        )

        RecordImageField(
            imageUris = imageUris,
            onImageUrisChanged = { imageUris = it }
        )
    }
}

@Composable
internal fun RecordFormScreen(
    subtitle: String,
    onBack: () -> Unit,
    onSave: () -> Unit,
    saveEnabled: Boolean,
    isSaving: Boolean = false,
    errorMessage: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = LambaCanvas,
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LambaCanvas)
                        .navigationBarsPadding()
                        .padding(
                            start = LambaSpacing.ScreenHorizontal,
                            end = LambaSpacing.ScreenHorizontal,
                            top = LambaSpacing.Step * 2,
                            bottom = LambaSpacing.ScreenBottom
                        )
                ) {
                    ContinueButton(
                        onClick = onSave,
                        text = "Сохранить",
                        enabled = saveEnabled && !isSaving
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LambaCanvas)
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(
                        start = LambaSpacing.ScreenHorizontal,
                        end = LambaSpacing.ScreenHorizontal,
                        top = LambaSpacing.ScreenTop,
                        bottom = LambaSpacing.ScreenBottom
                    ),
                verticalArrangement = Arrangement.spacedBy(LambaSpacing.CardPadding)
            ) {
                BackButton(onClick = onBack)

                Spacer(modifier = Modifier.height(LambaSpacing.Step * 2))

                Text(
                    text = "Новая запись",
                    style = MaterialTheme.typography.headlineMedium,
                    color = LambaInk
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LambaInkMuted
                )

                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = LambaSurface,
                    shape = RoundedCornerShape(LambaRadius.Large)
                ) {
                    Column(
                        modifier = Modifier.padding(LambaSpacing.CardPadding),
                        verticalArrangement = Arrangement.spacedBy(LambaSpacing.CardPadding),
                        content = content
                    )
                }
            }
        }
        if (isSaving) {
            LoadingOverlay(
                title = "Добавление записи",
                message = "Пожалуйста, подождите. Ваши данные обрабатываются"
            )
    }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordDropdownField(
    label: String,
    value: String,
    placeholder: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val fieldShape = RoundedCornerShape(LambaRadius.Medium)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = LambaInkMuted
        )

        Spacer(modifier = Modifier.height(LambaSpacing.Step))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth()
                    .heightIn(min = RecordFieldMinHeight)
                    .clip(fieldShape)
                    .background(LambaSurface, fieldShape)
                    .border(
                        width = 1.dp,
                        color = LambaOutlineSoft,
                        shape = fieldShape
                    ),
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LambaInkMuted
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = LambaInk),
                shape = fieldShape,
                colors = recordFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = LambaSurface,
                shape = RoundedCornerShape(LambaRadius.Medium)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                color = LambaInk
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordDateField(
    label: String,
    value: String,
    placeholder: String,
    selectedDateMillis: Long?,
    onDateSelected: (Long?) -> Unit
) {
    var isDialogVisible by remember { mutableStateOf(false) }

    if (isDialogVisible) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis
        )

        DatePickerDialog(
            onDismissRequest = { isDialogVisible = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        isDialogVisible = false
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text(text = "ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDialogVisible = false }) {
                    Text(text = "Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    RecordSelectableField(
        label = label,
        value = value,
        placeholder = placeholder,
        onClick = { isDialogVisible = true },
        trailingContent = {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = null,
                tint = LambaInkMuted
            )
        }
    )
}

@Composable
internal fun RecordSelectableField(
    label: String,
    value: String,
    placeholder: String,
    onClick: () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val fieldShape = RoundedCornerShape(LambaRadius.Medium)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = LambaInkMuted
        )

        Spacer(modifier = Modifier.height(LambaSpacing.Step))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = RecordFieldMinHeight)
                .clip(fieldShape)
                .background(LambaSurface, fieldShape)
                .border(
                    width = 1.dp,
                    color = LambaOutlineSoft,
                    shape = fieldShape
                )
                .clickable(onClick = onClick)
                .padding(horizontal = LambaSpacing.CardPadding, vertical = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value.ifBlank { placeholder },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (value.isBlank()) LambaInkMuted else LambaInk,
                    modifier = Modifier.weight(1f)
                )

                trailingContent?.invoke()
            }
        }
    }
}

@Composable
internal fun recordFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = LambaInk,
    unfocusedTextColor = LambaInk,
    focusedContainerColor = LambaSurface,
    unfocusedContainerColor = LambaSurface,
    disabledContainerColor = LambaSurface,
    errorContainerColor = LambaSurface,
    focusedLeadingIconColor = LambaInkMuted,
    unfocusedLeadingIconColor = LambaInkMuted,
    focusedTrailingIconColor = LambaInkMuted,
    unfocusedTrailingIconColor = LambaInkMuted,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent
)

internal val RecordFieldMinHeight = LambaSpacing.Step * 7
internal val RecordMultilineFieldMinHeight = LambaSpacing.Step * 15

internal fun formatRecordDate(selectedDateMillis: Long): String {
    return Instant
        .ofEpochMilli(selectedDateMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(RecordDateFormatter)
}

@Composable
internal fun RecordImageField(
    imageUris: List<String>,
    onImageUrisChanged: (List<String>) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val newUris = uris.map { it.toString() }
        onImageUrisChanged((imageUris + newUris).take(3))
    }

    @Composable
    fun decodeBitmap(uriStr: String) = remember(uriStr) {
        try {
            val uri = android.net.Uri.parse(uriStr)
            context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input)?.asImageBitmap()
            }
        } catch (_: Exception) {
            null
        }
    }

    val fieldShape = RoundedCornerShape(LambaRadius.Medium)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Фото",
                style = MaterialTheme.typography.labelSmall,
                color = LambaInkMuted
            )
            Text(
                text = "${imageUris.size}/3",
                style = MaterialTheme.typography.labelSmall,
                color = LambaInkMuted
            )
        }

        Spacer(modifier = Modifier.height(LambaSpacing.Step))

        if (imageUris.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                imageUris.forEachIndexed { index, uriStr ->
                    val bitmap = decodeBitmap(uriStr)
                    if (bitmap != null) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(fieldShape)
                                .background(LambaSurface, fieldShape)
                                .border(1.dp, LambaOutlineSoft, fieldShape)
                        ) {
                            Image(
                                bitmap = bitmap,
                                contentDescription = "Фото ${index + 1}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(fieldShape),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .clickable {
                                        onImageUrisChanged(imageUris.toMutableList().apply { removeAt(index) })
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Удалить",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(LambaSpacing.Step))
        }

        if (imageUris.size < 3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = RecordFieldMinHeight)
                    .clip(fieldShape)
                    .background(LambaSurface, fieldShape)
                    .border(1.dp, LambaOutlineSoft, fieldShape)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = LambaInkMuted
                    )
                    Text(
                        text = "Добавить фото",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LambaInkMuted
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun MaintenanceRecordScreenPreview() {
    LAMBA_MVPv0Theme {
        MaintenanceRecordScreen(
            onBack = {},
            onSave = {}
        )
    }
}
