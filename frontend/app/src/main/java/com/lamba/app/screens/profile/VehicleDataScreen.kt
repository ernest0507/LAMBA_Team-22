package com.lamba.app.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.lamba.app.data.cars.CarResponse
import com.lamba.app.data.cars.CarUpdateRequest
import com.lamba.app.screens.greeting.CarBrands
import com.lamba.app.screens.greeting.CarModelsByBrand
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface
import com.lamba.app.ui.theme.LambaVehicleBlue
import com.lamba.app.ui.theme.LambaVehicleGreen
import com.lamba.app.ui.theme.LambaVehicleRed
import com.lamba.app.ui.theme.LambaVehicleSilver
import components.BackButton
import components.CarImage
import components.ContinueButton
import java.text.NumberFormat
import java.time.Year
import java.util.Locale
import kotlinx.coroutines.delay

@Composable
fun VehicleDataScreen(
    onBackClick: () -> Unit = {},
    vehicleData: VehicleDataUiModel = VehicleDataUiModel(),
    isSaving: Boolean = false,
    saveErrorMessage: String? = null,
    onSaveVehicleData: (CarUpdateRequest) -> Unit = {}
) {
    var savedVehicleData by remember {
        mutableStateOf(vehicleData.toEditableData())
    }
    var currentVehicleData by rememberSaveable(stateSaver = VehicleEditableDataSaver) {
        mutableStateOf(vehicleData.toEditableData())
    }
    var editingFieldKey by rememberSaveable { mutableStateOf<String?>(null) }
    var expandedDropdownKey by rememberSaveable { mutableStateOf<String?>(null) }
    var isSuccessVisible by remember { mutableStateOf(false) }

    val validationErrors = currentVehicleData.validate()
    val isSaveEnabled = currentVehicleData != savedVehicleData && !isSaving && validationErrors.isValid
    val bodyColor = currentVehicleData.colorLabel.toVehicleColor()
    val colorScheme = MaterialTheme.colorScheme
    val makeOptions = remember(currentVehicleData.make) {
        CarBrands.withCurrentOption(currentVehicleData.make)
    }
    val modelOptions = remember(currentVehicleData.make, currentVehicleData.model) {
        CarModelsByBrand[currentVehicleData.make]
            .orEmpty()
            .withCurrentOption(currentVehicleData.model)
    }

    LaunchedEffect(vehicleData) {
        val editableData = vehicleData.toEditableData()
        savedVehicleData = editableData
        currentVehicleData = editableData
        editingFieldKey = null
        expandedDropdownKey = null
    }

    LaunchedEffect(isSuccessVisible) {
        if (isSuccessVisible) {
            delay(2500)
            isSuccessVisible = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = LambaSpacing.ScreenHorizontal,
                top = LambaSpacing.ScreenTop,
                end = LambaSpacing.ScreenHorizontal,
                bottom = LambaSpacing.ScreenBottom
            ),
            verticalArrangement = Arrangement.spacedBy(LambaSpacing.CardPadding)
        ) {
            item {
                VehicleDataHeader(onBackClick = onBackClick)
            }

            item {
                VehicleHeroCard(
                    model = currentVehicleData.displayName(),
                    bodyType = currentVehicleData.bodyType,
                    mileage = currentVehicleData.mileage.displayMileage(),
                    year = currentVehicleData.year,
                    bodyColor = bodyColor,
                    colorKey = currentVehicleData.colorLabel.toBackendColor()
                )
            }

            item {
                DropdownVehicleFieldCard(
                    label = "Марка автомобиля",
                    value = currentVehicleData.make,
                    icon = Icons.Filled.DirectionsCar,
                    options = makeOptions,
                    expanded = expandedDropdownKey == VehicleFieldKeys.Make,
                    onExpandedChange = { expanded ->
                        expandedDropdownKey = if (expanded) VehicleFieldKeys.Make else null
                        editingFieldKey = null
                    },
                    onOptionSelected = { selectedMake ->
                        val nextModel = CarModelsByBrand[selectedMake]?.firstOrNull()
                            ?: currentVehicleData.model.takeIf { it.isNotBlank() }
                            ?: selectedMake
                        currentVehicleData = currentVehicleData.copy(
                            make = selectedMake,
                            model = nextModel
                        )
                        expandedDropdownKey = null
                    },
                    errorMessage = validationErrors.make
                )
            }

            item {
                DropdownVehicleFieldCard(
                    label = "Модель автомобиля",
                    value = currentVehicleData.model,
                    icon = Icons.Filled.DirectionsCar,
                    options = modelOptions,
                    expanded = expandedDropdownKey == VehicleFieldKeys.Model,
                    onExpandedChange = { expanded ->
                        expandedDropdownKey = if (expanded) VehicleFieldKeys.Model else null
                        editingFieldKey = null
                    },
                    onOptionSelected = {
                        currentVehicleData = currentVehicleData.copy(model = it)
                        expandedDropdownKey = null
                    },
                    errorMessage = validationErrors.model
                )
            }

            item {
                ManualVehicleFieldCard(
                    label = "Год выпуска",
                    value = currentVehicleData.year,
                    icon = Icons.Filled.CalendarMonth,
                    isEditing = editingFieldKey == VehicleFieldKeys.Year,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onStartEditing = {
                        editingFieldKey = VehicleFieldKeys.Year
                        expandedDropdownKey = null
                    },
                    onDoneEditing = {
                        editingFieldKey = null
                    },
                    onValueChange = {
                        currentVehicleData = currentVehicleData.copy(year = it.filterDigits(maxLength = 4))
                    },
                    errorMessage = validationErrors.year
                )
            }

            item {
                ManualVehicleFieldCard(
                    label = "Пробег",
                    value = currentVehicleData.mileage,
                    icon = Icons.Filled.Speed,
                    isEditing = editingFieldKey == VehicleFieldKeys.Mileage,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    valueSuffix = "км",
                    onStartEditing = {
                        editingFieldKey = VehicleFieldKeys.Mileage
                        expandedDropdownKey = null
                    },
                    onDoneEditing = {
                        editingFieldKey = null
                    },
                    onValueChange = {
                        currentVehicleData = currentVehicleData.copy(mileage = it.filterDigits(maxLength = 7))
                    },
                    errorMessage = validationErrors.mileage
                )
            }
            item {
                DropdownVehicleFieldCard(
                    label = "Цвет автомобиля",
                    value = currentVehicleData.colorLabel,
                    icon = Icons.Filled.Palette,
                    options = VehicleColorOptions,
                    expanded = expandedDropdownKey == VehicleFieldKeys.Color,
                    onExpandedChange = { expanded ->
                        expandedDropdownKey = if (expanded) VehicleFieldKeys.Color else null
                        editingFieldKey = null
                    },
                    onOptionSelected = {
                        currentVehicleData = currentVehicleData.copy(colorLabel = it)
                        expandedDropdownKey = null
                    }
                )
            }

            item {
                DropdownVehicleFieldCard(
                    label = "Тип кузова",
                    value = currentVehicleData.bodyType,
                    icon = Icons.Filled.DirectionsCar,
                    options = VehicleBodyTypes,
                    expanded = expandedDropdownKey == VehicleFieldKeys.BodyType,
                    onExpandedChange = { expanded ->
                        expandedDropdownKey = if (expanded) VehicleFieldKeys.BodyType else null
                        editingFieldKey = null
                    },
                    onOptionSelected = {
                        currentVehicleData = currentVehicleData.copy(bodyType = it)
                        expandedDropdownKey = null
                    }
                )
            }

            item {
                ManualVehicleFieldCard(
                    label = "Общие заметки",
                    value = currentVehicleData.notes,
                    icon = Icons.AutoMirrored.Filled.Notes,
                    isEditing = editingFieldKey == VehicleFieldKeys.Notes,
                    singleLine = false,
                    onStartEditing = {
                        editingFieldKey = VehicleFieldKeys.Notes
                        expandedDropdownKey = null
                    },
                    onDoneEditing = {
                        editingFieldKey = null
                    },
                    onValueChange = {
                        currentVehicleData = currentVehicleData.copy(notes = it.take(MAX_NOTES_LENGTH))
                    },
                    errorMessage = validationErrors.notes
                )
            }

            if (isSuccessVisible) {
                item {
                    SuccessMessageCard()
                }
            }

            if (!saveErrorMessage.isNullOrBlank()) {
                item {
                    Text(
                        text = saveErrorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            item {
                ContinueButton(
                    text = if (isSaving) "Сохраняю..." else "Сохранить изменения",
                    onClick = {
                        if (!isSaveEnabled) return@ContinueButton
                        onSaveVehicleData(currentVehicleData.toCarUpdateRequest())
                        savedVehicleData = currentVehicleData
                        editingFieldKey = null
                        expandedDropdownKey = null
                        isSuccessVisible = true
                    },
                    enabled = isSaveEnabled,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}

@Composable
private fun VehicleDataHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BackButton(onClick = onBackClick)

        Text(
            text = "Данные автомобиля",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun VehicleHeroCard(
    model: String,
    bodyType: String,
    mileage: String,
    year: String,
    bodyColor: Color,
    colorKey: String
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LambaSpacing.CardPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = model,
                    style = MaterialTheme.typography.titleLarge,
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = bodyType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(LambaRadius.Medium))
                    .background(colorScheme.primaryContainer.copy(alpha = 0.45f))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                CarImage(
                    bodyColor = bodyColor,
                    bodyType = bodyType,
                    colorKey = colorKey
                )
            }

            Text(
                text = "$mileage • $year",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ManualVehicleFieldCard(
    label: String,
    value: String,
    icon: ImageVector,
    isEditing: Boolean,
    onStartEditing: () -> Unit,
    onDoneEditing: () -> Unit,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    valueSuffix: String? = null,
    errorMessage: String? = null
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onStartEditing),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(1.dp, colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LambaSpacing.CardPadding),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            FieldLeadingIcon(icon = icon)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onSurfaceVariant
                )

                if (isEditing) {
                    TextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = singleLine,
                        keyboardOptions = keyboardOptions,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = colorScheme.onSurface,
                            unfocusedTextColor = colorScheme.onSurface,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent,
                            cursorColor = colorScheme.onPrimaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        ),
                        suffix = valueSuffix?.let {
                            {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (!valueSuffix.isNullOrBlank()) {
                            Text(
                                text = valueSuffix,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.error
                    )
                }
            }

            IconButton(
                onClick = if (isEditing) onDoneEditing else onStartEditing
            ) {
                Icon(
                    imageVector = if (isEditing) Icons.Filled.Check else Icons.Filled.Edit,
                    contentDescription = if (isEditing) "Завершить редактирование" else "Редактировать поле",
                    tint = if (isEditing) colorScheme.onPrimaryContainer else colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DropdownVehicleFieldCard(
    label: String,
    value: String,
    icon: ImageVector,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit,
    errorMessage: String? = null
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandedChange(!expanded) },
            shape = RoundedCornerShape(LambaRadius.Large),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
            border = BorderStroke(1.dp, colorScheme.outlineVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LambaSpacing.CardPadding),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FieldLeadingIcon(icon = icon)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (!errorMessage.isNullOrBlank()) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.error
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Открыть список",
                    tint = colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .background(colorScheme.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun FieldLeadingIcon(
    icon: ImageVector
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(LambaRadius.Medium))
            .background(colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun SuccessMessageCard() {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Medium),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer),
        border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.32f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(LambaRadius.Pill))
                    .background(colorScheme.onPrimaryContainer.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = "Данные автомобиля обновлены",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

data class VehicleDataUiModel(
    val make: String = "Lada",
    val model: String = "2101",
    val year: String = "1975",
    val mileage: String = "100000",
    val colorLabel: String = "Красный",
    val bodyType: String = "Кроссовер",
    val notes: String = "Кроссовер",
) {
    fun toEditableData(): VehicleEditableData {
        return VehicleEditableData(
            make = make,
            model = model,
            year = year,
            mileage = mileage,
            colorLabel = colorLabel,
            bodyType = bodyType,
            notes = notes
        )
    }
}

data class VehicleEditableData(
    val make: String,
    val model: String,
    val year: String,
    val mileage: String,
    val colorLabel: String,
    val bodyType: String,
    val notes: String
)

private data class VehicleValidationErrors(
    val make: String? = null,
    val model: String? = null,
    val year: String? = null,
    val mileage: String? = null,
    val notes: String? = null
) {
    val isValid: Boolean
        get() = make == null && model == null && year == null && mileage == null && notes == null
}

private fun VehicleEditableData.validate(): VehicleValidationErrors {
    val cleanMake = make.trim()
    val cleanModel = model.trim()
    val cleanYear = year.trim()
    val cleanMileage = mileage.filter { it.isDigit() }
    val currentYear = Year.now().value + 1
    val parsedYear = cleanYear.toIntOrNull()
    val parsedMileage = cleanMileage.toIntOrNull()

    return VehicleValidationErrors(
        make = when {
            cleanMake.isBlank() -> "Выберите марку автомобиля"
            cleanMake.length > MAX_MODEL_LENGTH -> "Максимум $MAX_MODEL_LENGTH символов"
            else -> null
        },
        model = when {
            cleanModel.isBlank() -> "Выберите модель автомобиля"
            cleanModel.length < 2 -> "Минимум 2 символа"
            cleanModel.length > MAX_MODEL_LENGTH -> "Максимум $MAX_MODEL_LENGTH символов"
            !cleanModel.all { it.isLetterOrDigit() || it.isWhitespace() || it in MODEL_ALLOWED_SYMBOLS } ->
                "Можно использовать буквы, цифры, пробел, - / ."
            else -> null
        },
        year = when {
            cleanYear.isBlank() -> "Укажите год выпуска"
            parsedYear == null -> "Год должен быть числом"
            parsedYear !in MIN_CAR_YEAR..currentYear -> "Год должен быть от $MIN_CAR_YEAR до $currentYear"
            else -> null
        },
        mileage = when {
            cleanMileage.isBlank() -> "Укажите пробег"
            parsedMileage == null -> "Пробег должен быть числом"
            parsedMileage > MAX_MILEAGE_KM -> "Пробег не больше ${MAX_MILEAGE_KM.formatMileage()} км"
            else -> null
        },
        notes = when {
            notes.length > MAX_NOTES_LENGTH -> "Максимум $MAX_NOTES_LENGTH символов"
            else -> null
        }
    )
}

private fun String.sanitizeModelInput(): String {
    return filter { it.isLetterOrDigit() || it.isWhitespace() || it in MODEL_ALLOWED_SYMBOLS }
        .take(MAX_MODEL_LENGTH)
}

private fun String.filterDigits(maxLength: Int): String {
    return filter { it.isDigit() }.take(maxLength)
}

private fun VehicleEditableData.displayName(): String {
    return listOf(make, model)
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .joinToString(separator = " ")
        .ifBlank { "Автомобиль" }
}

private fun String.displayMileage(): String {
    return filter { it.isDigit() }
        .toIntOrNull()
        ?.let { "${it.formatMileage()} км" }
        ?: "0 км"
}

private fun List<String>.withCurrentOption(currentValue: String): List<String> {
    val cleanValue = currentValue.trim()
    return if (cleanValue.isBlank() || any { it.equals(cleanValue, ignoreCase = true) }) {
        this
    } else {
        listOf(cleanValue) + this
    }
}

private val VehicleEditableDataSaver: Saver<VehicleEditableData, List<String>> = Saver(
    save = {
        listOf(
            it.make,
            it.model,
            it.year,
            it.mileage,
            it.colorLabel,
            it.bodyType,
            it.notes
        )
    },
    restore = {
        VehicleEditableData(
            make = it[0],
            model = it[1],
            year = it[2],
            mileage = it[3],
            colorLabel = it[4],
            bodyType = it[5],
            notes = it[6]
        )
    }
)

fun CarResponse?.toVehicleDataUiModel(): VehicleDataUiModel {
    if (this == null) return VehicleDataUiModel()
    val resolvedMake = make?.trim()?.takeIf { it.isNotBlank() } ?: CarBrands.first()

    return VehicleDataUiModel(
        make = resolvedMake,
        model = model.trim().ifBlank { CarModelsByBrand[resolvedMake]?.firstOrNull() ?: "Автомобиль" },
        year = year.toString(),
        mileage = currentMileageKm.toString(),
        colorLabel = color.toColorLabel(),
        bodyType = bodyType.toBodyTypeLabel(),
        notes = notes?.takeIf { it.isNotBlank() } ?: "Не указано"
    )
}

private fun VehicleEditableData.toCarUpdateRequest(): CarUpdateRequest {
    return CarUpdateRequest(
        make = make.trim().takeIf { it.isNotBlank() },
        model = model.trim().ifBlank { "Автомобиль" },
        year = year.filter { it.isDigit() }.toIntOrNull(),
        currentMileageKm = mileage.filter { it.isDigit() }.toIntOrNull(),
        color = colorLabel.toBackendColor(),
        bodyType = bodyType.toBackendBodyType(),
        notes = notes.trim().takeIf { it.isNotBlank() && it != "Не указано" }
    )
}

private fun Int.formatMileage(): String {
    return NumberFormat
        .getIntegerInstance(Locale.forLanguageTag("ru-RU"))
        .format(this)
}

private const val MIN_CAR_YEAR = 1886
private const val MAX_MODEL_LENGTH = 80
private const val MAX_NOTES_LENGTH = 500
private const val MAX_MILEAGE_KM = 2_000_000
private val MODEL_ALLOWED_SYMBOLS = setOf('-', '/', '.', '_')

private object VehicleFieldKeys {
    const val Make = "make"
    const val Model = "model"
    const val Year = "year"
    const val Mileage = "mileage"
    const val Color = "color"
    const val BodyType = "body_type"
    const val Notes = "notes"
}

private val VehicleColorOptions = listOf(
    "Красный",
    "Синий",
    "Зеленый",
    "Серый",
    "Черный",
    "Белый"
)

private val VehicleBodyTypes = listOf(
    "Седан",
    "Хэтчбек",
    "Кроссовер",
    "Купе",
    "Универсал",
    "Пикап"
)

private fun String.toVehicleColor(): Color {
    return when (this) {
        "Красный" -> LambaVehicleRed
        "Синий" -> LambaVehicleBlue
        "Зеленый" -> LambaVehicleGreen
        "Серый" -> LambaVehicleSilver
        "Черный" -> LambaInk
        "Белый" -> LambaSurface
        else -> LambaVehicleRed
    }
}

private fun String?.toColorLabel(): String {
    return when (this?.trim()?.lowercase()) {
        "red" -> "Красный"
        "blue" -> "Синий"
        "green" -> "Зеленый"
        "graphite", "black" -> "Черный"
        "white" -> "Белый"
        else -> "Серый"
    }
}

private fun String.toBackendColor(): String {
    return when (trim().lowercase()) {
        "красный", "red" -> "red"
        "синий", "blue" -> "blue"
        "зеленый", "зелёный", "green" -> "green"
        "черный", "чёрный", "графит", "graphite", "black" -> "graphite"
        "белый", "white" -> "white"
        else -> "silver"
    }
}

private fun String?.toBodyTypeLabel(): String {
    return when (this?.trim()?.lowercase()) {
        "hatchback" -> "Хэтчбек"
        "crossover" -> "Кроссовер"
        "coupe" -> "Купе"
        "wagon" -> "Универсал"
        "pickup" -> "Пикап"
        "cabriolet" -> "Кабриолет"
        else -> "Седан"
    }
}

private fun String.toBackendBodyType(): String {
    return when (trim().lowercase()) {
        "хэтчбек", "hatchback" -> "hatchback"
        "кроссовер", "crossover" -> "crossover"
        "купе", "coupe" -> "coupe"
        "универсал", "wagon" -> "wagon"
        "пикап", "pickup" -> "pickup"
        "кабриолет", "cabriolet" -> "cabriolet"
        else -> "sedan"
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun VehicleDataScreenPreview() {
    LAMBA_MVPv0Theme {
        VehicleDataScreen()
    }
}
