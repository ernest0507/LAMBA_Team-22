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
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
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
import kotlinx.coroutines.delay

@Composable
fun VehicleDataScreen(
    onBackClick: () -> Unit = {},
    vehicleData: VehicleDataUiModel = VehicleDataUiModel()
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

    val isSaveEnabled = currentVehicleData != savedVehicleData
    val bodyColor = currentVehicleData.colorLabel.toVehicleColor()
    val colorScheme = MaterialTheme.colorScheme

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
                    model = currentVehicleData.model,
                    bodyType = currentVehicleData.bodyType,
                    mileage = currentVehicleData.mileage,
                    year = currentVehicleData.year,
                    condition = vehicleData.condition,
                    bodyColor = bodyColor
                )
            }

            item {
                ManualVehicleFieldCard(
                    label = "Марка и модель",
                    value = currentVehicleData.model,
                    icon = Icons.Filled.DirectionsCar,
                    isEditing = editingFieldKey == VehicleFieldKeys.Model,
                    onStartEditing = {
                        editingFieldKey = VehicleFieldKeys.Model
                        expandedDropdownKey = null
                    },
                    onDoneEditing = {
                        editingFieldKey = null
                    },
                    onValueChange = { currentVehicleData = currentVehicleData.copy(model = it) }
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
                    onValueChange = { currentVehicleData = currentVehicleData.copy(year = it) }
                )
            }

            item {
                ManualVehicleFieldCard(
                    label = "Пробег",
                    value = currentVehicleData.mileage,
                    icon = Icons.Filled.Speed,
                    isEditing = editingFieldKey == VehicleFieldKeys.Mileage,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onStartEditing = {
                        editingFieldKey = VehicleFieldKeys.Mileage
                        expandedDropdownKey = null
                    },
                    onDoneEditing = {
                        editingFieldKey = null
                    },
                    onValueChange = { currentVehicleData = currentVehicleData.copy(mileage = it) }
                )
            }

            item {
                ManualVehicleFieldCard(
                    label = "VIN",
                    value = currentVehicleData.vin,
                    icon = Icons.Filled.Numbers,
                    isEditing = editingFieldKey == VehicleFieldKeys.Vin,
                    onStartEditing = {
                        editingFieldKey = VehicleFieldKeys.Vin
                        expandedDropdownKey = null
                    },
                    onDoneEditing = {
                        editingFieldKey = null
                    },
                    onValueChange = { currentVehicleData = currentVehicleData.copy(vin = it) }
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
                DropdownVehicleFieldCard(
                    label = "Тип топлива",
                    value = currentVehicleData.fuelType,
                    icon = Icons.Filled.LocalGasStation,
                    options = VehicleFuelTypes,
                    expanded = expandedDropdownKey == VehicleFieldKeys.FuelType,
                    onExpandedChange = { expanded ->
                        expandedDropdownKey = if (expanded) VehicleFieldKeys.FuelType else null
                        editingFieldKey = null
                    },
                    onOptionSelected = {
                        currentVehicleData = currentVehicleData.copy(fuelType = it)
                        expandedDropdownKey = null
                    }
                )
            }

            item {
                DropdownVehicleFieldCard(
                    label = "Коробка передач",
                    value = currentVehicleData.transmission,
                    icon = Icons.Filled.Settings,
                    options = VehicleTransmissionTypes,
                    expanded = expandedDropdownKey == VehicleFieldKeys.Transmission,
                    onExpandedChange = { expanded ->
                        expandedDropdownKey = if (expanded) VehicleFieldKeys.Transmission else null
                        editingFieldKey = null
                    },
                    onOptionSelected = {
                        currentVehicleData = currentVehicleData.copy(transmission = it)
                        expandedDropdownKey = null
                    }
                )
            }

            item {
                ManualVehicleFieldCard(
                    label = "Госномер",
                    value = currentVehicleData.licensePlate,
                    icon = Icons.Filled.Numbers,
                    isEditing = editingFieldKey == VehicleFieldKeys.LicensePlate,
                    onStartEditing = {
                        editingFieldKey = VehicleFieldKeys.LicensePlate
                        expandedDropdownKey = null
                    },
                    onDoneEditing = {
                        editingFieldKey = null
                    },
                    onValueChange = { currentVehicleData = currentVehicleData.copy(licensePlate = it) }
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
                    onValueChange = { currentVehicleData = currentVehicleData.copy(notes = it) }
                )
            }

            if (isSuccessVisible) {
                item {
                    SuccessMessageCard()
                }
            }

            item {
                ContinueButton(
                    text = "Сохранить изменения",
                    onClick = {
                        if (!isSaveEnabled) return@ContinueButton
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
    condition: String,
    bodyColor: Color
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
                    bodyType = bodyType
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$mileage · $year",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )

                Box(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(LambaRadius.Pill))
                        .background(colorScheme.primaryContainer)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Состояние $condition",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
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
    singleLine: Boolean = true
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
                        )
                    )
                } else {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
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
    onOptionSelected: (String) -> Unit
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
    val model: String = "ВАЗ-2101",
    val year: String = "1975",
    val mileage: String = "100 000 км",
    val vin: String = "Не указано",
    val colorLabel: String = "Красный",
    val bodyType: String = "Кроссовер",
    val fuelType: String = "Бензин",
    val transmission: String = "Механика",
    val licensePlate: String = "Не указано",
    val notes: String = "Кроссовер",
    val condition: String = "92%"
) {
    fun toEditableData(): VehicleEditableData {
        return VehicleEditableData(
            model = model,
            year = year,
            mileage = mileage,
            vin = vin,
            colorLabel = colorLabel,
            bodyType = bodyType,
            fuelType = fuelType,
            transmission = transmission,
            licensePlate = licensePlate,
            notes = notes
        )
    }
}

data class VehicleEditableData(
    val model: String,
    val year: String,
    val mileage: String,
    val vin: String,
    val colorLabel: String,
    val bodyType: String,
    val fuelType: String,
    val transmission: String,
    val licensePlate: String,
    val notes: String
)

private val VehicleEditableDataSaver: Saver<VehicleEditableData, List<String>> = Saver(
    save = {
        listOf(
            it.model,
            it.year,
            it.mileage,
            it.vin,
            it.colorLabel,
            it.bodyType,
            it.fuelType,
            it.transmission,
            it.licensePlate,
            it.notes
        )
    },
    restore = {
        VehicleEditableData(
            model = it[0],
            year = it[1],
            mileage = it[2],
            vin = it[3],
            colorLabel = it[4],
            bodyType = it[5],
            fuelType = it[6],
            transmission = it[7],
            licensePlate = it[8],
            notes = it[9]
        )
    }
)

private object VehicleFieldKeys {
    const val Model = "model"
    const val Year = "year"
    const val Mileage = "mileage"
    const val Vin = "vin"
    const val Color = "color"
    const val BodyType = "body_type"
    const val FuelType = "fuel_type"
    const val Transmission = "transmission"
    const val LicensePlate = "license_plate"
    const val Notes = "notes"
}

private val VehicleColorOptions = listOf(
    "Красный",
    "Синий",
    "Зелёный",
    "Серый",
    "Чёрный",
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

private val VehicleFuelTypes = listOf(
    "Бензин",
    "Дизель",
    "Гибрид",
    "Электро"
)

private val VehicleTransmissionTypes = listOf(
    "Механика",
    "Автомат",
    "Робот",
    "Вариатор"
)

private fun String.toVehicleColor(): Color {
    return when (this) {
        "Красный" -> LambaVehicleRed
        "Синий" -> LambaVehicleBlue
        "Зелёный" -> LambaVehicleGreen
        "Серый" -> LambaVehicleSilver
        "Чёрный" -> LambaInk
        "Белый" -> LambaSurface
        else -> LambaVehicleRed
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun VehicleDataScreenPreview() {
    LAMBA_MVPv0Theme {
        VehicleDataScreen()
    }
}
